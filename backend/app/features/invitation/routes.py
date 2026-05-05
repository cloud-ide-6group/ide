from . import invitation_bp
from flask import request, make_response
from app.shared.features.jwt_token.service import get_id
from app.shared.consts import ResultsCodes
from .service import add_user_in_project, delete_user_from_project


@invitation_bp.route("/invite", methods=["POST"])
def invite():
    """
    Приглашение пользователя в проект
    ---
    tags:
      - features/invitation
    description: |
      Добавляет пользователя в проект
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
            invited_user_email:
              type: string
              example: "test@mail.ru"
    responses:
      200:
        description: Успешное пришлашение
        schema:
          type: object
          properties:
              project_id:
                type: int
                example: 25
      409:
        description: Ошибка приглашения
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Пользователь не найден"
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
    invited_user_email = data["invited_user_email"]

    result = add_user_in_project(project_name, invited_user_email, id)
    if result == ResultsCodes.OK:
        return {}, 200
    else:
        return {"message": result}, 409


@invitation_bp.route("/delete/invited", methods=["DELETE"])
def delete_invited():
    """
    Удалить пользователя из проекта
    ---
    tags:
      - features/invitation
    description: |
      Удаляет ранее приглашенного пользователя из проекта
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
            invited_user_email:
              type: string
              example: "test@mail.ru"
    responses:
      200:
        description: Успешное удаление
        schema:
          type: object
          properties:
              project_id:
                type: int
                example: 25
      409:
        description: Ошибка удаления
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Пользователь не найден"
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
