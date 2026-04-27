from werkzeug.security import generate_password_hash, check_password_hash


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
