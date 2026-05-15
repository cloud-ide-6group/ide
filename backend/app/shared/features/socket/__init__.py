from app.shared.extensions import socketio
from flask_socketio import join_room
from flask import request, session
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
        - get_file_content -- запрос клиента на получение даннх
        - send_file_content -- отправка клиенту
        - join_project_room -- подключиться к проекту

    Args:
        auth (str): Токен в json
    """
    token = auth.get("token")
    if not token:
        return False

    user_id, id_result = get_id(token)
    if id_result != ResultsCodes.OK:
        return False

    session["user_id"] = user_id
    join_room(str(user_id))
    send_notifications_to_client(user_id)
    return True
