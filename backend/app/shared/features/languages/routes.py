from . import languages_bp
from .service import *
from flask import jsonify


@languages_bp.route("/languages", methods=["GET"])
def get_languages():
    """
    Получить все языки
    ---
    tags:
      - shared/features/languages
    description: |
      Эндпоинт возвращает массив языков, доступных в системе.
    responses:
      200:
        description: Успешное получение массива языков
        examples:
        application/json:
            {
            "data": [
                {
                "id": 1,
                "name": "Java17",
                "description": "Java 17 language"
                },
                {
                "id": 2,
                "name": "Java21",
                "description": "Java 21 language"
                }
            ]
        }
    """
    langs = get_all_langs()

    return jsonify(langs), 200
