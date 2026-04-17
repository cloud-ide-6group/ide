from app.shared.dbmodels import Project
from app.shared.extensions import db


class ProjectRepository:
    """
    Репозиторий для работы с проектами.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель Project
    """

    def create_project(self, _name, _language_id, _owner_id):
        """
        Получить пользователя по email.

        Args:
            email (str): Email пользователя.

        Returns:
            User: Пользователь

        Example:
            >>> repo = UserRepository()
            >>> user = repo.get_by_email("email@mail.ru")
        """
        try:
            project = Project(name=_name, language_id=_language_id, owner_id=_owner_id)
            db.session.add(project)
            db.session.commit()

            return project

        except Exception as e:
            db.session.rollback()
            raise e


project_repo = ProjectRepository()
