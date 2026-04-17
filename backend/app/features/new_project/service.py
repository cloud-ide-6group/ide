import os
from dotenv import load_dotenv
from ...shared.consts import ResultsCodes
from .repository import project_repo

load_dotenv()


def create_project_dir(project_name):
    projects_dir = os.getenv("PROJECTS_PATH")
    new_project_dir = os.path.join(projects_dir, project_name)

    if not os.path.exists(new_project_dir):
        os.makedirs(new_project_dir)
        return ResultsCodes.OK
    else:
        return ResultsCodes.PROJECT_EXISTS_ALREADY


def create_project(user_id, project_name, language_id):
    project = project_repo.create_project(project_name, language_id, user_id)
    if project == None:
        return ResultsCodes.PROJECT_CREATE_ERROR
    return ResultsCodes.OK
