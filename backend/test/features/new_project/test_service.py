from flask_sqlalchemy import SQLAlchemy
import pytest
from app import create_app, db
from app.features.project.service import create_project
from app.shared.dbmodels import *
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
    "user_id, language_id, project_name, assert_result",
    [
        (1, 2, "TestProj", ResultsCodes.OK),
        (1, 2, "TestProj", ResultsCodes.PROJECT_EXISTS_ALREADY),
        (1, 2, "", ResultsCodes.INCORRECT_NAME),
        (1, 2, None, ResultsCodes.INCORRECT_NAME),
        (1, None, "JProject", ResultsCodes.INCORRECT_LANG),
        (None, 4, "JProject", ResultsCodes.USER_NOT_FOUND),
    ],
)
def test_create_project(user_id, language_id, project_name, assert_result, app_context):
    result = create_project(user_id, project_name, language_id)

    assert assert_result == result
