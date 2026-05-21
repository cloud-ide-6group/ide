from app.shared.extensions import db
from app.shared.dbmodels import Message


class BaseMessageRepository:
    """
    Репозиторий для работы с файлами

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель File
    """

    def get_chat_messages(self, chat_id):
        try:
            return (
                db.session.query(Message)
                .filter(Message.chat_id == chat_id)
                .order_by(Message.send_time.desc())
                .all()
            )
        except Exception as e:
            raise RuntimeError(f"Getting messages error: {e}")
