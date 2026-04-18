from flask_sqlalchemy import SQLAlchemy
import pytest
from app import create_app, db
from app.shared.features.languages.service import *
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


def test_get_all_langs(app_context):
    result = get_all_langs()

    ideal_langs = db.session.query(Language).all()
    expected = [
        {"id": l.id, "name": l.name, "description": l.description} for l in ideal_langs
    ]

    assert expected == result


@pytest.mark.parametrize(
    "id, result",
    [(4, True), (677, False)],
)
def test_get_lang(id, result, app_context):
    is_exists = lang_exists(id)

    assert is_exists == result
