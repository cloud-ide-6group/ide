from . import files_bp
from flask import request
from app.shared.features.jwt_token.service import (
    get_id,
    get_jwt_from_header,
    create_unauthorized_response,
)
from app.shared.consts import ResultsCodes
from .service import create_file, delete_file, save_file_content
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
            parent_name:
              type: string
              example: "file.py"
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
        data["name"], data["project_name"], data["parent_name"], data["is_folder"], id
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
    auth_header = request.headers.get("Authorization")
    token, result = get_jwt_from_header(auth_header)
    if result != ResultsCodes.OK:
        return create_unauthorized_response()

    id, result = get_id(token)

    file_id = data.get("file_id")
    new_content = data.get("content")

    save_file_content(file_id, new_content)

    return True
