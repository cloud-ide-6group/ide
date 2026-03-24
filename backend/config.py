import os
from dotenv import load_dotenv

load_dotenv()


class DebugConfig:
    SECRET_KEY = os.getenv("SECRET_KEY", "default-secret-key")
    FLASK_ENV = os.getenv("FLASK_ENV", "development")
    DEBUG = os.getenv("FLASK_DEBUG", "1") == "1"
    DB_TEST = False
    SQLALCHEMY_DATABASE_URI = os.getenv("DATABASE_URL")
    SWAGGER_URL_PREFIX = f"/docs-{os.getenv('DOCS_SECRET', 'S7mMg7yT1BLhAb')}"
    SWAGGER_SECRET_TOKEN = os.getenv("SWAGGER_PASSWORD", "ZWp0FY39JZaK0P")
    SWAGGER_SECRET_USER = os.getenv("SWAGGER_USER", "GhptWyANiUq3e8")
    ACCESS = os.getenv("ACCESS", "UMLFphza4e")
    REFRESH = os.getenv("REFRESH", "iZdMl8QF0X")


class DBTestConfig:
    SECRET_KEY = os.getenv("SECRET_KEY", "default-secret-key")
    FLASK_ENV = os.getenv("FLASK_ENV", "development")
    DEBUG = os.getenv("FLASK_DEBUG", "1") == "1"
    DB_TEST = True
    SQLALCHEMY_DATABASE_URI = os.getenv("TEST_DATABASE_URL")
    SWAGGER_URL_PREFIX = None
    SWAGGER_SECRET_TOKEN = None
    SWAGGER_SECRET_USER = None
    ACCESS = os.getenv("ACCESS", "UMLFphza4e")
    REFRESH = os.getenv("REFRESH", "iZdMl8QF0X")


config = {"debug": DebugConfig, "dbtest": DBTestConfig}
