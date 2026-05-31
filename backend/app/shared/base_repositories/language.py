from app.shared.extensions import db
from app.shared.dbmodels import Language


class BaseLanguageRepository:
    """
    Репозиторий для работы с языками.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель Language
    """

    def get_by_id(self, image_id):
        if image_id == "" or image_id == None:
            return None
        return db.session.query(Language).filter(Language.id == image_id).first()

    def get_lang_by_id(self, id):
        """
        Получить язык по id

        Args:
            id (int): Id языка

        Returns:
            Language: Языки

        Example:
            >>> repo = LanguageRepository()
            >>> user = repo.get_lang_by_id(4)
        """
        return db.session.query(Language).filter(id == Language.id).first()

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
