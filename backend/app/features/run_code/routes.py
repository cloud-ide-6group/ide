from . import run_code_bp
from flask import request
from app.shared.features.jwt_token.service import (
    get_id,
    get_jwt_from_header,
    create_unauthorized_response,
)
from app.shared.consts import ResultsCodes


@run_code_bp.route("/code/run", methods=["POST"])
def run_code():
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
