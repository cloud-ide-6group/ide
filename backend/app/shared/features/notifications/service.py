from .repository import notification_repo, user_repo
from app.shared.consts import ResultsCodes
from app.shared.extensions import socketio


def get_notifications(user_id):
    """
    Получает уведомления из БД и преобразует их в json.

    Args:
        user_id (int): Id пользователя

    Returns:
        list[dict]: Список уведомлений, каждое в виде словаря:
            [
                {
                    "sender_name": str,
                    "send_time": datetime,
                    "notification_id": int
                }
            ]
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


def send_to_klient(invited_user_id):
    """
    Посылает клиенту все уведомления по сокету.

    Args:
        invited_user_id (int): Id пользователя
    """
    socketio.emit(
        "notifications_list",
        {"notifications": get_notifications(invited_user_id)},
        room=str(invited_user_id),
    )


def delete_notification(user_id, notification_id):
    notification = notification_repo.get_by_id(notification_id)
    if user_id == notification.receiver_id:
        notification_repo.delete_by_id(notification.id)
        return ResultsCodes.OK
    return ResultsCodes.USER_NOT_FOUND
