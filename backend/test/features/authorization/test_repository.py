from flask_sqlalchemy import SQLAlchemy
import pytest
from app import create_app, db
from app.features.authorization.repository import user_repo
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

# TODO: fix this test
def test_get_by_email(app_context):
    user = user_repo.get_all()[0]
    test_user = db.session.query(User).filter(User.email == user.email).first()

    assert user == test_user


# TODO: fix this test
def test_insert_user(app_context):
    password = "Afdgh"
    name = "the_user"
    email = "test_insert@mail.ru"

    user = user_repo.get_by_email(email)
    assert user == None

    hash = generate_password_hash(password)
    user_repo.insert_user(email, hash, name)

    user = user_repo.get_by_email(email)

    assert user != None
    assert user.email == email
    assert user.name == name
    assert user.password_hash == hash
