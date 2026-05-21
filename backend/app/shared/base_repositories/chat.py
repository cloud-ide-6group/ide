from app.shared.extensions import db
from app.shared.dbmodels import Chat


class BaseChatRepository:
    """
    Репозиторий для работы с файлами

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель File
    """

    def get_by_project_id(self, project_id):
        return db.session.query(Chat).filter(Chat.project_id == project_id).all()
