from .repository import chat_repo, message_repo, user_repo, project_repo
from app.shared.consts import ResultsCodes
from datetime import datetime


def create_chat(project_id, author_id, identificator):
    """
    Создать чат

    Args:
        project_id (int): Id проекта
        author_id (int): Id пользователя
        identificator (str): Идентификатор чата

    Returns:
        Chat: Созданный чат
        ResultCodes: Результат выполнения операции
    """
    chat = chat_repo.get_by_identificator(identificator, project_id)
    if chat:
        return None, ResultsCodes.CHAT_ALREADY_EXISTS

    if project_repo.is_user_in_project(author_id, project_id) == False:
        return None, ResultsCodes.USER_IS_NOT_IN_PROJECT

    try:
        chat = chat_repo.add_chat(project_id, author_id, identificator)
        return chat, ResultsCodes.OK
    except Exception as e:
        print(e)
        return None, ResultsCodes.CREATE_ERROR


def delete_chat(chat_id, user_id):
    """
    Удалить чат

    Args:
        chat_id (int): Id чата
        user_id (int): Id пользователя

    Returns:
        int: Id проекта, в котором был чат для оповещения
        ResultCodes: Результат выполнения операции
    """
    chat = chat_repo.get_by_id(chat_id)

    if chat is None:
        return None, ResultsCodes.CHAT_NOT_FOUND

    project_id = chat.project_id

    if chat.author_id != user_id:
        return None, ResultsCodes.USER_IS_NOT_CHAT_CREATOR

    try:
        chat_repo.delete_chat(chat_id)
        return project_id, ResultsCodes.OK
    except Exception as e:
        return None, ResultsCodes.DELETE_ERROR


def send_message(chat_id, message_text, author_id):
    """
    Отправить сообщение в чат

    Args:
        chat_id (int): Id чата
        message_text (str): Текст сообщения
        author_id (int): Id автора

    Returns:
        ResultCodes: Результат выполнения операции
    """
    chat = chat_repo.get_by_id(chat_id)
    if chat is None:
        return ResultsCodes.CHAT_NOT_FOUND

    if project_repo.is_user_in_project(author_id, chat.project_id) == False:
        return ResultsCodes.USER_IS_NOT_IN_PROJECT

    try:
        message_repo.create_message(chat_id, message_text, author_id, datetime.now())
        return ResultsCodes.OK
    except Exception as e:
        print(e)
        return ResultsCodes.CREATE_ERROR


def get_messages(chat_id):
    """
    Получить все сообщения чата

    Args:
        chat_id (int): Id чата

    Returns:
        list[dict]: Список сообщений
            - id (int): Id
            - text(str): Текст
            - author(str): Имя автора
            - send_time (str): Время сообщения
        ResultCodes: Результат выполнения операции
    """
    try:
        messages_raw = message_repo.get_chat_messages(chat_id)
        messages = []
        for m in messages_raw:
            messages.append(
                {
                    "id": m.id,
                    "text": m.text,
                    "author": user_repo.get_name_by_id(m.author_id),
                    "send_time": m.send_time.strftime("%Y-%m-%d %H:%M:%S"),
                }
            )
        return messages, ResultsCodes.OK
    except Exception as e:
        print(e)
        return None, ResultsCodes.CHAT_NOT_FOUND


def get_chat_project_id(chat_id):
    """
    Получить id проекта в котором чат

    Args:
        chat_id (int): Id чата

    Returns:
        int: Id проекта
        ResultCodes: Результат выполнения операции
    """
    try:
        return chat_repo.get_by_id(chat_id).project_id, ResultsCodes.OK
    except AttributeError as e:
        print(f"Chat is None: {e}")
        return None, ResultsCodes.CHAT_NOT_FOUND
    except Exception as e:
        print(e)
        return None, ResultsCodes.UNEXPECTED_ERROR


def get_chats(project_id):
    """
    Получить все чаты проекта

    Args:
        project_id (int): Id проекта

    Returns:
        list[Chat]: Список чатов
        ResultCodes: Результат выполнения операции
    """
    try:
        return project_repo.get_chats(project_id), ResultsCodes.OK
    except Exception as e:
        print(e)
        return [], ResultsCodes.CHAT_NOT_FOUND
