import os
from dotenv import load_dotenv
from ...shared.consts import ResultsCodes
from .repository import project_repo, file_repo
from app.shared.extensions import socketio

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
        return ResultsCodes.INCORRECT_NAME

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
        Project: Созданный проект
        ResultCodes: Код результата операции
    """
    if user_id == None:
        return None, ResultsCodes.USER_NOT_FOUND
    if language_id == None:
        return None, ResultsCodes.INCORRECT_LANG
    if project_name == "" or project_name == None:
        return None, ResultsCodes.INCORRECT_NAME

    if project_repo.get_project(project_name) != None:
        return None, ResultsCodes.PROJECT_EXISTS_ALREADY

    project = project_repo.create_project(project_name, language_id, user_id)
    if project == None:
        return None, ResultsCodes.PROJECT_CREATE_ERROR
    return project, ResultsCodes.OK


def jsonify_file(file):
    """
    Превращает файл проекта в json объект-дерево.

    Args:
        file (File): Файл

    Returns:
        data (dict): {
            id (int): Id файла,
            name (str): Имя файлы,
            is_folder (str): Папка ли,
            list[dict]: Массив словарей json проектов
        }
    """
    children = file_repo.get_children(file.id)
    children_json = []
    for c in children:
        children_json.append(jsonify_file(c))

    return {
        "id": file.id,
        "name": file.name,
        "is_folder": file.is_folder,
        "children": children_json,
    }


def get_project_files_trees(project_id):
    """
    Возвращает json деревья всех файлов.

    Args:
        project_id (int): Id проекта

    Returns:
        list[dict]: Массив словарей json проектов
    """
    root_files = file_repo.get_root_files(project_id)
    files_trees = []
    for root_file in root_files:
        file_tree = jsonify_file(root_file)
        files_trees.append(file_tree)

    return files_trees


def is_user_invited(project_id, user_id):
    """
    Приглашен ли пользователь в проект

    Args:
        project_id (int): Id проекта
        user_id (int): Id пользователя

    Returns:
        bool: True, если пользователь уже в проекте, иначе False
    """
    return project_repo.is_user_invited(project_id, user_id)


def get_project_by_id(project_id):
    """
    Получить проект по id

    Args:
        project_id (int): Id проекта

    Returns:
        Project: Проект или None
    """
    return project_repo.get_by_id(project_id)


def send_files_to_all_clients(project_id):
    """
    Возвращает json деревья всех файлов всем пользователям в комнате проекта по сокету

    Args:
        project_id (int): Id проекта

    Returns:
        list[dict]: Массив словарей json проектов
    """
    socketio.emit(
        "files_list",
        {"files_trees": get_project_files_trees(project_id)},
        room=f"project_{project_id}",
    )
