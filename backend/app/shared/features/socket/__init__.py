from app.shared.extensions import socketio
from flask_socketio import join_room
from flask import request
from app.shared.consts import ResultsCodes
from app.shared.features.jwt_token.service import get_id
from app.shared.features.notifications.service import send_notifications_to_client


@socketio.on("connect")
def connect(auth):
    """
    Подключение пользователя по сокету и добавление в комнату по id.

    Возможные события:
        - notifications_list -- уведомления
        - files_list -- список файлов проекта
        - get_file_content -- получить данные файла
        - update_file_content -- послать новые данные в файл
        - join_project_room -- подключиться к проекту

    Args:
        auth (str): Authorization Bearer header
    """
    auth_header = request.headers.get("Authorization")
    if not auth_header or not auth_header.startswith("Bearer "):
        return False
    
    access_token = auth_header.split(" ")[1]
    user_id, id_result = get_id(access_token)
    if id_result != ResultsCodes.OK:
        return False

    join_room(str(user_id))

    send_notifications_to_client(user_id)
    return True
