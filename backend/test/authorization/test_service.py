import pytest
from sqlalchemy import text
from app.features.authorization.service import *
from werkzeug.security import generate_password_hash, check_password_hash
from app import create_app
from app.shared.dbmodels import User
from app.shared.consts import ResultsCodes
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
    "password",
    [
        "simple",
        "complex!@#$%",
        "very_long_password_" * 10,
        "12345",
        " with spaces ",
        "русский_пароль",
        "admin",
        "password123",
    ],
)
def test_password_hashing(password):
    """Тест хеширования пароля"""

    hashed = get_password_hash(password)

    assert check_password_hash(hashed, password) is True


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


def test_password_checking():
    """Тест проверки пароля"""

    password = "test"
    hashed = get_password_hash(password)
    correct = check_password_with_hash(hashed, password)
    assert correct == True
    wrong = check_password_with_hash(hashed, "not test")
    assert wrong == False


def test_get_access_token():
    """
    Тест создания access токенов
    """
    REFRESH_KEY = "123"
    ACCESS_KEY = "567"

    token = create_token(123, REFRESH_KEY, timedelta(minutes=5), False)
    access = get_access_refresh_tokens(token, REFRESH_KEY, ACCESS_KEY)
    data = jwt.decode(access, ACCESS_KEY, algorithms=["HS256"])

    assert data["is_access"] == True
    assert data["id"] == 123


@pytest.mark.parametrize(
    "email, password, name, expected_result",
    [
        ("user1@mail.ru", "test_password", "user_name1", "user1@mail.ru"),
        ("user1@mail.ru", "test_pass", "user_na", ResultsCodes.EMAIL_EXISTS),
        ("user@mail.ru", None, None, ResultsCodes.INCORRECT_USER_DATA),
        (None, "test_password", None, ResultsCodes.INCORRECT_USER_DATA),
        ("pochta@mail.ru", "test_password", None, ResultsCodes.INCORRECT_USER_DATA),
    ],
)
def test_create_user(email, password, name, expected_result, app_context):
    """Тест проверки создания пользователя"""

    result, error = create_user(email, name, password)
    if result:
        assert result.email == expected_result
    else:
        assert error == expected_result


@pytest.mark.parametrize(
    "email, password, expected_result",
    [
        ("user@mail.ru", "test_password", "user@mail.ru"),
        ("user@mail.ru", "anti_password", ResultsCodes.INVALID_PASSWORD),
        ("not_in_db@mail.ru", "not_in_db", ResultsCodes.USER_NOT_FOUND),
        ("user@mail.ru", None, ResultsCodes.INVALID_PASSWORD),
        (None, "test_password", ResultsCodes.INCORRECT_USER_DATA),
        ("", "test_password", ResultsCodes.USER_NOT_FOUND),
    ],
)
def test_get_user(email, password, expected_result, app_context):
    """Тест проверки получаения пользователя"""

    user, error = get_user(email, password)
    if user:
        assert user.email == expected_result
    else:
        assert error == expected_result
