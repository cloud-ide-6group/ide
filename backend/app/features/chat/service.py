from .repository import chat_repo, message_repo
from app.shared.consts import ResultsCodes


def create_chat(project_id, author_id):
    try:
        chat = chat_repo.add_chat(project_id, author_id)
        return ResultsCodes.OK
    except Exception as e:
        return ResultsCodes.CREATE_ERROR
