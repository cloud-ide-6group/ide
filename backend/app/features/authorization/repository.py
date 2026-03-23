from app.shared.extensions import db
from app.shared.dbmodels import User


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

    def get_all(self):
        """
        Получить всех пользователей.

        Returns:
            User: Пользователь

        Example:
            >>> repo = UserRepository()
            >>> users = repo.get_all()
        """
        return db.session.query(User).all()

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

    def insert_user(self, email, password_hash, name):
        """
        Создать пользователя.

        Args:
            email (str): Email пользователя.
            password_hash (str): Password hash.
            name (str): Имя пользователя.

        Returns:
            User: Пользователь.

        Example:
            >>> repo = UserRepository()
            >>> user = repo.insert_user("email@mail.ru", "hash", "username")
        """
        try:
            user = User(
                name=name,
                email=email,
                password_hash=password_hash,
            )
            db.session.add(user)
            db.session.commit()

            return user

        except Exception as e:
            db.session.rollback()
            raise e

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


user_repo = UserRepository()
