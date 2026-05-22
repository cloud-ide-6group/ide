from .repository import project_repo, language_repo
from app.shared.consts import ResultsCodes, MOUNT_DIR, CONFIG_FILE
from app.shared.extensions import redis_client
from app.shared.extensions import socketio
import docker
import chardet
import json
import os
import re
from dotenv import load_dotenv

load_dotenv()


def run_code(project_id, user_id, app):
    """
    Запуск кода

    Args:
        project_id (int): Id проекта.
        user_id (int): Id пользователя.
        app (): Объект приложения
    """
    with app.app_context():
        result = project_repo.is_user_in_project(user_id, project_id)
        if result == False:
            print(ResultsCodes.USER_IS_NOT_IN_PROJECT)
            return

        projects_dir = os.getenv("PROJECTS_PATH")
        project = project_repo.get_by_id(project_id)
        if project is None:
            print(ResultsCodes.PROJECT_NOT_FOUND)
            return

        project_dir = os.path.join(projects_dir, project.name)
        language = language_repo.get_by_id(project.language_id)

        if not language:
            print(ResultsCodes.INCORRECT_LANG)
            return

        start_file = read_start_file_from_conf(project_dir)
        if start_file is None:
            print(ResultsCodes.INCORRECT_SETUP)
            return

        image_command = prepare_command(language.command, MOUNT_DIR, start_file)

        run_docker(project_dir, language.image_name, image_command, user_id, project_id)


def run_docker(project_dir, image_name, image_command, user_id, project_id):
    """
    Запуск контейнера с кодом

    Args:
        project_dir (str): Путь к проекту.
        image_name (str): Имя изображения для создания контенера.
        image_command (str): Команда для выполнения в контейнере.
        user_id (int): Id пользователя.
        project_id (int): Id проекта.
    """
    client = docker.from_env()

    session_key = f"{user_id}_{project_id}"

    old_container_id = redis_client.get(session_key)
    if old_container_id:
        try:
            old_container = client.containers.get(old_container_id)
            old_container.stop()
            old_container.remove()
        except Exception as e:
            print(f"Старый контейнер не найден: {e}")
        redis_client.delete(session_key)

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
            "JAVA_TOOL_OPTIONS": "-Dfile.encoding=UTF-8",
        },
    )

    stdin_socket = container.attach_socket(params={"stdin": 1, "stream": 1})

    redis_client.setex(session_key, 3600, container.id)

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
                line = re.sub(r"[\r\n\t\x0b\x0c]", "", line)
                socketio.emit(
                    "console_output",
                    {"data": line, "is_ended": False},
                    room=str(user_id),
                )

    socketio.emit(
        "console_output",
        {"data": "Программа завершена.", "is_ended": True},
        room=str(user_id),
    )

    container_id = redis_client.get(session_key)
    if container_id:
        stdin_socket.close()
        try:
            if container:
                container.stop()
                container.remove()
        except Exception as e:
            print(f"Ошибка при удалении контейнера: {e}")
        finally:
            redis_client.delete(session_key)


def read_start_file_from_conf(project_dir):
    """
    Считывает стартовый файл из конфигураций

    Args:
        project_dir (str): Путь к проекту.
    """
    conf_file = os.path.join(project_dir, CONFIG_FILE)

    if not os.path.exists(conf_file):
        return None

    with open(conf_file) as f:
        data = json.load(f)

    if "start_file" not in data:
        return None

    return data["start_file"]


def get_container(container_id):
    """
    Получить запущенный контейнер из докера

    Args:
        container_id (int): Id контейнера
    """
    if container_id:
        try:
            client = docker.from_env()
            container = client.containers.get(container_id)
            return container
        except Exception as e:
            return None


def prepare_command(command, mount_dir, file_name):
    """
    Заменяет специальные символы актуальными в команде.

    Args:
        command (str): Исходная команда
        mount_dir (str): Директория в которую монтировать в контейнере
        file_name (str): Имя стартового файла

    Returns:
        str: Улучшенная команда
    """
    return (
        command.replace("{file}", mount_dir + file_name)
        .replace("{class}", file_name.split(".")[0])
        .replace("?mount?", mount_dir[:-1])
    )
