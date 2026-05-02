from app.shared.extensions import db
from app.shared.dbmodels import Notification, User


class NotificationRepository:
    """
    Репозиторий для работы с уведомлениями

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель Notifications
    """

    def get_all_by_user_id(self, user_id):
        """
        Получить все уведомления пользователя по его id.

        Args:
            user_id (int): Id пользователя.

        Returns:
            list[Notification]: Список объектов Notification (может быть пустым).
        """
        return (
            db.session.query(Notification)
            .filter(Notification.receiver_id == user_id)
            .all()
        )

    def get_by_id(self, notification_id):
        """
        Получить уведомление по его id

        Args:
            notification_id (int): Id уведомления.

        Returns:
            Notification: Уведомление
        """
        return (
            db.session.query(Notification)
            .filter(Notification.id == notification_id)
            .first()
        )

    def delete_by_id(self, notification_id):
        """
        Удаляет уведомление из БД

        Args:
            notification_id (int): Id уведомления
        """
        notification = db.session.query(Notification.id == notification_id)
        if notification:
            db.session.delete(notification)
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

    def get_name(self, user_id):
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


notification_repo = NotificationRepository()
user_repo = UserRepository()
