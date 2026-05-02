from . import notifications_bp
from flask import request, make_response
from app.shared.consts import ResultsCodes
from .service import get_notifications
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
