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
    requestBody:
      required: true
      content:
        application/json:
          schema:
            type: object
            properties:
              name:
                type: string
                example: "main.txt"
              project_id:
                type: integer
                example: 81
              parent_id:
                type: integer
                example: 13
              is_folder:
                type: boolean
                example: false
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
        description: Ошибка создания файла
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

    result = create_file(
        data["name"], data["project_id"], data["parent_id"], data["is_folder"], id
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
    requestBody:
      required: true
      content:
        application/json:
          schema:
            type: object
            properties:
              file_id:
                type: integer
                example: 23
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
        description: Ошибка удаления файла
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

    file_id = data["file_id"]
    result = delete_file(file_id, id)

    if result == ResultsCodes.OK:
        return {}, 200
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
    requestBody:
      required: true
      content:
        application/json:
          schema:
            type: object
            properties:
              file_id:
                type: integer
                example: 23
              new_name:
                type: string
                example: "NewFileName"
    responses:
      200:
        description: Успешное переименование
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
        description: Ошибка переименования файла
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
    Сокет update_file_content. Клиент посылает новое содержимое файла, которое рассылается всем остальным пользователям.

    Args:
        data (dict): Словарь с данными файла.
          {
            file_id (int): Уникальный идентификатор файла
            content (str): Новое содержимое файла
          }

    Example:
        >>> data = {"file_id": 3, "content": "The file content"}
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
    Сокет get_file_content. Клиент посылает id файла и получает содержимое файла.

    Args:
        data (dict): {
            "file_id": int
        }

    Example:
        >>> data = {"file_id": 3}
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
