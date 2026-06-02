from . import subscription_bp
from flask import request
from dotenv import load_dotenv
from ...shared.features.jwt_token.service import (
    get_id,
    get_jwt_from_header,
    create_unauthorized_response,
)
from app.shared.features.password_hash.service import get_password_hash
from app.shared.consts import ResultsCodes
from .service import subscribe

load_dotenv()


@subscription_bp.route("/subscribe", methods=["GET"])
def subscribe_route():
    """
    Получить профиль пользователя. JWT-токен отправляем в заголовке Authorization: Bearer 4f677hu98u...
    ---
    tags:
      - features/subscription
    description: |
      Оформить подписку
    security:
      - BearerAuth: []
    responses:
      200:
        description: Успешно оформили
      401:
        description: Неверный access токен, доступ запрещен
        content:
          application/json:
            schema:
              type: object
              properties:
                message:
                  type: string
                  example: "Неверный access токен, доступ запрещен"
      409:
        description: Внутренняя ошибка сервера
        content:
          application/json:
            schema:
              type: object
              properties:
                message:
                  type: string
                  example: "Пользователь не найден, доступ запрещен"
    """
    auth_header = request.headers.get("Authorization")
    token, result = get_jwt_from_header(auth_header)

    if result != ResultsCodes.OK:
        response = create_unauthorized_response(result)
        return response

    id, id_result = get_id(token)
    if id_result != ResultsCodes.OK:
        return {"message": id_result}, 401

    result = subscribe(id)

    if result == ResultsCodes.OK:
        return {}, 200
    else:
        return {"message": result}, 409
