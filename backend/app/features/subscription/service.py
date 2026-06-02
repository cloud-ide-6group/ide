from .repository import user_repo
from app.shared.consts import SUBSCRIPTION_DURATION
from datetime import datetime


def subscribe(user_id):
    """
    Добавляет подписку

    Args:
        user_id (int): Id пользователя

    Returns:
        ResultCodes: Результат выполнения операции
    """
    subscription_end = datetime.now() + SUBSCRIPTION_DURATION
    return user_repo.subscribe(user_id, subscription_end)
