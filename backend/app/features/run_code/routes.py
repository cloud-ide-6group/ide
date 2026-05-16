from . import run_code_bp

@run_code_bp.route("/code/run", methods=["POST"])
def run_code():
    """
    Запуск проекта
    ---
    tags:
      - features/code
    description: |
      Создает файл
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