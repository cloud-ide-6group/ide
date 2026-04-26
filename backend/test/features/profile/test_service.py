from flask_sqlalchemy import SQLAlchemy
import pytest
from app import create_app, db
from app.features.profile.service import get_user_data, update_user_data
from app.shared.dbmodels import *
from config import DBTestConfig
from werkzeug.security import generate_password_hash


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
    "id, result, email, name, photo_path",
    [
        (1, ResultsCodes.OK, "user@mail.ru", "test_user", "users_imgs/default.png"),
        (1245, ResultsCodes.USER_NOT_FOUND, None, None, None),
        (None, ResultsCodes.USER_ID_NULL, None, None, None),
    ],
)
def test_get_user_data(id, result, email, name, photo_path, app_context):
    """Тест получения данных пользователя"""
    user, user_result = get_user_data(id)

    assert user_result == result
    if result == ResultsCodes.OK:
        assert user.email == email
        assert user.photo_path == photo_path
        assert user.name == name
    else:
        assert user == None


@pytest.mark.parametrize(
    "old_name, old_email, email, name, expected_code",
    [
        ("upd3", "upd3@mail.ru", "userupd3@mail.ru", "test_user3", ResultsCodes.OK),
        (
            "upd2",
            "upd2@mail.ru",
            "invalid-email",
            "test_user2",
            ResultsCodes.INVALID_EMAIL,
        ),
    ],
)
def test_update_user_data(old_name, old_email, email, name, expected_code, app_context):
    user = User(
        name=old_name,
        email=old_email,
        password_hash="old_hash",
        photo_path="old_path",
    )
    db.session.add(user)
    db.session.commit()

    id = db.session.query(User).filter(User.email == old_email).first().id

    result, code = update_user_data(id, email, name, "new_hash", "new_path")

    assert code == expected_code

    if expected_code == ResultsCodes.OK:
        assert result is not None
        assert result.email == email
    else:
        assert result is None

    deleted_user = db.session.get(User, id)
    db.session.delete(deleted_user)
    db.session.commit()
