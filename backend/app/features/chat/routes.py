from . import chat_bp
from flask import request
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

    chat, result = create_chat(project_id, id)

    if result == ResultsCodes.OK:
        chats, result_code = get_chats(project_id)
        chats_list = []
        for c in chats:
            messages, result_getting_messages = get_messages(c.id)
            if result_getting_messages == ResultsCodes.OK:
                chats_list.append({"id": c.id, "messages": messages})
        socketio.emit(
            "get_chats",
            {"chats_list": chats_list},
            room=f"project_{project_id}",
        )
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
        socketio.emit(
            "get_chats",
            {"chats_list": chats_list},
            room=f"project_{project_id}",
        )
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
                    room=f"project_{project_id}",
                )
        except Exception as e:
            print(f"Error: {e}, ResultCodes: {get_project_id_result}")
        return {}, 200
    else:
        return {"message": result}, 409
