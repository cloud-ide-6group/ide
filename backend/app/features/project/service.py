import os
from dotenv import load_dotenv
from ...shared.consts import ResultsCodes
from .repository import project_repo, file_repo
from app.shared.extensions import socketio
from flask import request
from app.shared.features.jwt_token.service import (
    get_jwt_from_header,
    create_unauthorized_response,
    get_id,
)
from flask_socketio import join_room, session, leave_room

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


def send_files_to_klients(project_id):
    """
    Посылает клиенту все уведомления по сокету. Название события -- notifications_list

    Args:
        invited_user_id (int): Id пользователя
    """
    root_file = file_repo.get_root_file(project_id)
    files = jsonify_file(root_file)

    socketio.emit(
        "files_list",
        {"files_tree": files},
        room=f"project_{project_id}",
    )


@socketio.on("join_project_room")
def join_project_room(data):
    """
    Подключение к комнате проекта.
    Клиент вызывает это событие, когда открывает проект.
    """
    auth_header = request.headers.get("Authorization")
    token, result = get_jwt_from_header(auth_header)

    if result != ResultsCodes.OK:
        return create_unauthorized_response()

    id, result = get_id(token)
    project_id = data.get("project_id")
    project = project_repo.get_by_id(project_id)
    if project:
        if project.owner_id == id or project_repo.is_user_invited(project_id, id):
            for room in session.get("project_rooms", []):
                leave_room(room)
            new_room = f"project_{project_id}"
            join_room(new_room)
            session["project_rooms"] = [new_room]
            return True

    return False
