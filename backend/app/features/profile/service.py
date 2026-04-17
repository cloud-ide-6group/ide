import base64
from .repository import user_repo, project_repo
from ...shared.consts import ResultsCodes
import os
from dotenv import load_dotenv

load_dotenv()


def get_user_data(id):
    """
    Получает данные пользователя по id

    Args:
        id (int): id пользователя

    Returns:
        User: Пользователь
        ResultCodes: Результат

    Example:
        >>> user, result = get_user_data(7)
    """
    if id == None:
        return None, ResultsCodes.USER_ID_NULL

    user = user_repo.get_by_id(id)
    if user == None:
        return user, ResultsCodes.USER_NOT_FOUND

    return user, ResultsCodes.OK


def get_user_projects(user_id):
    """
    Возвращает все проекты, которые создал этот пользователь

    Args:
        user_id (int): Id пользователя

    Returns:
        list[dict]: Массив словарей с полями проектов
        Каждый словарь содержит:
            - id (int): Id проекта
            - name (str): Название проекта
        ResultCodes: Результат операции
    """
    if user_id == None:
        return [], ResultsCodes.USER_ID_NULL

    projects = []

    projects_raw_data = project_repo.get_by_owner_id(user_id)
    for p in projects_raw_data:
        projects.append({"id": p.id, "name": p.name})

    return projects, ResultsCodes.OK


def get_photo_base_64(image_path):
    """
    Преобразует фото в base64 строку.

    Args:
        image_path (str): Относительный путь к файлу внутри IMAGES_PATH

    Returns:
        str: Строка в формате base64

    Example:
        >>> base64_str = get_photo_base_64('path/image.jpg')

    Note:
        Переменная окружения IMAGES_PATH должна указывать на корневую папку с фото.
    """
    images_dir = os.getenv("IMAGES_PATH")

    with open(os.path.join(images_dir, image_path), "rb") as img_file:
        photo_base64 = base64.b64encode(img_file.read()).decode("utf-8")

    return photo_base64
