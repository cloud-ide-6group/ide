class ResultsCodes:
    """
    Возможные результаты.
    """

    OK = ""
    INCORRECT_USER_DATA = "Почта, пароль и имя обязательны"
    EMAIL_EXISTS = "Пользователь с такой почтой уже существует"
    USER_NOT_FOUND = "Пользователь не найден"
    INVALID_PASSWORD = "Неверный пароль"
    INVALID_EMAIL = "Неверный email"
    REFREESH_TOKEN_NEEDED = "Неверный refresh токен"
    REFRESH_TOKEN_EXPIRED = "Refresh токен истек"
