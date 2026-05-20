from .repository import chat_repo, message_repo, user_repo
from app.shared.consts import ResultsCodes
from datetime import datetime


def create_chat(project_id, author_id):
    try:
        chat = chat_repo.add_chat(project_id, author_id)
        return ResultsCodes.OK
    except Exception as e:
        return ResultsCodes.CREATE_ERROR


def delete_chat(chat_id, id):
    chat = chat_repo.get_by_id(chat_id)

    if chat is None:
        return ResultsCodes.CHAT_NOT_FOUND

    if chat.author_id == id:
        return ResultsCodes.USER_IS_NOT_CHAT_CREATOR

    try:
        chat_repo.delete_chat(chat_id)
        return ResultsCodes.OK
    except Exception as e:
        return ResultsCodes.DELETE_ERROR


def send_message(chat_id, message_text, author_id):
    try:
        message_repo.create_message(chat_id, message_text, author_id, datetime.now())
        return ResultsCodes.OK
    except Exception as e:
        print(e)
        return ResultsCodes.CREATE_ERROR


def get_messages(chat_id):
    try:
        messages_raw = message_repo.get_chat_messages(chat_id), ResultsCodes.OK
        messages = []
        for m in messages_raw:
            messages.append(
                {
                    "id": m.id,
                    "text": m.text,
                    "author": user_repo.get_name_by_id(m.author_id),
                    "send_time": m.send_time,
                }
            )
        return messages, ResultsCodes.OK
    except Exception as e:
        print(e)
        return None, ResultsCodes.CHAT_NOT_FOUND


def get_chat_project_id(chat_id):
    try:
        return chat_repo.get_by_id(chat_id).project_id, ResultsCodes.OK
    except AttributeError as e:
        print(f"Chat is None: {e}")
        return None, ResultsCodes.CHAT_NOT_FOUND
    except Exception as e:
        print(e)
        return None, ResultsCodes.UNEXPECTED_ERROR
