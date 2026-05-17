from . import run_code_bp
from flask import request, session, current_app
from app.shared.features.jwt_token.service import (
    get_id,
    get_jwt_from_header,
    create_unauthorized_response,
)
from app.shared.consts import ResultsCodes
from app.shared.extensions import socketio
from .service import active_containers, run_code


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

    if session_key in active_containers:
        stdin_socket = active_containers[session_key]["stdin_socket"]
        stdin_socket.sendall((user_input + "\n").encode("utf-8"))
