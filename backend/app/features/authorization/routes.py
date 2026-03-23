from app.shared.features.jwt_token.service import create_token

from . import auth_bp
from .service import *
from flask import request
import os
from dotenv import load_dotenv

load_dotenv()


@auth_bp.route("/login", methods=["POST"])
def login():
    """
    Аутентификация пользователя
    ---
    tags:
      - features/auth
    parameters:
      - name: body
        in: body
        required: true
        schema:
          type: object
          properties:
            email:
              type: email
              example: "example@examp.le"
            password:
              type: string
              example: "password"
    responses:
      200:
        description: Успешная аутентификация
        schema:
          type: object
          properties:
            name:
              type: string
              example: "username"
            photo_path:
              type: string
              example: "users_imgs/default.png"
            email:
              type: string
              example: "user@mail.ru"
            access_token:
              type: string
              example: "pbkdf2:sha256:260000$xyz..."
            refresh_token:
              type: string
              example: "pbkdf2:sha256:260000$xyz..."
      403:
        description: Неверные учетные данные, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Неверные учетные данные"
    """
    data = request.json

    email = data["email"]
    password = data["password"]

    user, error = get_user(email, password)
    if error != ResultsCodes.OK:
        return {"message": error}, 403
    if user is None:
        return {"message": ResultsCodes.USER_NOT_FOUND}, 403

    ACCESS_SECRET = os.getenv("ACCESS", "UMLFphza4e")
    REFRESH_SECRET = os.getenv("REFRESH", "iZdMl8QF0X")
    access_token = create_token(user.id, ACCESS_SECRET, timedelta(minutes=15), True)
    refresh_token = create_token(user.id, REFRESH_SECRET, timedelta(days=7), False)

    return {
        "name": user.name,
        "photo_path": user.photo_path,
        "email": user.email,
        "access_token": access_token,
        "refresh_token": refresh_token,
    }, 200


@auth_bp.route("/sign", methods=["POST"])
def sign():
    """
    Регистрация нового пользователя
    ---
    tags:
      - features/auth
    parameters:
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
              example: "username"
            password:
              type: string
              example: "password"
    responses:
      201:
        description: Пользователь создан
        schema:
          type: object
          properties:
            name:
              type: string
              example: "username"
            photo_path:
              type: string
              example: "users_imgs/default.png"
            email:
              type: string
              example: "user@mail.ru"
            access_token:
              type: string
              example: "pbkdf2:sha256:260000$xyz..."
            refresh_token:
              type: string
              example: "pbkdf2:sha256:260000$xyz..."
      400:
        description: Ошибка с отправленными данными
        schema:
          type: object
          properties:
            message:
              type: string
              example: "Неверный пароль"
    """
    data = request.json

    user, error = create_user(data["email"], data["name"], data["password"])
    if error != ResultsCodes.OK:
        return {"message": error}, 400
    if user is None:
        return {"message": ResultsCodes.USER_NOT_FOUND}, 400

    ACCESS_SECRET = os.getenv("ACCESS", "UMLFphza4e")
    REFRESH_SECRET = os.getenv("REFRESH", "iZdMl8QF0X")
    access_token = create_token(user.id, ACCESS_SECRET, timedelta(minutes=15), True)
    refresh_token = create_token(user.id, REFRESH_SECRET, timedelta(days=7), False)

    return {
        "name": user.name,
        "photo_path": user.photo_path,
        "email": user.email,
        "access_token": access_token,
        "refresh_token": refresh_token,
    }, 201