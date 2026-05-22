from . import chat_bp
from flask import request, session
from app.shared.features.jwt_token.service import (
    get_id,
    get_jwt_from_header,
    create_unauthorized_response,
)
from app.shared.consts import ResultsCodes
from .service import (
    create_chat,
    delete_chat,
    send_message,
    get_messages,
    get_chat_project_id,
    get_chats,
)
from app.shared.extensions import socketio
from .repository import chat_repo, project_repo
from flask_socketio import join_room, leave_room


@chat_bp.route("/chat/create", methods=["POST"])
def create_chat_route():
    """
    Создание чата
    ---
    tags:
      - features/chat
    description: |
      Создает чат
    requestBody:
      required: true
      content:
        application/json:
          schema:
            type: object
            properties:
              project_id:
                type: integer
                example: 80
              identificator:
                type: string
                example: "abc5"
    responses:
      201:
        description: Успешное создание
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
        description: Ошибка создания чата
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
    identificator = data["identificator"]

    chat, result = create_chat(project_id, id, identificator)

    if result == ResultsCodes.OK:
        chats, result_code = get_chats(project_id)
        chats_list = []
        for c in chats:
            messages, result_getting_messages = get_messages(c.id)
            if result_getting_messages == ResultsCodes.OK:
                chats_list.append({"id": c.id, "messages": messages})
        return {}, 201
    else:
        return {"message": result}, 409


@chat_bp.route("/chat/delete", methods=["DELETE"])
def delete_chat_route():
    """
    Удаление чата
    ---
    tags:
      - features/chat
    description: |
      Удаляет чат
    requestBody:
      required: true
      content:
        application/json:
          schema:
            type: object
            properties:
              chat_id:
                type: integer
                example: 80
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
        description: Ошибка удаления чата
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

    chat_id = data["chat_id"]

    project_id, result = delete_chat(chat_id, id)

    if result == ResultsCodes.OK:
        chats, result_code = get_chats(project_id)
        chats_list = []
        for c in chats:
            messages, result_getting_messages = get_messages(c.id)
            if result_getting_messages == ResultsCodes.OK:
                chats_list.append({"id": c.id, "messages": messages})
        return {"deleted_chat_id": chat_id}, 200
    else:
        return {"message": result}, 409


@chat_bp.route("/message/create", methods=["POST"])
def create_message_route():
    """
    Создание сообщения
    ---
    tags:
      - features/chat
    description: |
      Создает сообщение
    requestBody:
      required: true
      content:
        application/json:
          schema:
            type: object
            properties:
              chat_id:
                type: integer
                example: 80
              message_text:
                type: string
                example: "new message"
    responses:
      201:
        description: Успешное создание
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
        description: Ошибка создания сообщения
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

    chat_id = data["chat_id"]
    message_text = data["message_text"]
    author_id = id

    result = send_message(chat_id, message_text, author_id)

    if result == ResultsCodes.OK:
        project_id, get_project_id_result = get_chat_project_id(chat_id)
        try:
            messages, code = get_messages(chat_id)
            if code == ResultsCodes.OK:
                socketio.emit(
                    "get_messages",
                    {"chat_id": chat_id, "messages": messages},
                    room=f"chat_{chat_id}",
                )
        except Exception as e:
            print(f"Error: {e}, ResultCodes: {get_project_id_result}")
        return {}, 200
    else:
        return {"message": result}, 409


@socketio.on("join_chat_room")
def join_chat_room_socket(data):
    """
    Сокет join_chat_room. Клиент открывает чат и попадает в его комнату. Необходимо вызывать при открытии чата.

    Args:
        data (dict): Словарь с данными проекта.
            {
                project_id (int): Id проекта
                identificator (str): Уникальный идентификатор чата, не id
            }

    Returns:
        bool: True при успешном подключении, False при ошибке

    Example:
        >>> data = {"project_id": 3, "identificator": "abc6"}
    """
    id = session.get("user_id")
    if not id:
        return False

    project_id = data.get("project_id")
    chat_identificator = data.get("chat_identificator")
    if not project_repo.is_user_in_project(id, project_id):
        return False

    project = project_repo.get_by_id(project_id)
    if project:
        chat = chat_repo.get_by_identificator(chat_identificator, project_id)
        if chat:
            new_room = f"chat_{chat.id}"
            join_room(new_room)
            try:
                messages, code = get_messages(chat.id)
                if code == ResultsCodes.OK:
                    socketio.emit(
                        "get_messages",
                        {"chat_id": chat.id, "messages": messages},
                        room=f"chat_{chat.id}",
                    )
            except Exception as e:
                print(f"Error: {e}")

    return False


@socketio.on("leave_chat_room")
def leave_chat_room_socket(data):
    """
    Сокет leave_chat_room. Клиент покидает комнату чата

    Args:
        data (dict): Словарь с данными проекта.
          {
            project_id (int): Id проекта
            identificator (str): Уникальный идентификатор чата, не id
          }

    Example:
        >>> data = {"project_id": 3, "identificator": "abc6"}
    """
    id = session.get("user_id")
    if not id:
        return False

    project_id = data.get("project_id")
    chat_identificator = data.get("chat_identificator")

    project = project_repo.get_by_id(project_id)
    if project:
        chat = chat_repo.get_by_identificator(chat_identificator, project_id)
        if chat:
            old_room = f"chat_{chat.id}"
            leave_room(old_room)

    return True
