from . import chat_bp
from flask import request, session
from app.shared.features.jwt_token.service import (
    get_id,
    get_jwt_from_header,
    create_unauthorized_response,
)
from app.shared.consts import ResultsCodes


@chat_bp.route("/chat/create", methods=["POST"])
def create_chat_route():
    """
    Создание чат
    ---
    tags:
      - features/chat
    description: |
      Создает чат
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
            name:
              type: string
              example: "main.txt"
            project_name:
              type: string
              example: "TestProject"
            parent_id:
              type: int
              example: 13 | "" если родителя нет
            is_folder:
              type: boolean
              example: false
    responses:
      201:
        description: Успешное создание
      401:
        description: Проблема с токеном
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Токен недействителен"
      403:
        description: Неверные учетные данные, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Неверные учетные данные"
      409:
        description: Ошибка создания файла
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Пользователь не найден"
    """
    auth_header = request.headers.get("Authorization")
    token, result = get_jwt_from_header(auth_header)

    if result == ResultsCodes.NO_TOKEN:
        response = create_unauthorized_response()
        return response

    data = request.json
    id, id_result = get_id(token)
    if id_result != ResultsCodes.OK:
        return {"message": id_result}, 403

    author_id = data["author_id"]
    project_id = data["project_id"]


@chat_bp.route("/chat/delete", methods=["DELETE"])
def delete_chat_route():
    """
    Удаление чата
    ---
    tags:
      - features/chat
    description: |
      Создает чат
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
            name:
              type: string
              example: "main.txt"
            project_name:
              type: string
              example: "TestProject"
            parent_id:
              type: int
              example: 13 | "" если родителя нет
            is_folder:
              type: boolean
              example: false
    responses:
      201:
        description: Успешное создание
      401:
        description: Проблема с токеном
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Токен недействителен"
      403:
        description: Неверные учетные данные, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Неверные учетные данные"
      409:
        description: Ошибка создания файла
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Пользователь не найден"
    """
    auth_header = request.headers.get("Authorization")
    token, result = get_jwt_from_header(auth_header)

    if result == ResultsCodes.NO_TOKEN:
        response = create_unauthorized_response()
        return response

    data = request.json
    id, id_result = get_id(token)
    if id_result != ResultsCodes.OK:
        return {"message": id_result}, 403

    chat_id = data["chat_id"]
    message_text = data["message_text"]
    author_id = data["author_id"]


@chat_bp.route("/message/send", methods=["POST"])
def create_message_route():
    """
    Создание сообщения
    ---
    tags:
      - features/chat
    description: |
      Создает сообщение
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
            name:
              type: string
              example: "main.txt"
            project_name:
              type: string
              example: "TestProject"
            parent_id:
              type: int
              example: 13 | "" если родителя нет
            is_folder:
              type: boolean
              example: false
    responses:
      201:
        description: Успешное создание
      401:
        description: Проблема с токеном
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Токен недействителен"
      403:
        description: Неверные учетные данные, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Неверные учетные данные"
      409:
        description: Ошибка создания файла
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Пользователь не найден"
    """
    auth_header = request.headers.get("Authorization")
    token, result = get_jwt_from_header(auth_header)

    if result == ResultsCodes.NO_TOKEN:
        response = create_unauthorized_response()
        return response

    data = request.json
    id, id_result = get_id(token)
    if id_result != ResultsCodes.OK:
        return {"message": id_result}, 403

    chat_id = data["chat_id"]
    message_text = data["message_text"]
    author_id = data["author_id"]
