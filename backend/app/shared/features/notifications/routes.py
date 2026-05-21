from . import notifications_bp
from flask import request, make_response
from app.shared.consts import ResultsCodes
from .service import delete_notification
from app.shared.features.jwt_token.service import get_id
from flask import request


@notifications_bp.route("/notification/delete", methods=["DELETE"])
def delete_notification_rout():
    """
    Удаление уведомления после прочтения пользователем
    ---
    tags:
      - shared/features/notifications
    description: |
      Удаляет уведомление
    parameters:
      - name: Authorization
        in: header
        required: true
        schema:
          type: string
        example: "Bearer pbkdf2:sha256:260000$xyz..."
    requestBody:
      required: true
      content:
        application/json:
          schema:
            type: object
            properties:
              notification_id:
                type: integer
                example: 7
    responses:
      200:
        description: Успешное удаление
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
        description: Ошибка удаления
        content:
          application/json:
            schema:
              type: object
              properties:
                message:
                  type: string
                  example: "Пользователь не существует"
    """
    auth_header = request.headers.get("Authorization")
    if not auth_header or not auth_header.startswith("Bearer "):
        response = make_response({"message": "Токен не предоставлен"}, 401)
        response.headers["WWW-Authenticate"] = "Bearer"
        return response

    data = request.json
    access_token = auth_header.split(" ")[1]
    user_id, id_result = get_id(access_token)
    if id_result != ResultsCodes.OK:
        return {"message": id_result}, 401

    notification_id = data["notification_id"]
    result = delete_notification(user_id, notification_id)

    if result == ResultsCodes.OK:
        return {}, 200
    else:
        return {"message": result}, 409
