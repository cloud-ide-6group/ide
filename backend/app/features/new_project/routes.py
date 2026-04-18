from . import new_project_bp
from ...shared.features.jwt_token.routes import get_id
from app.shared.features.languages.service import lang_exists
from flask import request
from .service import *


@new_project_bp.route("/new_project", methods=["POST"])
def create_new_project():
    """
    Создание нового проекта
    ---
    tags:
      - features/new_project
    parameters:
      - name: body
        in: body
        required: true
        schema:
          type: object
          properties:
            access_token:
              type: string
              example: "eyJhbGciOiJIUzI1NiIs..."
            project_name:
              type: string
              example: "TestProject"
            language_id:
              type: string
              example: "7"
    responses:
      201:
        description: Успешное создание
      403:
        description: Неверные учетные данные, доступ запрещен
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Неверные учетные данные"
      500:
        description: Ошибка создания проекта
        schema:
          type: object
          properties:
              message:
                type: string
                example: "Проект уже существует"
    """
    data = request.json
    access_token = data["access_token"]
    id, id_result = get_id(access_token)
    if id_result != ResultsCodes.OK:
        return {"message": id_result}, 403

    project_name = data["project_name"]
    language_id = data["language_id"]

    is_lang_exists = lang_exists(language_id)
    if not is_lang_exists:
        return {"message": ResultsCodes.INCORRECT_LANG}, 500

    result = create_project_dir(project_name)
    if result != ResultsCodes.OK:
        return {"message": result}, 500

    result = create_project(id, project_name, language_id)

    if result != ResultsCodes.OK:
        return {"message": result}, 500

    return {}, 201
