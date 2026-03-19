from werkzeug.security import generate_password_hash, check_password_hash
import jwt
from datetime import datetime, timedelta
from .repository import user_repo
from app.shared.consts import ErrorCodes


def get_password_hash(password):
    """
    Хеширует пароль для безопасного хранения в базе данных.

    Функция принимает сырой пароль и возвращает хеш,
    используя алгоритм scrypt (через werkzeug.security).

    Args:
        password (str): Сырой пароль пользователя (нехешированный)

    Returns:
        str: Хешированный пароль для хранения в БД

    Raises:
        ValueError: Если пароль пустой или None

    Example:
        >>> hashed = get_password_hash("my_secret_password")
        >>> print(hashed)
        'scrypt:32768:8:1$...'
    """
    if not password:
        raise ValueError("Password cannot be empty")
    return generate_password_hash(password)


def check_password_with_hash(password_hash, password):
    """
    Проверяет соответствие пароля его хешу.

    Сравнивает введенный пользователем пароль с хешем из базы данных.

    Args:
        password_hash (str): Хеш пароля из БД
        password (str): Пароль для проверки (от пользователя)

    Returns:
        bool: True если пароль правильный, False если нет

    Example:
        >>> stored_hash = get_password_hash("secret")
        >>> check_password_hash(stored_hash, "secret")
        True
        >>> check_password_hash(stored_hash, "wrong")
        False
    """
    return check_password_hash(password_hash, password)


def create_token(id, key, token_lifetime, is_access):
    """
    Генерирует JWT-токены.

    Args:
        id (int): Id пользователя
        secret (str): Секретный ключ
        token_lifetime (int): Время жизни токена
        is_acess (bool): True, если это access-токен

    Returns:
        token: Сгенерерированный токен

    Example:
        >>> refresh = create_token(123, "key", timedelta(days=7), False)
        >>> access = create_token(123, "key", timedelta(minutes=15), True)
    """
    return jwt.encode(
        {
            "sub": str(id),
            "id": id,
            "is_access": is_access,
            "exp": datetime.utcnow() + token_lifetime,
        },
        key,
        algorithm="HS256",
    )


def get_access_token(token, refresh_key, access_key):
    """
    Создает access-токен

    Args:
        token (refresh_roken): Refresh-токен
        refresh_key (str): Секретный ключ для refresh
        access_key (str): Секретный ключ для access

    Returns:
        token: Сгенерерированный access-токен

    Example:
        >>> access = get_access_token("token", "r_key", "a_key")
    """
    data = jwt.decode(token, refresh_key, algorithms=["HS256"])
    if data["is_access"]:
        return None
    else:
        return create_token(data["id"], access_key, timedelta(minutes=15), True)


def get_user_id(token, access_key):
    """
    Получает id из токена

    Args:
        token (access_roken): Access-токен
        access_key (str): Секретный ключ для access

    Returns:
        int: Id пользователя

    Example:
        >>> access = get_user_id("token", "a_key")
        >>> 123
    """
    try:
        data = jwt.decode(token, access_key, algorithms=["HS256"])
        return data["id"]
    except:
        return None


def create_user(email, name, password):
    """
    Создает пользователя.

    Args:
        email (str): Email
        name (str): Имя пользователя
        password (str): пароль

    Returns:
        User: Созданного пользователя
        ErrorCodes: Код ошибки

    Example:
        >>> user, error = create_user("user@mail.ru, "username", "password")
    """
    if email is None or password is None or name is None:
        return None, ErrorCodes.INCORRECT_USER_DATA

    user = user_repo.get_by_email(email)
    if user is None:
        return user_repo.insert_user(email, get_password_hash(password), name), ErrorCodes.OK
    else:
        return None, ErrorCodes.EMAIL_EXISTS


def get_user(email, password):
    """
    Получает пользователя.

    Args:
        email (str): Email
        password (str): пароль

    Returns:
        User: Пользователь
        ErrorCodes: Код ошибки

    Example:
        >>> user, error = get_user("user@mail.ru, "password")
    """
    if password is None:
        return None, ErrorCodes.INVALID_PASSWORD

    if email is None:
        return None, ErrorCodes.INCORRECT_USER_DATA

    user = user_repo.get_by_email(email)
    if not user:
        return None, ErrorCodes.USER_NOT_FOUND

    if check_password_with_hash(user.password_hash, password):
        return user, ErrorCodes.OK
    else:
        return None, ErrorCodes.INVALID_PASSWORD
