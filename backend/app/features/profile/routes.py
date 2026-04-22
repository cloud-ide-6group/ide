from . import profile_bp
from flask import request, make_response
from dotenv import load_dotenv
from .service import *
from ...shared.features.jwt_token.service import get_id
from app.shared.dbmodels import User
from app.shared.features.password_hash.service import (
    get_password_hash,
    check_password_with_hash,
)

load_dotenv()


@profile_bp.route("/profile", methods=["GET"])
def profile():
    """
    Получить профиль пользователя. JWT-токен отправляем в заголовке Authorization: Bearer 4f677hu98u...
    ---
    tags:
      - features/profile
    description: |
      Получить профиль пользователя. JWT-токен отправляем в заголовке Authorization: Bearer 4f677hu98u...
    parameters:
      - name: Authorization
        in: header
        required: true
        type: string
        example: "Bearer pbkdf2:sha256:260000$xyz..."
    responses:
      200:
        description: Получение данных
        schema:
          type: object
          properties:
            name:
              type: string
              example: "username"
            photo:
              type: string
              format: byte
              description: "Фото в формате base64"
              example: "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg=="
            email:
              type: string
              example: "user@mail.ru"
            projects:
              type: array
              items:
                type: object
                properties:
                  id:
                    type: integer
                    description: "ID проекта"
                    example: 1
                  name:
                    type: string
                    description: "Название проекта"
                    example: "Project34"
              example: [{"id": 1, "name": "Project1"}, {"id": 15, "name": "Project2"}]
      401:
        description: Неверный access токен, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Неверный access токен, доступ запрещен"
      404:
        description: Пользователь не найден, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Пользователь не найден, доступ запрещен"
    """
    auth_header = request.headers.get("Authorization")
    if not auth_header or not auth_header.startswith("Bearer "):
        response = make_response({"message": ResultsCodes.REFRESH_TOKEN_NEEDED}, 401)
        response.headers["WWW-Authenticate"] = "Bearer"
        return response

    token = auth_header.split(" ")[1]

    id, result = get_id(token)
    if result != ResultsCodes.OK:
        response = make_response({"message": result}, 401)
        response.headers["WWW-Authenticate"] = "Bearer"
        return response

    user, result = get_user_data(id)
    if user == None:
        return {"message": ResultsCodes.USER_NOT_FOUND}, 404
    if result != ResultsCodes.OK:
        return {"message": result}, 404

    photo = get_photo_base_64(user.photo_path)

    projects, result = get_user_projects(id)
    if result != ResultsCodes.OK:
        return {"message": result}, 404

    return {
        "projects": projects,
        "name": user.name,
        "photo": photo,
        "email": user.email,
    }, 200


@profile_bp.route("/profile/update/data", methods=["PUT"])
def update_profile():
    """
    Обновить профиль. JWT-токен отправляем в заголовке Authorization: Bearer 4f677hu98u...
    ---
    tags:
      - features/profile
    description: |
      Обновить профиль. JWT-токен отправляем в заголовке Authorization: Bearer 4f677hu98u...
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
            email:
              type: email
              example: "example@examp.le"
            name:
              type: string
              example: "newName"
    responses:
      200:
        description: Данные обновлены
      401:
        description: Неверный access токен, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Неверный access токен, доступ запрещен"
      404:
        description: Пользователь не найден, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Пользователь не найден, доступ запрещен"
    """
    auth_header = request.headers.get("Authorization")
    if not auth_header or not auth_header.startswith("Bearer "):
        response = make_response({"message": ResultsCodes.REFRESH_TOKEN_NEEDED}, 401)
        response.headers["WWW-Authenticate"] = "Bearer"
        return response

    token = auth_header.split(" ")[1]

    id, result = get_id(token)
    if result != ResultsCodes.OK:
        response = make_response({"message": result}, 401)
        response.headers["WWW-Authenticate"] = "Bearer"
        return response

    data = request.json

    user = user_repo.update_user(id, data["email"], data["name"], None, None)

    if user != None:
        return {"message": ResultsCodes.DATA_UPDATED}, 200
    else:
        return {"message": ResultsCodes.USER_NOT_FOUND}, 409


@profile_bp.route("/profile/update/password", methods=["PUT"])
def update_password():
    """
    Обновить пароль профиля. JWT-токен отправляем в заголовке Authorization: Bearer 4f677hu98u...
    ---
    tags:
      - features/profile
    description: |
      Обновить пароль профиля. JWT-токен отправляем в заголовке Authorization: Bearer 4f677hu98u...
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
            old_password:
              type: string
              example: "password"
            new_password:
              type: string
              example: "password123"
    responses:
      200:
        description: Данные обновлены
      401:
        description: Неверный access токен, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Неверный access токен, доступ запрещен"
      404:
        description: Пользователь не найден, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Пользователь не найден, доступ запрещен"
    """
    auth_header = request.headers.get("Authorization")
    if not auth_header or not auth_header.startswith("Bearer "):
        response = make_response({"message": ResultsCodes.REFRESH_TOKEN_NEEDED}, 401)
        response.headers["WWW-Authenticate"] = "Bearer"
        return response

    token = auth_header.split(" ")[1]

    id, result = get_id(token)
    if result != ResultsCodes.OK:
        response = make_response({"message": result}, 401)
        response.headers["WWW-Authenticate"] = "Bearer"
        return response

    data = request.json

    if data["new_password"] != "" and data["new_password"] != None:
        old_password = data["old_password"]
        old_password_hash = user_repo.get_password_hash(id)
        result = check_password_with_hash(old_password_hash, old_password)
        if result == False:
            return {"message": ResultsCodes.INCORRECT_OLD_PASSWORD}, 401

        password_hash = get_password_hash(data["new_password"])
        user = user_repo.update_user(id, None, None, password_hash, None)
        if user != None:
            return {"message": ResultsCodes.DATA_UPDATED}, 200

    return {"message": ResultsCodes.USER_NOT_FOUND}, 200


@profile_bp.route("/profile/update/photo", methods=["PUT"])
def update_photo():
    """
    Обновить фото профиля. JWT-токен отправляем в заголовке Authorization: Bearer 4f677hu98u...
    ---
    tags:
      - features/profile
    description: |
      Обновить фото профиля. JWT-токен отправляем в заголовке Authorization: Bearer 4f677hu98u...
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
            photo:
              type: string
              format: byte
              description: "Фото в формате base64"
              example: "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg=="
    responses:
      200:
        description: Данные обновлены
      401:
        description: Неверный access токен, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Неверный access токен, доступ запрещен"
      404:
        description: Пользователь не найден, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Пользователь не найден, доступ запрещен"
    """
    auth_header = request.headers.get("Authorization")
    if not auth_header or not auth_header.startswith("Bearer "):
        response = make_response({"message": ResultsCodes.REFRESH_TOKEN_NEEDED}, 401)
        response.headers["WWW-Authenticate"] = "Bearer"
        return response

    token = auth_header.split(" ")[1]

    id, result = get_id(token)
    if result != ResultsCodes.OK:
        response = make_response({"message": result}, 401)
        response.headers["WWW-Authenticate"] = "Bearer"
        return response

    data = request.json
    photo = data["photo"]

    filename, result = save_photo(photo, id)
    if result != ResultsCodes.OK or filename == None:
        return {"message": result}, 409
    else:
        user = user_repo.update_user(id, None, None, None, filename)
        if user == None:
            return {"message": ResultsCodes.INVALID_BASE64}, 409

    return {}, 200
