from . import notifications_bp
from flask import request, make_response
from app.shared.consts import ResultsCodes
from .service import delete_notification
from app.shared.features.jwt_token.service import (
    get_id,
    get_jwt_from_header,
    create_unauthorized_response,
)
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

    notification_id = data["notification_id"]
    result = delete_notification(id, notification_id)

    if result == ResultsCodes.OK:
        return {}, 200
    else:
        return {"message": result}, 409
