from app.shared.dbmodels import Language
from app.shared.extensions import db


class LanguageRepository:
    """
    Репозиторий для работы с языками.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель Language
    """

    def get_all_langs(self):
        """
        Получить все языки.

        Returns:
            Language: Языки

        Example:
            >>> repo = LanguageRepository()
            >>> user = repo.get_all_langs()
        """
        return db.session.query(Language).all()


language_repo = LanguageRepository()
