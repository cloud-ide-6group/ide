from .repository import file_repo, project_repo
from app.shared.consts import ResultsCodes
import os
from dotenv import load_dotenv
from pathlib import Path
from app.shared.extensions import socketio
from app.features.project.service import send_files_to_clients
import shutil

load_dotenv()


def get_file_path(current_file, file_path):
    if current_file == None:
        return file_path

    if current_file.parent_id:
        parent_file = file_repo.get_by_id(current_file.parent_id)
        parent_name = parent_file.name
        file_path = os.path.join(parent_name, file_path)

        file_path = get_file_path(current_file, file_path)

    return file_path


def create_file(name, project_name, parent_name, is_folder, user_id):
    project = project_repo.get_by_name(project_name)
    if project:
        if not is_user_in_project(user_id, project.id):
            return ResultsCodes.CANT_CHANGE_FILE
        parent = file_repo.get_by_name(parent_name)
        result = create_file_on_disk(name, parent, project_name, is_folder)
        if result == ResultsCodes.OK:
            file_repo.create_file(
                name, None if parent is None else parent.id, project.id, is_folder
            )
            send_files_to_clients(project.id)
            return ResultsCodes.OK
        else:
            return result
    else:
        return ResultsCodes.PROJECT_NOT_FOUND


def is_user_in_project(user_id, project_id):
    return project_repo.is_user_in_project(user_id, project_id)


def create_file_on_disk(name, parent, project_name, is_folder):
    project_dir = os.path.join(os.getenv("PROJECTS_PATH"), project_name)

    file_path = get_file_path(parent, "")

    file_path = os.path.join(project_dir, file_path)
    if parent:
        file_path = os.path.join(file_path, parent.name)
    file_path = os.path.join(file_path, name)

    try:
        path_obj = Path(file_path)
        if is_folder:
            path_obj.mkdir(parents=True, exist_ok=True)
        else:
            path_obj.parent.mkdir(parents=True, exist_ok=True)
            path_obj.touch()
        return ResultsCodes.OK
    except Exception as e:
        print(f"Ошибка создания файла: {e}")
        return ResultsCodes.CREATE_FILE_ERROR


def delete_file_from_disk(name, parent, project_name, is_folder):
    project_dir = os.path.join(os.getenv("PROJECTS_PATH"), project_name)

    file_path = get_file_path(parent, "")

    file_path = os.path.join(project_dir, file_path)
    if parent:
        file_path = os.path.join(file_path, parent.name)
    file_path = os.path.join(file_path, name)

    path = Path(file_path)
    if path.exists():
        if is_folder:
            shutil.rmtree(path)
        else:
            path.unlink()


def delete_file(file_id, user_id):
    file = file_repo.get_by_id(file_id)
    if not file:
        return ResultsCodes.FILE_NOT_EXIST
    project = file_repo.get_project(file_id)
    if not project:
        return ResultsCodes.PROJECT_NOT_FOUND
    if not is_user_in_project(user_id, project.id):
        return ResultsCodes.CANT_CHANGE_FILE
    parent = file_repo.get_by_id(file.parent_id)
    was_deleted = file_repo.delete_file(file_id)
    if was_deleted == True:
        delete_file_from_disk(file.name, parent, project.name, file.is_folder)
        send_files_to_clients(project.id)
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


def save_file_content(file_id, file_content):
    file = file_repo.get_by_id(file_id)
    file_path = get_file_path(file, "")
    with open(file_path, "w", encoding="utf-8") as f:
        f.write(file_content)
        send_file_content_to_clients(file_id)


def send_file_content_to_clients(file_id):
    """
    Посылает клиенту все уведомления по сокету. Название события -- notifications_list

    Args:
        invited_user_id (int): Id пользователя
    """
    file = file_repo.get_by_id(file_id)
    socketio.emit(
        "get_file_content",
        {"content": get_file_content(file_id)},
        room=f"project_{file.project_id}",
    )
