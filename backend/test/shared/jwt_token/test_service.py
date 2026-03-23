from datetime import timedelta

import jwt
import pytest
from sqlalchemy import text
from werkzeug.security import generate_password_hash, check_password_hash
from app import create_app
from app.shared.dbmodels import User
from app.shared.consts import ResultsCodes
from app.shared.features.jwt_token.service import create_token, get_access_refresh_tokens
from config import DBTestConfig


@pytest.fixture
def app():
    """Создает тестовое приложение"""
    app = create_app(DBTestConfig)
    return app


@pytest.fixture
def client(app):
    """Тестовый клиент"""
    return app.test_client()


@pytest.fixture
def app_context(app):
    """Контекст приложения для тестов"""
    with app.app_context():
        yield


@pytest.mark.parametrize(
    "id, key, token_lifetime, is_access",
    [
        (123, "55thu8", timedelta(minutes=5), True),
        (345, "55thu8", timedelta(minutes=10), False),
    ],
)
def test_create_token(id, key, token_lifetime, is_access):
    token = create_token(id, key, token_lifetime, is_access)

    data = jwt.decode(token, key, algorithms=["HS256"])

    assert data["is_access"] == is_access
    assert data["id"] == id


def test_get_access_token():
    """
    Тест создания access токенов
    """
    REFRESH_KEY = "123"
    ACCESS_KEY = "567"

    token = create_token(123, REFRESH_KEY, timedelta(minutes=5), False)
    result = get_access_refresh_tokens(token, REFRESH_KEY, ACCESS_KEY)
    access = result["access"]
    refresh = result["refresh"]
    data = jwt.decode(access, ACCESS_KEY, algorithms=["HS256"])

    assert data["is_access"] == True
    assert refresh != None
    assert data["id"] == 123
