from . import invitation_bp
from flask import request, make_response, session
from app.shared.features.jwt_token.service import get_id
from app.shared.consts import ResultsCodes
from .service import add_user_in_project, delete_user_from_project
from app.shared.extensions import socketio
from flask_socketio import leave_room


@invitation_bp.route("/invite", methods=["POST"])
def invite():
    """
    Приглашение пользователя в проект
    ---
    tags:
      - features/invitation
    description: |
      Добавляет пользователя в проект
    parameters:
      - name: Authorization
        in: header
        required: true
        schema:
          type: string
        example: "Bearer pbkdf2:sha256:260000$xyz..."
    requestBody:
      required: true
      content:
        application/json:
          schema:
            type: object
            properties:
              project_name:
                type: string
                example: "TestProject"
              invited_user_email:
                type: string
                example: "test@mail.ru"
    responses:
      200:
        description: Успешное приглашение
      401:
        description: Проблема с токеном
        content:
          application/json:
            schema:
              type: object
              properties:
                message:
                  type: string
                  example: "Токен недействителен"
      403:
        description: Неверные учетные данные, доступ запрещен
        content:
          application/json:
            schema:
              type: object
              properties:
                message:
                  type: string
                  example: "Неверные учетные данные"
      409:
        description: Ошибка приглашения
        content:
          application/json:
            schema:
              type: object
              properties:
                message:
                  type: string
                  example: "Пользователь не найден"
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
    invited_user_email = data["invited_user_email"]

    result = add_user_in_project(project_name, invited_user_email, id)
    if result == ResultsCodes.OK:
        return {}, 200
    else:
        return {"message": result}, 409


@invitation_bp.route("/delete/invited", methods=["DELETE"])
def delete_invited():
    """
    Удалить пользователя из проекта
    ---
    tags:
      - features/invitation
    description: |
      Удаляет ранее приглашенного пользователя из проекта
    parameters:
      - name: Authorization
        in: header
        required: true
        schema:
          type: string
        example: "Bearer pbkdf2:sha256:260000$xyz..."
    requestBody:
      required: true
      content:
        application/json:
          schema:
            type: object
            properties:
              project_id:
                type: integer
                example: 81
              invited_user_email:
                type: string
                example: "test@mail.ru"
    responses:
      200:
        description: Успешное удаление
      401:
        description: Проблема с токеном
        content:
          application/json:
            schema:
              type: object
              properties:
                message:
                  type: string
                  example: "Токен недействителен"
      403:
        description: Неверные учетные данные, доступ запрещен
        content:
          application/json:
            schema:
              type: object
              properties:
                message:
                  type: string
                  example: "Неверные учетные данные"
      409:
        description: Ошибка удаления
        content:
          application/json:
            schema:
              type: object
              properties:
                message:
                  type: string
                  example: "Пользователь не найден"
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

    project_id = data["project_id"]
    invited_user_email = data["invited_user_email"]

    user, result = delete_user_from_project(project_id, invited_user_email, id)
    if result == ResultsCodes.OK:
        socketio.emit(
            "removed_from_project",
            {"project_id": project_id},
            room=f"{user.id}",
        )
        return {}, 200
    else:
        return {"message": result}, 409


@socketio.on("leave_project_room")
def leave_project_room_socket(data):
    """
    Сокет leave_project_room. Клиент покидает комнату проекта

    Args:
        data (dict): Словарь с данными проекта.
          {
            project_id (int): Уникальный идентификатор проекта
          }

    Example:
        >>> data = {"project_id": 3}
    """
    id = session.get("user_id")
    if not id:
        return False

    project_id = data.get("project_id")
    room_name = f"project_{project_id}"
    leave_room(room_name)

    return True
