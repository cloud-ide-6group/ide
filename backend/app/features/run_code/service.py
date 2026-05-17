from .repository import project_repo, language_repo
from app.shared.consts import ResultsCodes, MOUNT_DIR, CONFIG_FILE
from app.shared.extensions import socketio
from flask_socketio import join_room
import docker
import chardet
from flask import session, request
import json
import os
from dotenv import load_dotenv

load_dotenv()

active_containers = {}


def run_code(project_id, user_id, app):
    """ЗАДАЧА ЗАПУСКА КОДА"""
    with app.app_context():
        user_in_project, result = project_repo.is_user_in_project(user_id, project_id)
        if result != ResultsCodes.OK or not user_in_project:
            socketio.emit("console_output", {"data": result}, room=str(user_id))
            return

        projects_dir = os.getenv("PROJECTS_PATH")
        project = project_repo.get_by_id(project_id)
        if project is None:
            socketio.emit(
                "console_output",
                {"data": ResultsCodes.PROJECT_NOT_FOUND},
                room=str(user_id),
            )
            return

        project_dir = os.path.join(projects_dir, project.name)
        language = language_repo.get_by_id(project.language_id)

        if not language:
            socketio.emit(
                "console_output",
                {"data": ResultsCodes.INCORRECT_LANG},
                room=str(user_id),
            )
            return

        image_command = (
            language.command + " " + MOUNT_DIR + read_start_file_from_conf(project_dir)
        )

        run_docker(project_dir, language.image_name, image_command, user_id, project_id)


def run_docker(project_dir, image_name, image_command, user_id, project_id):
    """ЗАПУСК ДОКЕР КОНТЕЙНЕРА"""
    client = docker.from_env()

    container = client.containers.run(
        image_name.lower(),
        command=image_command,
        volumes={project_dir: {"bind": MOUNT_DIR, "mode": "ro"}},
        network_mode="none",
        cap_drop=["ALL"],
        read_only=True,
        detach=True,
        tty=True,
        remove=False,
        stdin_open=True,
        environment={
            "LANG": "C.UTF-8",
            "LC_ALL": "C.UTF-8",
            "PYTHONIOENCODING": "utf-8",
            "PYTHONUTF8": "1",
            "NODE_OPTIONS": "--input-encoding=utf-8",
            "JAVA_TOOL_OPTIONS": "-Dfile.encoding=UTF-8",
        },
    )

    stdin_socket = container.attach_socket(params={"stdin": 1, "stream": 1})

    session_key = f"{user_id}_{project_id}"
    active_containers[session_key] = {
        "container": container,
        "client": client,
        "stdin_socket": stdin_socket,
    }

    buffer = b""

    for chunk in container.logs(stream=True, follow=True):
        buffer += chunk

        while b"\n" in buffer:
            line_bytes, buffer = buffer.split(b"\n", 1)

            detected = chardet.detect(line_bytes)
            encoding = detected["encoding"] if detected["encoding"] else "utf-8"

            try:
                line = line_bytes.decode(encoding)
            except:
                line = line_bytes.decode("utf-8", errors="replace")

            if line.strip():
                socketio.emit("console_output", {"data": line}, room=str(user_id))

    if session_key in active_containers:
        active_containers[session_key]["stdin_socket"].close()
        del active_containers[session_key]


def read_start_file_from_conf(project_dir):
    conf_file = os.path.join(project_dir, CONFIG_FILE)

    if not os.path.exists(conf_file):
        return None

    with open(conf_file) as f:
        data = json.load(f)

    if "start_file" not in data:
        return None

    return data["start_file"]
