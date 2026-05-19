from . import project_bp
from ...shared.features.jwt_token.routes import get_id
from app.shared.features.languages.service import lang_exists
from app.shared.consts import ResultsCodes
from flask import request, make_response
from .service import (
    create_project_dir,
    create_project,
    is_user_invited,
    get_project_by_id,
    get_project_files_trees,
)
from flask_socketio import join_room, leave_room
from flask import session
from app.shared.extensions import socketio

# TODO: удаление проекта
# TODO: вынести сокеты в отдельный файл
# TODO: базовые реализации репозиториев
@project_bp.route("/project/create", methods=["POST"])
def create_new_project():
    """
    Создание нового проекта
    ---
    tags:
      - features/project
    parameters:
      - name: Authorization
        in: header
        required: true
        type: string
        example: "Bearer pbkdf2:sha256:260000$xyz..."
      - name: body
        in: body
        required: true
        schema:
          type: object
          properties:
            project_name:
              type: string
              example: "TestProject"
            language_id:
              type: int
              example: 7
    responses:
      201:
        description: Успешное создание
        schema:
          type: object
          properties:
              project_id:
                type: int
                example: 25
      401:
        description: Неверный access токен, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Неверный access токен, доступ запрещен"
      403:
        description: Неверные учетные данные, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Неверные учетные данные"
      409:
        description: Ошибка создания проекта
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Проект уже существует"
    """
    auth_header = request.headers.get("Authorization")
    if not auth_header or not auth_header.startswith("Bearer "):
        response = make_response({"message": "Токен не предоставлен"}, 401)
        response.headers["WWW-Authenticate"] = "Bearer"
        return response

    data = request.json
    access_token = auth_header.split(" ")[1]
    id, id_result = get_id(access_token)
    if id_result != ResultsCodes.OK:
        return {"message": id_result}, 403

    project_name = data["project_name"]
    language_id = data["language_id"]

    is_lang_exists = lang_exists(language_id)
    if not is_lang_exists:
        return {"message": ResultsCodes.INCORRECT_LANG}, 409

    result = create_project_dir(project_name)
    if result != ResultsCodes.OK:
        return {"message": result}, 409

    project, result = create_project(id, project_name, language_id)

    if result != ResultsCodes.OK:
        return {"message": result}, 409

    return {"project_id": project.id}, 201


@socketio.on("join_project_room")
def join_project_room(data):
    """
    Клиент открывает проект и попадает в его комнату. Название события -- join_project_room

    Args:
        data (dict): {
            project_id (int): Id проекта
        }
    """
    id = session.get("user_id")
    if not id:
        return False

    project_id = data.get("project_id")
    project = get_project_by_id(project_id)
    if project:
        if project.owner_id == id or is_user_invited(project_id, id):
            for room in session.get("project_rooms", []):
                leave_room(room)
            new_room = f"project_{project_id}"
            join_room(new_room)
            session["project_rooms"] = [new_room]
            socketio.emit(
                "files_list",
                {"files_trees": get_project_files_trees(project_id)},
                room=f"{id}",
            )
            return True

    return False
