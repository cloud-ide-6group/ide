from app.shared.extensions import db
from app.shared.dbmodels import Notification, User


class NotificationRepository:
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

    def get_all_by_user_id(self, user_id):
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
        return (
            db.session.query(Notification)
            .filter(Notification.receiver_id == user_id)
            .all()
        )


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

    def get_name(self, user_id):
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
        user = db.session.query(User).filter(User.id == user_id).first()
        if user:
            return user.name
        else:
            return None


notification_repo = NotificationRepository()
user_repo = UserRepository()
