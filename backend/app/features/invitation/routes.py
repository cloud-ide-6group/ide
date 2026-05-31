from . import invitation_bp
from flask import request, session
from app.shared.features.jwt_token.service import (
    get_id,
    get_jwt_from_header,
    create_unauthorized_response,
)
from app.shared.consts import ResultsCodes
from .service import add_user_in_project, delete_user_from_project
from app.shared.extensions import socketio
from flask_socketio import leave_room
from .repository import notification_repo


@invitation_bp.route("/invite", methods=["POST"])
def invite():
    """
    Приглашение пользователя в проект
    ---
    tags:
      - features/invitation
    description: |
      Добавляет пользователя в проект
    requestBody:
      required: true
      content:
        application/json:
          schema:
            type: object
            properties:
              project_id:
                type: integer
                example: 89
              invited_user_email:
                type: string
                example: "test@mail.ru"
    responses:
      200:
        description: Успешное приглашение
        content:
          application/json:
            schema:
              type: object
              properties:
                id:
                  type: integer
                  example: 24
                email:
                  type: string
                  example: "test@mail.ru"
                name:
                  type: string
                  example: "username"
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
    security:
      - BearerAuth: []
    """
    auth_header = request.headers.get("Authorization")
    token, result = get_jwt_from_header(auth_header)

    if result != ResultsCodes.OK:
        response = create_unauthorized_response(result)
        return response

    data = request.json
    id, id_result = get_id(token)
    if id_result != ResultsCodes.OK:
        return {"message": id_result}, 401

    project_id = data["project_id"]
    invited_user_email = data["invited_user_email"]

    user, result = add_user_in_project(project_id, invited_user_email, id)
    if result == ResultsCodes.OK:
        return {"id": user.id, "email": user.email, "name": user.name}, 200
    else:
        return {"message": result}, 409


@invitation_bp.route("/invited/delete", methods=["DELETE"])
def delete_invited():
    """
    Удалить пользователя из проекта
    ---
    tags:
      - features/invitation
    description: |
      Удаляет ранее приглашенного пользователя из проекта
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
    security:
      - BearerAuth: []
    """
    auth_header = request.headers.get("Authorization")
    token, result = get_jwt_from_header(auth_header)

    if result != ResultsCodes.OK:
        response = create_unauthorized_response(result)
        return response

    data = request.json
    id, id_result = get_id(token)
    if id_result != ResultsCodes.OK:
        return {"message": id_result}, 401

    project_id = data["project_id"]
    invited_user_email = data["invited_user_email"]

    user, result = delete_user_from_project(project_id, invited_user_email, id)
    if result == ResultsCodes.OK:
        user_id = user.id
        socketio.emit(
            "removed_from_project",
            {"project_id": project_id},
            room=f"{user.id}",
        )
        notification_repo.delete_by_reciever_project_id(user_id, project_id)
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
