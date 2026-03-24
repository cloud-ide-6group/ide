from werkzeug.security import generate_password_hash, check_password_hash
import jwt
from .repository import user_repo
from app.shared.consts import ResultsCodes


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
        return None, ResultsCodes.INCORRECT_USER_DATA

    user = user_repo.get_by_email(email)
    if user is None:
        try:
            return (
                user_repo.insert_user(email, get_password_hash(password), name),
                ResultsCodes.OK,
            )
        except ValueError as e:
            return None, str(e)
    else:
        return None, ResultsCodes.EMAIL_EXISTS


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
        return None, ResultsCodes.INVALID_PASSWORD

    if email is None:
        return None, ResultsCodes.INCORRECT_USER_DATA

    user = user_repo.get_by_email(email)
    if not user:
        return None, ResultsCodes.USER_NOT_FOUND

    if check_password_with_hash(user.password_hash, password):
        return user, ResultsCodes.OK
    else:
        return None, ResultsCodes.INVALID_PASSWORD
