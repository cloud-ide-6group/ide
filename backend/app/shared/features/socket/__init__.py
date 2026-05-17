from app.shared.extensions import socketio
from flask_socketio import join_room
from flask import request, session
from app.shared.consts import ResultsCodes
from app.shared.features.jwt_token.service import get_id
from app.shared.features.notifications.service import send_notifications_to_client


# TODO: НЕ ЗАБЫТЬ ЗАМЕНИТЬ НА files_trees_list
@socketio.on("connect")
def connect(auth):
    """
    Подключение пользователя по сокету и добавление в комнату по id.

    Возможные события ОТ КЛИЕНТА СЕРВЕРУ:
        - update_file_content -- послать новые данные в файл
        - get_file_content -- запрос клиента на получение данных
        - join_project_room -- подключиться к проекту

    Возможные события ОТ СЕРВЕРА КЛИЕНТУ, ПОДПИСЫВАЕМСЯ НА НИХ:
        - send_file_content -- посылает всем клиентам обновленное содержимое файла
        - notifications_list -- уведомления
        - files_list -- массив деревьев файлов проекта

    Args:
        auth (str): Токен в json БЕЗ BEARER

    Example:
        >>> {
        >>>     "auth": {
        >>>         "token": "eykjkl..."
        >>>     }
        >>> }
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
    print("connected")
    return True
