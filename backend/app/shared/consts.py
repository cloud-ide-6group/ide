class ErrorCodes:
    """
    Возможные ошибки.

    Attributes:
        OK: ""
        INCORRECT_USER_DATA: "Почта, пароль и имя обязательны"
        EMAIL_EXISTS: "Пользователь с таким именем уже существует"
        USER_NOT_FOUND: "Пользователь не найден"
        INVALID_PASSWORD: "Неверный пароль"
        REFREESH_TOKEN_NEEDED: "Неверный refresh токен"
        REFRESH_TOKEN_EXPIRED: "Refresh токен истек"
    """

    OK = ""
    INCORRECT_USER_DATA = "Почта, пароль и имя обязательны"
    EMAIL_EXISTS = "Пользователь с таким именем уже существует"
    USER_NOT_FOUND = "Пользователь не найден"
    INVALID_PASSWORD = "Неверный пароль"
    REFREESH_TOKEN_NEEDED = "Неверный refresh токен"
    REFRESH_TOKEN_EXPIRED = "Refresh токен истек"
