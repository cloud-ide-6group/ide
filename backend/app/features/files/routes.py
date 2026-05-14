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
      Добавляет пользователя в проект
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
            invited_user_email:
              type: string
              example: "test@mail.ru"
    responses:
      200:
        description: Успешное пришлашение
        schema:
          type: object
          properties:
              project_id:
                type: int
                example: 25
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
        data["name"], data["project_name"], data["parent_name"], data["is_folder"]
    )

    if result == ResultsCodes.OK:
        return {}, 201
    else:
        return {"message": result}, 409


@files_bp.route("/files/delete", methods=["POST"])
def delete_file_route():
    """
    Приглашение пользователя в проект
    ---
    tags:
      - features/files
    description: |
      Добавляет пользователя в проект
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
            invited_user_email:
              type: string
              example: "test@mail.ru"
    responses:
      200:
        description: Успешное пришлашение
        schema:
          type: object
          properties:
              project_id:
                type: int
                example: 25
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
    result = delete_file(file_id)

    if result == ResultsCodes.OK:
        return {}, 201
    else:
        return {"message": result}, 409


@socketio.on("update_file_content")
def update_file_content(data):
    """
    Получает новый контент файла от клиента.
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
