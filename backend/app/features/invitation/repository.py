from app.shared.extensions import db
from app.shared.dbmodels import Project, UserInProject, User


class ProjectRepository:
    """
    Репозиторий для работы с пользователями.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель User

    Example:
        >>> repo = UserRepository()
        >>> user = repo.get_by_id(1)
        >>> print(user.name)
        'username1'
    """

    def get_by_name(self, name):
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
        return db.session.query(Project).filter(Project.name == name).first()

    def add_user_in_project(self, _project_id, _user_id):
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
        userInProject = UserInProject(project_id=_project_id, user_id=_user_id)
        db.session.add(userInProject)
        db.session.commit()

    def delete_user_from_project(self, _project_id, _user_id):
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
        user_in_project = (
            db.session.query(UserInProject)
            .filter(
                (UserInProject.project_id == _project_id)
                & (UserInProject.user_id == _user_id)
            )
            .first()
        )
        if user_in_project:
            db.session.delete(user_in_project)
            db.session.commit()


class UserRepository:
    """
    Репозиторий для работы с пользователями.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель User

    Example:
        >>> repo = UserRepository()
        >>> user = repo.get_by_id(1)
        >>> print(user.name)
        'username1'
    """

    def user_exists(self, id):
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
        user = db.session.query(Project).filter(Project.id == id).first()
        if user == None:
            return False
        else:
            return True

    def get_by_email(self, email):
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
        return db.session.query(User).filter(User.email == email).first()


project_repo = ProjectRepository()
user_repo = UserRepository()
