from .repository import project_repo, language_repo
from app.shared.consts import ResultsCodes
import docker
import os
from dotenv import load_dotenv

load_dotenv()


def run_code(project_id, user_id):
    user_in_project, result = project_repo.is_user_in_project(user_id, project_id)
    if result != ResultsCodes.OK or not user_in_project:
        return "", result

    projects_dir = os.getenv("PROJECTS_PATH")
    project = project_repo.get_by_id(project_id)
    if project is None:
        return "", ResultsCodes.PROJECT_NOT_FOUND
    project_dir = os.path.join(projects_dir, project.name)
    run_docker(project_dir)

    language = language_repo.get_by_id(project.language_id)
    run_docker(project_dir, language.name)


def run_docker(project_dir, image_name, image_command):
    client = docker.from_env()

    container = client.containers.run(
        image_name,
        command=image_command,
        volumes={project_dir: {"bind": "/app", "mode": "ro"}},
        network_mode="none",
        cap_drop=["ALL"],
        read_only=True,
        detach=True,
        remove=False,
    )

    result = container.wait()
    logs = container.logs().decode("utf-8")

    container.remove()
    client.close()

    return logs
