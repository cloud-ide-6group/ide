from app.shared.extensions import db
from app.shared.dbmodels import User, Project


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

    def get_by_id(self, user_id):
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
        return db.session.get(User, user_id)

    def update_user(self, id, email, name, password_hash, photo_path):
        old_user = db.session.get(User, id)

        if old_user == None:
            return None

        if email != "" and email != None:
            old_user.email = email
        if name != "" and name != None:
            old_user.name = name

        if password_hash != "" and password_hash != None:
            old_user.password_hash = password_hash

        if photo_path != "" and photo_path != None:
            old_user.photo_path = photo_path

        db.session.commit()
        return old_user

    def get_password_hash(self, id):
        return db.session.query(User).filter(User.id == id).first().password_hash

    def delete_user_by_id(self, id):
        """
        Удалить пользователя.

        Args:
            id (int): Id пользователя.

        Example:
            >>> repo = UserRepository()
            >>> repo.delete_user(7)
        """
        user = db.session.query(User).filter(User.id == id).first()

        if user:
            db.session.delete(user)
            db.session.commit()


class ProjectRepository:
    """
    Репозиторий для работы с проектами.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель Project
    """

    def get_by_owner_id(self, owner_id):
        """
        Получить все проекты пользователя по его id.

        Args:
            owner_id (int): Id пользователя.

        Returns:
            Project: Проект

        Example:
            >>> repo = ProjectRepository()
            >>> list_of_projects = repo.get_by_owner_id(123)
        """
        return db.session.query(Project).filter(Project.owner_id == owner_id).all()


user_repo = UserRepository()
project_repo = ProjectRepository()
