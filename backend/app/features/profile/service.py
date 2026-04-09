from .repository import user_repo
from ...shared.consts import ResultsCodes


def get_user_data(id):
    """
    Получает данные пользователя по id

    Args:
        id (int): id пользователя

    Returns:
        User: Пользователь
        ResultCodes: Результат

    Example:
        >>> user, result = get_user_data(7)
    """
    if id == None:
        return None, ResultsCodes.USER_ID_NULL

    user = user_repo.get_by_id(id)
    if user == None:
        return user, ResultsCodes.USER_NOT_FOUND

    return user, ResultsCodes.OK
