import pytest
from app import create_app
from app.shared.extensions import db


@pytest.fixture
def app():
    """Создает тестовое приложение"""
    app = create_app()

    app.config["SWAGGER_SECRET_TOKEN"] = None

    with app.app_context():
        db.create_all()

    yield app

    with app.app_context():
        db.drop_all()


@pytest.fixture
def client(app):
    """Тестовый клиент для отправки запросов"""
    return app.test_client()


@pytest.fixture
def runner(app):
    """Тестовый клиент для команд CLI"""
    return app.test_cli_runner()
