from app.shared.extensions import db
from app.shared.dbmodels import Project, UserInProject, Language
from app.shared.consts import ResultsCodes


class ProjectRepository:
    """
    Репозиторий для работы с проектом.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель User

    Example:
        >>> repo = UserRepository()
        >>> user = repo.get_by_id(1)
        >>> print(user.name)
        'username1'
    """

    def is_user_in_project(self, user_id, project_id):
        """
        Получить пользователя по id.

        Args:
            user_id (int): Id пользователя.

        Returns:
            User: Пользователь

        Example:
            >>> repo = UserRepository()
            >>> user = repo.get_by_id(123)
        """
        project = db.session.query(Project).filter(Project.id == project_id).first()
        if not project:
            return False, ResultsCodes.PROJECT_NOT_FOUND
        if project.owner_id == user_id:
            return True, ResultsCodes.OK
        userInProject = (
            db.session.query(UserInProject)
            .filter(
                UserInProject.user_id == user_id, UserInProject.project_id == project_id
            )
            .first()
        )
        if userInProject:
            return True, ResultsCodes.OK

        return False, ResultsCodes.CANT_CHANGE_FILE

    def get_by_id(self, project_id):
        if project_id == "" or project_id == None:
            return None
        return db.session.query(Project).filter(Project.id == project_id).first()


class LanguageRepository:
    """
    Репозиторий для работы с проектом.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель User

    Example:
        >>> repo = UserRepository()
        >>> user = repo.get_by_id(1)
        >>> print(user.name)
        'username1'
    """

    def get_by_id(self, image_id):
        if image_id == "" or image_id == None:
            return None
        return db.session.query(Language).filter(Language.id == image_id).first()


project_repo = ProjectRepository()
language_repo = LanguageRepository()
