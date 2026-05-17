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


@run_code_bp.route("/code/run", methods=["POST"])
def run_code_route():
    """
    Запуск проекта
    ---
    tags:
      - features/code
    description: |
      Запускает проект
    parameters:
      - name: Authorization
        in: header
        required: true
        type: string
        example: "Bearer pbkdf2:sha256:260000$xyz..."
      - name: body
        in: body
        required: true
        schema:
          type: object
          properties:
            project_id:
              type: int
              example: 12
    responses:
      201:
        description: Успешное создание
        schema:
          type: object
          properties:
              result_data:
                type: str
                example: "Hello, world!!!"
      401:
        description: Неверный access токен, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Неверный access токен, доступ запрещен"
      403:
        description: Неверные учетные данные, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Неверные учетные данные"
      409:
        description: Ошибка запуска проекта
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Error on line 4, column 6 ... "
    """
    auth_header = request.headers.get("Authorization")
    token, result = get_jwt_from_header(auth_header)

    if result == ResultsCodes.NO_TOKEN:
        response = create_unauthorized_response()
        return response

    id, id_result = get_id(token)
    if id_result != ResultsCodes.OK:
        return {"message": id_result}, 403

    data = request.json

    socketio.emit(
        "console_output",
        {"data": "jjjjj"},
        room=str(id),
    )

    return {}, 200


@socketio.on("run_code")
def handle_run_code(data):
    """ЗАПУСК КОДА ЧЕРЕЗ СОКЕТ"""
    user_id = session.get("user_id")
    project_id = data["project_id"]

    # ПОЛУЧАЕМ APP ДО ФОНОВОЙ ЗАДАЧИ
    app = current_app._get_current_object()

    print(f"Запуск кода от пользователя {user_id}, проект {project_id}")

    # ЗАПУСКАЕМ В ФОНЕ С ПЕРЕДАЧЕЙ APP
    socketio.start_background_task(run_code, project_id, user_id, app)

    # ОТВЕЧАЕМ СРАЗУ
    socketio.emit("run_started", {"project_id": project_id}, room=str(user_id))


@socketio.on("send_input")
def handle_input(data):
    """ОТПРАВКА ВВОДА В КОНТЕЙНЕР"""
    user_id = session.get("user_id")
    project_id = data["project_id"]
    user_input = data["input"]

    session_key = f"{user_id}_{project_id}"

    if session_key in active_containers:
        container = active_containers[session_key]["container"]
        socket = container.attach_socket(params={"stdin": 1, "stream": 1})
        socket.write(user_input.encode())
        socket.close()
