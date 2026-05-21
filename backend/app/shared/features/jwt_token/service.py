from datetime import datetime, timedelta

import jwt
from ...consts import ResultsCodes
import os
from dotenv import load_dotenv
from flask import make_response

load_dotenv()

DAYS = 20
HOURS = 24
MINS = 60
SECS = 60

ACCESS_KEY = os.getenv("ACCESS")
REFRESH_KEY = os.getenv("REFRESH")


def create_token(id, token_lifetime, is_access):
    """
    Генерирует JWT-токены.

    Args:
        id (int): Id пользователя
        token_lifetime (int): Время жизни токена
        is_acess (bool): True, если это access-токен

    Returns:
        token: Сгенерерированный токен

    Example:
        >>> refresh = create_token(123, "key", timedelta(days=7), False)
        >>> access = create_token(123, "key", timedelta(minutes=15), True)
    """
    key = REFRESH_KEY
    if is_access:
        key = ACCESS_KEY

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


def get_access_refresh_tokens(token):
    """
    Создает access-токен. Используется refresh token rotation(новый refresh каждый раз)

    Args:
        token (refresh_token): Refresh-токен

    Returns:
        access: Сгенерерированный access-токен
        refresh: Сгенерерированный refresh-токен

    Example:
        >>> access = get_access_token("token", "r_key", "a_key")
    """
    from app.shared.extensions import redis_client

    if redis_client.get(token):
        return {"result": ResultsCodes.REFRESH_TOKEN_EXPIRED}

    data = jwt.decode(token, REFRESH_KEY, algorithms=["HS256"])
    if data["is_access"]:
        return {
            "result": ResultsCodes.REFRESH_TOKEN_NEEDED,
        }
    else:
        access = create_token(data["id"], timedelta(minutes=15), True)
        refresh = create_token(data["id"], timedelta(days=7), False)
        redis_client.setex(token, DAYS * HOURS * MINS * SECS, "used")
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


def get_jwt_from_header(auth_header):
    """
    Получает jwt токен из заголовка запроса.

    Args:
        auth_header (str): Заголовок

    Returns:
        access_token (str): Jwt токен или None
        result_code (ResultCodes): Результат выполнения

    Example:
        >>> auth_header = request.headers.get("Authorization")
        >>> token, result = get_jwt_from_header(auth_header)
    """
    if not auth_header or not auth_header.startswith("Bearer "):
        return None, ResultsCodes.NO_TOKEN

    access_token = auth_header.split(" ")[1]

    is_token_valid = validate_jwt(access_token, os.getenv("ACCESS"))
    if is_token_valid == False:
        return None, ResultsCodes.INVALID_TOKEN

    return access_token, ResultsCodes.OK


def create_unauthorized_response(result):
    """
    Создает стандартный ответ для отсутствия токена

    Returns:
        response (json): Json ответ
    """
    response = make_response({"message": result}, 401)
    response.headers["WWW-Authenticate"] = "Bearer"
    return response


def validate_jwt(token, secret_key):
    """
    Валидирует токен

    Args:
        token (str): Токен
        secret_key(str): Ключ

    Returns:
        bool: Валиден или нет
    """
    try:
        jwt.decode(token, secret_key, algorithms=["HS256"])
        return True
    except:
        return False
