from . import run_code_bp
from flask import session, current_app
from app.shared.extensions import socketio
from .service import run_code, get_container
from app.shared.extensions import redis_client
from config import DebugConfig


@socketio.on("run_code")
def run_code_socket(data):
    """
    Сокет run_code. Сокет запуска кода

    Args:
        project_id (int): Id проекта.

    Example:
        >>> { "project_id": 79 }
    """
    user_id = session.get("user_id")
    project_id = data["project_id"]

    app = current_app._get_current_object()

    socketio.start_background_task(run_code, project_id, user_id, app)


@socketio.on("send_input")
def input_socket(data):
    """
    Сокет send_input. Сокет отправки ввода в программу

    Args:
        project_id (int): Id проекта.
        input (int/str): Данные ввода.

    Example:
        >>> { "project_id": 79, "input": "the line" }
    """
    user_id = session.get("user_id")
    project_id = data["project_id"]
    user_input = str(data["input"])

    session_key = f"{user_id}_{project_id}"

    container_id = redis_client.get(session_key)
    container = get_container(container_id)
    if container:
        stdin_socket = container.attach_socket(params={"stdin": 1, "stream": 1})
        if DebugConfig.PLATFORM == "LINUX":
            stdin_socket._sock.sendall((user_input + "\n").encode("utf-8"))
        elif DebugConfig.PLATFORM == "WINDOWS":
            stdin_socket.sendall((user_input + "\n").encode("utf-8"))
        stdin_socket.close()


@socketio.on("stop_code")
def stop_code_socket(data):
    """
    Сокет stop_code. Сокет прерывания выполнения программы

    Args:
        project_id (int): Id проекта.

    Example:
        >>> { "project_id": 79 }
    """
    user_id = session.get("user_id")
    project_id = data["project_id"]
    session_key = f"{user_id}_{project_id}"

    container_id = redis_client.get(session_key)
    if not container_id:
        return

    container = get_container(container_id)
    if container:
        try:
            container.stop()
            container.remove()
        except Exception as e:
            print(f"Ошибка: {e}")

    redis_client.delete(session_key)
