from .repository import chat_repo, message_repo
from app.shared.consts import ResultsCodes


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
