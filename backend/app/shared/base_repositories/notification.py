from app.shared.extensions import db
from app.shared.dbmodels import Notification


class BaseNotificationRepository:
    """
    Репозиторий для работы с уведомлениями.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель User
    """

    def add_notification(self, _project_id, _sender_id, _receiver_id, send_time):
        """
        Добавляет уведомление в БД, удаляя старое

        Args:
            _project_id (int): Id проекта.
            _sender_id (int): Id отправителя.
            _receiver_id (int): Id получаетеля.
        """
        notification = Notification(
            project_id=_project_id,
            sender_id=_sender_id,
            receiver_id=_receiver_id,
            send_time=send_time,
        )

        old_notification = (
            db.session.query(Notification)
            .filter(
                (Notification.project_id == _project_id)
                & (Notification.receiver_id == _receiver_id)
            )
            .all()
        )
        for old in old_notification:
            db.session.delete(old)
        db.session.commit()

        db.session.add(notification)
        db.session.commit()

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
        notification = (
            db.session.query(Notification)
            .filter(Notification.id == notification_id)
            .first()
        )
        if notification:
            db.session.delete(notification)
            db.session.commit()

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
