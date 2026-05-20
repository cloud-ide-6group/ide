from app.shared.extensions import db
from app.shared.dbmodels import Chat, Message, User
from app.shared.consts import ResultsCodes


class ChatRepository:
    """
    Репозиторий для работы с файлами

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель File
    """

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


class MessageRepository:
    """
    Репозиторий для работы с файлами

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель File
    """

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
        return db.session.query(Message).filter(Message.id == int(id)).first()

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

    def get_chat_messages(self, chat_id):
        try:
            return db.session.query(Message).filter(Message.chat_id == chat_id).all()
        except Exception as e:
            raise RuntimeError(f"Getting messages error: {e}")


class UserRepository:
    """
    Репозиторий для работы с файлами

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель File
    """

    def get_name_by_id(self, id):
        """
        Получить файл по id.

        Args:
            id (int): Id файла.

        Returns:
            File: Файл.
        """
        user = db.session.query(User).filter(User.id == int(id)).first()
        if user:
            return user.name
        else:
            return ResultsCodes.UNKNOWN_USER


class ProjectRepository:
    """
    Репозиторий для работы с проектами.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель Project
    """

    def get_chats(self, project_id):
        try:
            return db.session.query(Chat).filter(Chat.project_id == project_id).all()
        except Exception as e:
            raise RuntimeError(f"Getting chats error: {e}")


chat_repo = ChatRepository()
message_repo = MessageRepository()
user_repo = UserRepository()
project_repo = ProjectRepository()
