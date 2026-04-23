import os


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
    REFRESH_TOKEN_NEEDED = "Неверный refresh токен"
    REFRESH_TOKEN_EXPIRED = "Refresh токен истек"
    INCORRECT_USER_NAME = "Неверное имя пользователя"
    ACCESS_TOKEN_EXPIRED = "Access токен истек"
    USER_ID_NULL = "User id равен null"
    PROJECT_EXISTS_ALREADY = "Проект с таким именем уже существует"
    PROJECT_CREATE_ERROR = "Ошибка создания проекта"
    INCORRECT_LANG = "Некорректный язык"
    INCORRECT_PROJECT_NAME = "Некорректное имя проекта"
    DATA_UPDATED = "Данные обновлены"
    INCORRECT_OLD_PASSWORD = (
        "Вы не можете установить новый пароль, так как ввели неверный старый пароль"
    )
    INVALID_BASE64 = "Неверна строка изображения"
    NEW_PASSWORD_NULL = "Новый пароль не может быть null"
