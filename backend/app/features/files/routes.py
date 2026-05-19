from . import files_bp
from flask import request, session
from app.shared.features.jwt_token.service import (
    get_id,
    get_jwt_from_header,
    create_unauthorized_response,
)
from app.shared.consts import ResultsCodes
from .service import (
    create_file,
    delete_file,
    save_file_content,
    get_file_content,
    rename_file,
)
from app.shared.extensions import socketio


@files_bp.route("/files/create", methods=["POST"])
def create_file_route():
    """
    Создание файла
    ---
    tags:
      - features/files
    description: |
      Создает файл
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
              example: 13
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
        description: Ошибка приглашения
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

    result = create_file(
        data["name"], data["project_name"], data["parent_id"], data["is_folder"], id
    )

    if result == ResultsCodes.OK:
        return {}, 201
    else:
        return {"message": result}, 409


@files_bp.route("/files/delete", methods=["DELETE"])
def delete_file_route():
    """
    Удаляет файл
    ---
    tags:
      - features/files
    description: |
      Удалить файл
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
            file_id:
              type: int
              example: 23
    responses:
      200:
        description: Успешное удаление
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
        description: Ошибка приглашения
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

    file_id = data["file_id"]
    result = delete_file(file_id, id)

    if result == ResultsCodes.OK:
        return {}, 201
    else:
        return {"message": result}, 409


@files_bp.route("/files/rename", methods=["PUT"])
def rename_file_route():
    """
    Переименовывает файл
    ---
    tags:
      - features/files
    description: |
      Переименовать файл
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
            file_id:
              type: int
              example: 23
            new_name:
              type: str
              example: "NewFileName"
    responses:
      200:
        description: Успешное переименование
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
        description: Ошибка приглашения
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

    file_id = data["file_id"]
    new_name = data["new_name"]

    result = rename_file(file_id, new_name, id)

    if result == ResultsCodes.OK:
        return {}, 200
    else:
        return {"message": result}, 409


@socketio.on("update_file_content")
def update_file_content(data):
    """
    Клиент посылает новое содержимое файла.

    Args:
        data (dict): {
            file_id (int): Id файла,
            content (str): Новое содержимое
        }
    """
    id = session.get("user_id")
    if not id:
        return False

    file_id = data.get("file_id")
    new_content = data.get("content")

    save_file_content(file_id, new_content)

    return True


@socketio.on("get_file_content")
def get_file_content_socket(data):
    """
    Клиент посылает новое содержимое файла.

    Args:
        data (dict): {
            file_id (int): Id файла
        }
    """
    id = session.get("user_id")
    if not id:
        return False

    file_id = data.get("file_id")

    socketio.emit(
        "send_file_content",
        {"content": get_file_content(file_id)},
        room=f"{id}",
    )

    return True
