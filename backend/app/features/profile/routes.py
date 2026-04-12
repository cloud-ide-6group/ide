from . import profile_bp
import os
from flask import request
from dotenv import load_dotenv
from .service import *
from ...shared.features.jwt_token.service import get_id

load_dotenv()


@profile_bp.route("/profile", methods=["GET"])
def profile():
    """
    Получить профиль пользователя. JWT-токен отправляем в заголовке Authorization: Bearer 4f677hu98u...
    ---
    tags:
      - features/profile
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
        return {"message": "Токен не предоставлен"}, 403

    token = auth_header.split(" ")[1]

    id, result = get_id(token)
    if result != ResultsCodes.OK:
        return {"message": result}, 401

    user, result = get_user_data(id)
    if user == None:
        return {"message": ResultsCodes.USER_NOT_FOUND}, 404
    if result != ResultsCodes.OK:
        return {"message": result}, 404

    photo = get_photo_base_64(user.photo_path)

    return {"name": user.name, "photo": photo, "email": user.email}, 200
