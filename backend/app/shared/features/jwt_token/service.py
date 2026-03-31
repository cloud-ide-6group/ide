from datetime import datetime, timedelta

import jwt
from ...consts import ResultsCodes
import os
from dotenv import load_dotenv

load_dotenv()


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


def get_access_refresh_tokens(token, refresh_key, access_key):
    """
    Создает access-токен. Используется refresh token rotation(новый refresh каждый раз)

    Args:
        token (refresh_token): Refresh-токен
        refresh_key (str): Секретный ключ для refresh
        access_key (str): Секретный ключ для access

    Returns:
        access: Сгенерерированный access-токен
        refresh: Сгенерерированный refresh-токен

    Example:
        >>> access = get_access_token("token", "r_key", "a_key")
    """
    data = jwt.decode(token, refresh_key, algorithms=["HS256"])
    if data["is_access"]:
        return {
            "result": ResultsCodes.REFRESH_TOKEN_NEEDED,
        }
    else:
        access = create_token(data["id"], access_key, timedelta(minutes=15), True)
        refresh = create_token(data["id"], refresh_key, timedelta(days=7), False)
        return {"access": access, "refresh": refresh, "result": ResultsCodes.OK}


def get_id(token):
    """
    Получает id из access токена.

    Args:
        token (access_token): Access-токен

    Returns:
        user_id (int): Id пользователя
        result_code (ResultCodes): Результат выполнения

    Example:
        >>> id, result = get_access_token("token")
    """
    ACCESS_SECRET = os.getenv("ACCESS", "UMLFphza4e")

    try:
        decoded_token = jwt.decode(token, ACCESS_SECRET, algorithms=["HS256"])
        user_id = decoded_token.get("id")
        return user_id, ResultsCodes.OK
    except jwt.ExpiredSignatureError:
        return None, ResultsCodes.ACCESS_TOKEN_EXPIRED
