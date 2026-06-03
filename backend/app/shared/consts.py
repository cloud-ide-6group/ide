from datetime import timedelta

MOUNT_DIR = "/app/"
"""Путь для монтирования директории в контейнере."""

CONFIG_FILE = "conf.ctgson"
"""Имя конфигурационного файла."""

MAX_PROJECTS_COUNT = 5
"""Максимальное количество проектов для одного пользователя."""

SUBSCRIPTION_DURATION = timedelta(days=30)
"""Длительность подписки (30 дней)."""


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
    INCORRECT_NAME = "Некорректное имя"
    DATA_UPDATED = "Данные обновлены"
    INCORRECT_OLD_PASSWORD = (
        "Вы не можете установить новый пароль, так как ввели неверный старый пароль"
    )
    INVALID_BASE64 = "Неверна строка изображения"
    NEW_PASSWORD_NULL = "Новый пароль не может быть null"
    UPDATED_DATA_INCORRECT = "Данные для обновления неверны"
    PROJECT_NOT_FOUND = "Проект не найден"
    USER_IS_IN_ALREADY = "Пользователь уже в проекте"
    CANT_INVITE = "Вы не можете пригласить сами себя"
    CANT_DELETE = "Вы не можете удалить сами себя"
    NO_TOKEN = "Токен не предоставлен"
    NO_PARENT = "Родительской папки нет"
    CREATE_FILE_ERROR = "Ошибка создания файла"
    FILE_NOT_EXIST = "Файла не существует"
    PARENT_NOT_EXIST = "Родителя не существует"
    CANT_CHANGE_FILE = "У пользователя нет прав на работу с файлом"
    THIS_IS_FOLDER = "Это папка, а не файл"
    FILE_ALREADY_EXIST = "Файл уже существует"
    CREATE_ERROR = "Ошибка создания"
    DELETE_ERROR = "Ошибка удаления"
    USER_IS_NOT_CHAT_CREATOR = "Вы не можете удалить чат, так как вы не его создатель"
    CHAT_NOT_FOUND = "Чат не найден"
    UNKNOWN_USER = "-"
    UNEXPECTED_ERROR = "Внутреняя ошибка сервера"
    USER_IS_NOT_IN_PROJECT = "Пользователь не в проекте"
    INVALID_TOKEN = "Токен некорректен"
    USER_NOT_OWNER = "Пользователь не имеет права удалить проект"
    CHAT_ALREADY_EXISTS = "Чат с таким идентификатором уже существует"
    INCORRECT_SETUP = "Некорректный файл conf.ctgson"
    SUBSCRIPTION_EXPIRED = "Ваша подписка не активна"
