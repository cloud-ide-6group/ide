import base64
from .repository import user_repo, project_repo
from ...shared.consts import ResultsCodes
from app.shared.dbmodels import User
import os
from dotenv import load_dotenv
from app.shared.features.password_hash.service import check_password_with_hash

load_dotenv()

BASE_IMAGES_DIR = "users_imgs"


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


def update_user_data(id, email, name, password_hash, photo_path):
    """
    Создает обновленный объект user и отправляет его в репозиторий, чтобы сохранить новые данные

    Args:
        id (int): Id пользователя
        email (str): Почта пользователя
        name (str): Имя пользователя
        password_hash (str): Хэш пароля
        photo_path (str): Путь к фото профиля на сервере

    Returns:
        User: Обновленный пользователь
        ResultCodes: Результат выполнения
    """
    old_user = user_repo.get_by_id(id)

    if old_user == None:
        return None, ResultsCodes.USER_NOT_FOUND

    new_user = User()
    new_user.id = id

    try:
        new_user.email = email or old_user.email
    except:
        return None, ResultsCodes.INVALID_EMAIL

    new_user.name = name or old_user.name
    new_user.password_hash = password_hash or old_user.password_hash
    new_user.photo_path = photo_path or old_user.photo_path

    try:
        return user_repo.update_user(new_user), ResultsCodes.OK
    except:
        return None, ResultsCodes.UPDATED_DATA_INCORRECT


def check_old_password(id, password):
    """
    Сверяет пароль с тем, что есть в БД

    Args:
        id (int): Id пользователя
        password (str): Пароль, который необходимо сверить

    Returns:
        ResultCodes: Результат
    """
    old_password_hash = user_repo.get_password_hash(id)
    result = check_password_with_hash(old_password_hash, password)

    if result == False:
        return ResultsCodes.INCORRECT_OLD_PASSWORD

    return ResultsCodes.OK


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


def save_photo(base64_string, user_id):
    """
    Сохраняет фото из base64 в файл

    Args:
        base64_string (str): Строка base64 (может быть с префиксом data:image/png;base64, или без)
        user_id (int): Id пользователя

    Returns:
        str: Имя файла
        ResultCodes: Результат выполнения операции
    """
    if "base64," in base64_string:
        base64_string = base64_string.split("base64,")[1]

    try:
        image_data = base64.b64decode(base64_string)
    except Exception as e:
        return None, ResultsCodes.INVALID_BASE64

    extension = get_image_extension(image_data)

    filename = f"{BASE_IMAGES_DIR}/img_user{user_id}{extension}"

    upload_folder = os.getenv("IMAGES_PATH")
    os.makedirs(upload_folder, exist_ok=True)

    filepath = os.path.join(upload_folder, filename)

    with open(filepath, "wb") as f:
        f.write(image_data)

    return filename, ResultsCodes.OK


def get_image_extension(image_data):
    """
    Получает расширение фото из base64 строки

    Args:
        image_data (str): Строка фото

    Returns:
        str: Расширение
    """
    if image_data[:4] == b"\x89PNG":
        return ".png"

    if image_data[:2] == b"\xff\xd8":
        return ".jpg"

    if image_data[:3] == b"GIF":
        return ".gif"

    if image_data[:4] == b"RIFF" and image_data[8:12] == b"WEBP":
        return ".webp"

    return ".jpg"
