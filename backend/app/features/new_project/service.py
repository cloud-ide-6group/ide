import os
from dotenv import load_dotenv
from ...shared.consts import ResultsCodes
from .repository import project_repo

load_dotenv()


def create_project_dir(project_name):
    """
    Выделяет пространство на диске на проект

    Args:
        project_name (str): Имя проекта

    Returns:
        resultCodes: Результат выполнения операции

    Note:
        Переменная окружения PROJECTS_PATH должна указывать на корневую папку со всеми проектами.
    """
    if project_name == "" or project_name == None:
        return ResultsCodes.INCORRECT_PROJECT_NAME

    projects_dir = os.getenv("PROJECTS_PATH")
    new_project_dir = os.path.join(projects_dir, project_name)

    if not os.path.exists(new_project_dir):
        os.makedirs(new_project_dir)
        return ResultsCodes.OK
    else:
        return ResultsCodes.PROJECT_EXISTS_ALREADY


def create_project(user_id, project_name, language_id):
    """
    Добавляет проект в базу данных

    Args:
        user_id (int): Id создателя
        project_name (str): Имя проекта
        language_id (int): Id языка

    Returns:
        ResultCodes: Код результата операции
    """
    if user_id == None:
        return ResultsCodes.USER_NOT_FOUND
    if language_id == None:
        return ResultsCodes.INCORRECT_LANG
    if project_name == "" or project_name == None:
        return ResultsCodes.INCORRECT_PROJECT_NAME

    if project_repo.get_project(project_name) != None:
        return ResultsCodes.PROJECT_EXISTS_ALREADY

    project = project_repo.create_project(project_name, language_id, user_id)
    if project == None:
        return ResultsCodes.PROJECT_CREATE_ERROR
    return ResultsCodes.OK
