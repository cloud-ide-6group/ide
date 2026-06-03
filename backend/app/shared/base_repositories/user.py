from app.shared.extensions import db
from app.shared.dbmodels import User


class BaseUserRepository:
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

    def get_name_by_id(self, user_id):
        """
        Получить имя пользователя по id.

        Args:
            user_id (int): Id пользователя.

        Returns:
            str: Имя. Может быть None
        """
        user = db.session.query(User).filter(User.id == user_id).first()
        if user:
            return user.name
        else:
            return None

    def get_by_email(self, email):
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
        return db.session.query(User).filter(User.email == email).first()

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

    def get_password_hash(self, id):
        """
        Получает хэш пароля из таблицы

        Args:
            id (int): Id пользователя

        Returns:
            str: Хэш пароля
        """
        return db.session.query(User).filter(User.id == id).first().password_hash

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


user_repo = BaseUserRepository()
