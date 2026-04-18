import jwt

from . import jwt_token_bp
from .service import *
import os
from ...consts import ResultsCodes
from flask import request, jsonify, make_response
from dotenv import load_dotenv

load_dotenv()


@jwt_token_bp.route("/refresh", methods=["POST"])
def refresh():
    """
    Обновление access-токена с помощью refresh-токена
    ---
    tags:
      - shared/features/jwt_token
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
              message:
                type: string
                example: "Неверный refresh токен"
    """
    data = request.json
    refresh_token = data["refresh_token"]

    if not refresh_token:
        return {"message": ResultsCodes.REFRESH_TOKEN_NEEDED}, 401

    ACCESS_SECRET = os.getenv("ACCESS", "UMLFphza4e")
    REFRESH_SECRET = os.getenv("REFRESH", "iZdMl8QF0X")

    try:
        result = get_access_refresh_tokens(refresh_token, REFRESH_SECRET, ACCESS_SECRET)
        if result["result"] == ResultsCodes.OK:
            return {
                "access_token": result["access"],
                "refresh_token": result["refresh"],
            }, 200
        else:
            return {"message": result["result"]}, 401

    except jwt.ExpiredSignaturemessage:
        return {"message": ResultsCodes.REFRESH_TOKEN_EXPIRED}, 401
    except jwt.InvalidTokenError as e:
        print(f"InvalidTokenmessage: {e}")
        print(f"Тип ошибки: {type(e).__name__}")
        return {"message": ResultsCodes.REFRESH_TOKEN_NEEDED}, 401
