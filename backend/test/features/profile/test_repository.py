from flask_sqlalchemy import SQLAlchemy
import pytest
from app import create_app, db
from app.features.profile.repository import user_repo
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


def test_get_all(app_context):
    repo_users = user_repo.get_all()
    db_users = db.session.query(User).all()

    assert len(repo_users) == len(db_users)
    assert {u.id for u in repo_users} == {u.id for u in db_users}


def test_get_by_id(app_context):
    user = user_repo.get_all()[0]
    test_user = db.session.query(User).filter(User.id == user.id).first()

    assert user == test_user
