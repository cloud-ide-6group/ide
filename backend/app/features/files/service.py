from .repository import file_repo, project_repo
from app.shared.consts import ResultsCodes
import os
from dotenv import load_dotenv
from pathlib import Path
from app.shared.extensions import socketio

load_dotenv()


def get_file_path(current_file, file_path):
    if current_file.parent_id:
        parent_file = file_repo.get_by_id(current_file.parent_id)
        parent_name = parent_file.name
        file_path = os.path.join(parent_name, file_path)

        file_path = get_file_path(current_file, file_path)

    return file_path


def create_file(name, project_name, parent_name, is_folder):
    parent = file_repo.get_by_name(parent_name)
    if parent:
        project = project_repo.get_by_name(project_name)
        if project:
            result = create_file_on_disk(name, parent, project_name)
            if result == ResultsCodes.OK:
                file_repo.create_file(name, parent.id, project.id, is_folder)
                return ResultsCodes.OK
            else:
                return result
        else:
            return ResultsCodes.PROJECT_NOT_FOUND
    else:
        return ResultsCodes.NO_PARENT


def create_file_on_disk(name, parent, project_name):
    project_dir = os.path.join(os.getenv("PROJECTS_PATH"), project_name)

    current_file = parent
    file_path = ""
    while current_file.parent_id:
        parent_file = file_repo.get_by_id(current_file.parent_id)
        parent_name = parent_file.name
        file_path = os.path.join(parent_name, file_path)
        current_file = parent_file

    file_path = os.path.join(project_dir, file_path, name)

    try:
        path_obj = Path(file_path)
        path_obj.parent.mkdir(parents=True, exist_ok=True)
        path_obj.touch()
        return ResultsCodes.OK
    except Exception as e:
        print(f"Ошибка создания файла: {e}")
        return ResultsCodes.CREATE_FILE_ERROR


def delete_file(file_id):
    was_deleted = file_repo.delete_file(file_id)
    if was_deleted == True:
        return ResultsCodes.OK
    return ResultsCodes.FILE_NOT_EXIST


def get_file_content(file_id):
    file = file_repo.get_by_id(file_id)
    file_path = get_file_path(file)
    project = project_repo.get_by_id(file.project_id)
    project_dir = os.path.join(os.getenv("PROJECTS_PATH"), project.name)
    file_path = os.path.join(project_dir, file_path, file.name)
    content = ""
    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()
    return content


def send_file_content_to_klients(file_id):
    """
    Посылает клиенту все уведомления по сокету. Название события -- notifications_list

    Args:
        invited_user_id (int): Id пользователя
    """
    socketio.emit(
        "get_file_content",
        {"content": get_file_content(file_id)},
    )
