from . import invitation_bp
from flask import request, make_response
from app.shared.features.jwt_token import get_id
from app.shared.consts import ResultsCodes
from .service import add_user_in_project, delete_user_from_project


@invitation_bp.route("/invite", methods=["POST"])
def invite():
    """
    Создание нового проекта
    ---
    tags:
      - features/project
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
            project_name:
              type: string
              example: "TestProject"
            language_id:
              type: int
              example: 7
    responses:
      201:
        description: Успешное создание
        schema:
          type: object
          properties:
              project_id:
                type: int
                example: 25
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
        description: Ошибка создания проекта
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Проект уже существует"
    """
    auth_header = request.headers.get("Authorization")
    if not auth_header or not auth_header.startswith("Bearer "):
        response = make_response({"message": "Токен не предоставлен"}, 401)
        response.headers["WWW-Authenticate"] = "Bearer"
        return response

    data = request.json
    access_token = auth_header.split(" ")[1]
    id, id_result = get_id(access_token)
    if id_result != ResultsCodes.OK:
        return {"message": id_result}, 403

    project_name = data["project_name"]
    invited_user_email = data["email"]

    result = add_user_in_project(project_name, invited_user_email, id)
    if result == ResultsCodes.OK:
        return {}, 200
    else:
        return {"message": result}, 409


@invitation_bp.route("/delete/invited", methods=["POST"])
def delete_invited():
    """
    Создание нового проекта
    ---
    tags:
      - features/project
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
            project_name:
              type: string
              example: "TestProject"
            language_id:
              type: int
              example: 7
    responses:
      201:
        description: Успешное создание
        schema:
          type: object
          properties:
              project_id:
                type: int
                example: 25
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
        description: Ошибка создания проекта
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Проект уже существует"
    """
    auth_header = request.headers.get("Authorization")
    if not auth_header or not auth_header.startswith("Bearer "):
        response = make_response({"message": "Токен не предоставлен"}, 401)
        response.headers["WWW-Authenticate"] = "Bearer"
        return response

    data = request.json
    access_token = auth_header.split(" ")[1]
    id, id_result = get_id(access_token)
    if id_result != ResultsCodes.OK:
        return {"message": id_result}, 403

    project_name = data["project_name"]
    invited_user_email = data["email"]

    result = delete_user_from_project(project_name, invited_user_email, id)
    if result == ResultsCodes.OK:
        return {}, 200
    else:
        return {"message": result}, 409
