from app.shared.extensions import db
from app.shared.dbmodels import Chat, Message


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
        chat = Chat(author_id=_author_id, project_id=_project_id)

        db.session.add(chat)

        try:
            db.session.commit()
            return chat
        except Exception as e:
            db.session.rollback()
            raise RuntimeError(f"Ошибка {e}")


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


chat_repo = ChatRepository()
message_repo = MessageRepository()
