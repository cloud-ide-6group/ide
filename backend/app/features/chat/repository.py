from app.shared.base_repositories import (
    BaseMessageRepository,
    BaseUserRepository,
    BaseChatRepository,
    BaseProjectRepository,
)
from app.shared.dbmodels import Chat, Message
from app.shared.extensions import db


class ChatRepository(BaseChatRepository):
    def get_by_id(self, id):
        """
        Получить файл по id.

        Args:
            id (int): Id файла.

        Returns:
            File: Файл.
        """
        if id == "" or id is None:
            return None
        return db.session.query(Chat).filter(Chat.id == int(id)).first()

    def add_chat(self, _project_id, _author_id):
        try:
            chat = Chat(author_id=_author_id, project_id=_project_id)
            db.session.add(chat)
            db.session.commit()
            return chat
        except Exception as e:
            db.session.rollback()
            raise RuntimeError(f"Ошибка {e}")

    def delete_chat(self, chat_id):
        chat = db.session.query(Chat).filter(Chat.id == chat_id).first()

        db.session.delete(chat)

        try:
            db.session.commit()
        except Exception as e:
            db.session.rollback()
            raise RuntimeError(f"Deletion error: {e}")


class MessageRepository(BaseMessageRepository):
    def create_message(self, _chat_id, message_text, _author_id, _send_time):
        try:
            message = Message(
                text=message_text,
                author_id=_author_id,
                chat_id=_chat_id,
                send_time=_send_time,
            )
            db.session.add(message)
            db.session.commit()
        except Exception as e:
            db.session.rollback()
            raise RuntimeError(f"Message creation error: {e}")


class UserRepository(BaseUserRepository):
    pass


class ProjectRepository(BaseProjectRepository):
    pass


chat_repo = ChatRepository()
message_repo = MessageRepository()
user_repo = UserRepository()
project_repo = ProjectRepository()
