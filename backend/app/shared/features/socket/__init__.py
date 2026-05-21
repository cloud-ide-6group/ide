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

    Возможные события ОТ КЛИЕНТА СЕРВЕРУ:
        - update_file_content -- послать новые данные в файл
        - get_file_content -- запрос клиента на получение данных
        - join_project_room -- подключиться к проекту
        - run_code -- запустить программу
        - send_input -- отправить ввод в программу
        - stop_code -- остановить выполнение программы

    Возможные события ОТ СЕРВЕРА КЛИЕНТУ, ПОДПИСЫВАЕМСЯ НА НИХ:
        - send_file_content -- посылает всем клиентам обновленное содержимое файла
            >>> {"content": "content example"}
        - notifications_list -- уведомления
            >>> {"notifications": [
            >>>     {
            >>>         "sender_name": "username",
            >>>         "send_time": "2026-05-21T15:30:45.123456",
            >>>         "notification_id": 80,
            >>>         "project_id": 79,
            >>>         "project_name": "TestProject"
            >>>     }
            >>> ]}
        - files_trees_list -- массив деревьев файлов проекта
            >>> {"files_trees_list": get_project_files_trees(project_id)
            >>>     [
            >>>         {
            >>>             "id": 80,
            >>>             "name": "NewFile.txt",
            >>>             "is_folder": true,
            >>>             "children": [ /рекурсивно дети с такой же структурой/ ]
            >>>         }
            >>>     ]
            >>> }
        - console_output -- вывод в консоль
            >>> {"data": "output"}
        - get_messages -- получить сообщения чата
            >>> {"chat_id": 80,
            >>>     "messages":
            >>>         "id": 80,
            >>>         "text": "message",
            >>>         "author": "username",
            >>>         "send_time": "2026-05-21 15:30:45"
            >>> }
        - get_chats -- получить чаты с сообщениями
            >>> {"chats_list": 
            >>>     "id": 80,
            >>>     "messages":
            >>>         [
            >>>             "id": 80,
            >>>             "text": "message",
            >>>             "author": "username",
            >>>             "send_time": "2026-05-21 15:30:45"
            >>>         ]
            >>> }

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
    return True
