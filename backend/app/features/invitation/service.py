from .repository import user_repo, project_repo, notification_repo
from app.shared.consts import ResultsCodes
from app.shared.features.notifications.service import send_notifications_to_client


def add_user_in_project(project_name, invited_user_email, owner_id):
    """
    Добавляет пользователя в проект.

    Args:
        project_name (str): Имя проекта
        invited_user_email (str): Почта приглашаемого пользователя
        owner_id (int): Id автора-владельца проекта

    Returns:
        ResultCodes: Результат выполнения
    """
    project = project_repo.get_by_name(project_name)

    if project and project.owner_id == owner_id:
        added_user = user_repo.get_by_email(invited_user_email)
        if not added_user:
            return ResultsCodes.USER_NOT_FOUND
        if added_user.id == owner_id:
            return ResultsCodes.CANT_INVITE
        if added_user and user_repo.user_exists(owner_id):
            if project_repo.is_user_in_project(project.id, added_user.id):
                return ResultsCodes.USER_IS_IN_ALREADY
            project_repo.add_user_in_project(project.id, added_user.id)
            notification_repo.add_notification(project.id, owner_id, added_user.id)
            send_notifications_to_client(added_user.id)
            return ResultsCodes.OK
        else:
            return ResultsCodes.USER_NOT_FOUND
    else:
        return ResultsCodes.PROJECT_NOT_FOUND


def delete_user_from_project(project_name, invited_user_email, owner_id):
    """
    Удаляет пользователя из проекта

    Args:
        project_name (str): Имя проекта
        invited_user_email (str): Почта приглашаемого пользователя
        owner_id (int): Id автора-владельца проекта

    Returns:
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
