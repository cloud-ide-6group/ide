from datetime import timedelta

from app.shared.features.jwt_token.service import create_token

from . import auth_bp
from .service import *
from flask import request


@auth_bp.route("/login", methods=["POST"])
def login():
    """
    Аутентификация пользователя
    ---
    tags:
      - features/auth
    requestBody:
      required: true
      content:
        application/json:
          schema:
            type: object
            properties:
              email:
                type: string
                format: email
                example: "example@examp.le"
              password:
                type: string
                example: "password"
    responses:
      200:
        description: Успешная аутентификация
        content:
          application/json:
            schema:
              type: object
              properties:
                access_token:
                  type: string
                  example: "pbkdf2:sha256:260000$xyz..."
                refresh_token:
                  type: string
                  example: "pbkdf2:sha256:260000$xyz..."
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
    """
    data = request.json

    email = data["email"]
    password = data["password"]

    user, error = get_user(email, password)
    if error != ResultsCodes.OK:
        return {"message": error}, 403
    if user is None:
        return {"message": ResultsCodes.USER_NOT_FOUND}, 403

    access_token = create_token(user.id, timedelta(minutes=15), True)
    refresh_token = create_token(user.id, timedelta(days=7), False)

    return {
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
    requestBody:
      required: true
      content:
        application/json:
          schema:
            type: object
            properties:
              email:
                type: string
                format: email
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
        content:
          application/json:
            schema:
              type: object
              properties:
                access_token:
                  type: string
                  example: "pbkdf2:sha256:260000$xyz..."
                refresh_token:
                  type: string
                  example: "pbkdf2:sha256:260000$xyz..."
      400:
        description: Ошибка с отправленными данными
        content:
          application/json:
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

    access_token = create_token(user.id, timedelta(minutes=15), True)
    refresh_token = create_token(user.id, timedelta(days=7), False)

    return {
        "access_token": access_token,
        "refresh_token": refresh_token,
    }, 201
