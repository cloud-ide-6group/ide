from .repository import notification_repo, user_repo
from app.shared.consts import ResultsCodes


def get_notifications(user_id):
    """
    Создает обновленный объект user и отправляет его в репозиторий, чтобы сохранить новые данные

    Args:
        id (int): Id пользователя
        email (str): Почта пользователя
        name (str): Имя пользователя
        password_hash (str): Хэш пароля
        photo_path (str): Путь к фото профиля на сервере

    Returns:
        User: Обновленный пользователь
        ResultCodes: Результат выполнения
    """
    raw_notifications = notification_repo.get_all_by_user_id(user_id)
    notifications = []
    for n in raw_notifications:
        notifications.append(
            {
                "sender_name": user_repo.get_name(n.sender_id) or "-",
                "send_time": n.send_time,
                "notification_id": n.id,
            }
        )

    return notifications
