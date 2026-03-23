from . import auth_bp
from .service import *
from flask import request, jsonify
import os
from dotenv import load_dotenv

load_dotenv()


@auth_bp.route("/login", methods=["POST"])
def login():
    """
    Аутентификация пользователя
    ---
    tags:
      - Auth
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
              error:
                type: string
                example: "Неверные учетные данные"
    """
    data = request.json

    email = data["email"]
    password = data["password"]

    user, error = get_user(email, password)
    if error != ResultsCodes.OK:
        return {"error": error}, 403
    if user is None:
        return {"error": ResultsCodes.USER_NOT_FOUND}, 403

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
      - Auth
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
            error:
              type: string
              example: "Неверный пароль"
    """
    data = request.json

    user, error = create_user(data["email"], data["name"], data["password"])
    if error != ResultsCodes.OK:
        return {"error": error}, 400
    if user is None:
        return {"error": ResultsCodes.USER_NOT_FOUND}, 400

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


@auth_bp.route("/refresh", methods=["POST"])
def refresh():
    """
    Обновление access-токена с помощью refresh-токена
    ---
    tags:
      - Auth
    parameters:
      - name: body
        in: body
        required: true
        schema:
          type: object
          required:
            - refresh_token
          properties:
            refresh_token:
              type: string
              example: "eyJhbGciOiJIUzI1NiIs..."
    responses:
      200:
        description: Новый access-токен и refresh-токен
        schema:
          type: object
          properties:
            access_token:
              type: string
              example: "eyJhbGciOiJIUzI1NiIs..."
            refresh_token:
              type: string
              example: "eyJhbGciOiJIUzI1NiIs..."
      401:
        description: Недействительный refresh-токен
        schema:
          type: object
          properties:
              error:
                type: string
                example: "Неверный refresh токен"
    """
    data = request.json
    refresh_token = data["refresh_token"]

    if not refresh_token:
        return {"error": ResultsCodes.REFRESH_TOKEN_NEEDED}, 401

    ACCESS_SECRET = os.getenv("ACCESS", "UMLFphza4e")
    REFRESH_SECRET = os.getenv("REFRESH", "iZdMl8QF0X")

    try:
        result = get_access_refresh_tokens(refresh_token, REFRESH_SECRET, ACCESS_SECRET)
        if result["result"] == ResultsCodes.OK:
            return {"access_token": result["access"], "refresh_token": result["refresh"]}, 200
        else:
            return {"error": result["result"]}, 401

    except jwt.ExpiredSignatureError:
        return {"error": ResultsCodes.REFRESH_TOKEN_EXPIRED}, 401
    except jwt.InvalidTokenError as e:
        print(f"InvalidTokenError: {e}")
        print(f"Тип ошибки: {type(e).__name__}")
        return {"error": ResultsCodes.REFRESH_TOKEN_NEEDED}, 401
