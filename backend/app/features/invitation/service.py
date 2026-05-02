from .repository import user_repo, project_repo, notification_repo
from app.shared.consts import ResultsCodes


def add_user_in_project(project_name, invited_user_email, owner_id):
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
    project = project_repo.get_by_name(project_name)

    if project and project.owner_id == owner_id:
        added_user = user_repo.get_by_email(invited_user_email)
        if added_user and user_repo.user_exists(owner_id):
            project_repo.add_user_in_project(project.id, added_user.id)
            notification_repo.add_notification(project.id, owner_id, added_user)
            return ResultsCodes.OK
        else:
            return ResultsCodes.USER_NOT_FOUND
    else:
        return ResultsCodes.PROJECT_NOT_FOUND


def delete_user_from_project(project_name, invited_user_email, owner_id):
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
    project = project_repo.get_by_name(project_name)

    if project and project.owner_id == owner_id:
        deleted_user = user_repo.get_by_email(invited_user_email)
        if deleted_user and user_repo.user_exists(owner_id):
            project_repo.delete_user_from_project(project.id, deleted_user.id)
            return ResultsCodes.OK
        else:
            return ResultsCodes.USER_NOT_FOUND
    else:
        return ResultsCodes.PROJECT_NOT_FOUND
