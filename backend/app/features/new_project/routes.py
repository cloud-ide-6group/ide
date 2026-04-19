from . import new_project_bp
from ...shared.features.jwt_token.routes import get_id
from app.shared.features.languages.service import lang_exists
from flask import request, make_response
from .service import *


@new_project_bp.route("/cteate/project", methods=["POST"])
def create_new_project():
    """
    Создание нового проекта
    ---
    tags:
      - features/new_project
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
