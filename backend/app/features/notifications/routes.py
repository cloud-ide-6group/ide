from . import notifications_bp
from flask import request, make_response
from app.shared.consts import ResultsCodes
from .service import get_notifications
from app.shared.features.jwt_token.service import get_id
from app.shared.extensions import socketio
from flask_socketio import emit
from flask import request
from app.shared.sockets.notifications import active_notification_sockets


@socketio.on("connect_notifications")
def handle_notifications_connect():
    """Клиент подключается для получения уведомлений"""
    auth_header = request.args.get("Authorization")
    if not auth_header or not auth_header.startswith("Bearer "):
        return False

    access_token = auth_header.split(" ")[1]
    user_id, id_result = get_id(access_token)
    if id_result != ResultsCodes.OK:
        return False

    active_notification_sockets[user_id] = request.sid

    notifications = get_notifications(user_id)
    emit("notifications_list", {"notifications": notifications})
    return True


@socketio.on("disconnect_notifications")
def handle_notifications_disconnect():
    """Клиент отключился"""
    for user_id, sid in list(active_notification_sockets.items()):
        if sid == request.sid:
            del active_notification_sockets[user_id]
            break
