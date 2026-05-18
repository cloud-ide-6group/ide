from . import run_code_bp
from flask import session, current_app
from app.shared.extensions import socketio
from .service import run_code, get_container
from app.shared.extensions import redis_client


@socketio.on("run_code")
def handle_run_code(data):
    """ЗАПУСК КОДА ЧЕРЕЗ СОКЕТ"""
    user_id = session.get("user_id")
    project_id = data["project_id"]

    app = current_app._get_current_object()

    socketio.start_background_task(run_code, project_id, user_id, app)


@socketio.on("send_input")
def handle_input(data):
    """ОТПРАВКА ВВОДА В КОНТЕЙНЕР"""
    user_id = session.get("user_id")
    project_id = data["project_id"]
    user_input = str(data["input"])

    session_key = f"{user_id}_{project_id}"

    container_id = redis_client.get(session_key)
    container = get_container(container_id)
    if container:
        stdin_socket = container.attach_socket(params={"stdin": 1, "stream": 1})
        stdin_socket.sendall((user_input + "\n").encode("utf-8"))
        stdin_socket.close()
