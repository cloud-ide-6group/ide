from . import notifications_bp
from flask import request, make_response
from app.shared.consts import ResultsCodes
from .service import get_notifications, delete_notification
from app.shared.features.jwt_token.service import get_id
from app.shared.extensions import socketio
from flask_socketio import emit, join_room
from flask import request


@socketio.on("connect_notifications")
def connect_notifications():
    """
    Подключает клиента к системе уведомлений и отправляет все уведомления из БД.

    Args:
        Authorization (str): Bearer токен. Передается как query параметр.

    Emits:
        notifications_list: Отправляет клиенту при успешном подключении.
            Структура: {
                "notifications": [
                    {
                        "sender_name": "userName",
                        "send_time": "2026-05-03 15:30:45.123456",
                        "notification_id": 10,
                    }
                ]
            }
    """
    auth_header = request.args.get("Authorization")
    if not auth_header or not auth_header.startswith("Bearer "):
        return False

    access_token = auth_header.split(" ")[1]
    user_id, id_result = get_id(access_token)
    if id_result != ResultsCodes.OK:
        return False

    join_room(str(user_id))

    notifications = get_notifications(user_id)
    emit("notifications_list", {"notifications": notifications})
    return True


@notifications_bp.route("/delete/notification", methods=["DELETE"])
def delete_notification():
    auth_header = request.headers.get("Authorization")
    if not auth_header or not auth_header.startswith("Bearer "):
        response = make_response({"message": "Токен не предоставлен"}, 401)
        response.headers["WWW-Authenticate"] = "Bearer"
        return response

    data = request.json
    access_token = auth_header.split(" ")[1]
    user_id, id_result = get_id(access_token)
    if id_result != ResultsCodes.OK:
        return {"message": id_result}, 403

    notification_id = data["notification_id"]
    result = delete_notification(user_id, notification_id)

    if result == ResultsCodes.OK:
        return {}, 200
    else:
        return {"message": result}, 409
