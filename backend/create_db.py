from sqlalchemy import create_engine, text
from app.shared.dbmodels import *
from config import DebugConfig, DBTestConfig
import os
from dotenv import load_dotenv
from sqlalchemy.orm import sessionmaker
from werkzeug.security import generate_password_hash

load_dotenv()

WORK_URI = DebugConfig.SQLALCHEMY_DATABASE_URI
TEST_URI = DBTestConfig.SQLALCHEMY_DATABASE_URI


def created_db():
    """
    Создает рабочую БД
    """
    dblink = os.getenv("DB_PASSWORD")
    password = os.getenv("DB_LINK")
    engine = create_engine(f"postgresql://baseuser:{password}@{dblink}:5432/postgres")
    with engine.connect() as conn:
        conn.execute(text("COMMIT"))

        result = conn.execute(
            text("SELECT 1 FROM pg_database WHERE datname='cloudidedb'")
        )
        exists = result.scalar() is not None

        if not exists:
            conn.execute(text("CREATE DATABASE cloudidedb"))
        else:
            print("Database exists already")

    engine = create_engine(DebugConfig.SQLALCHEMY_DATABASE_URI)

    db.metadata.create_all(engine)
    print("Tables created")


def create_tables(uri):
    """
    Создает таблицы
    """
    engine = create_engine(uri)
    db.metadata.create_all(engine)
    print("Tables created")


def create_languages(uri):
    """
    Добавляет языки
    """
    engine = create_engine(uri)

    Session = sessionmaker(bind=engine)
    session = Session()

    initial_languages = [
        "Python",
        "Empty Python",
        "JavaScript",
        "Java 17",
        "Java 21",
        "Lua",
    ]
    initial_images = [
        "python-essentials",
        "python-test-img",
        "js-essential",
        "java-17",
        "java-21",
        "lua-essentials",
    ]

    try:
        i = 0
        for lang_name in initial_languages:
            existing = session.query(Language).filter_by(name=lang_name).first()
            if not existing:
                language = Language(
                    name=lang_name, description="", image_name=initial_images[i]
                )
                i = i + 1
                session.add(language)
                print(f"Added language: {lang_name}")
            else:
                print(f"Language already exists: {lang_name}")

        session.commit()
        print("All languages created successfully!")

    except Exception as e:
        session.rollback()
        print(f"Error adding languages: {e}")
    finally:
        session.close()


def create_users(uri):
    """
    Добавляет пользователей
    """
    engine = create_engine(uri)

    Session = sessionmaker(bind=engine)
    session = Session()

    users_names = ["test_user"]
    users_emails = ["user@mail.ru"]
    users_pass = ["test_password"]

    try:
        for i in range(len(users_names)):
            user = User(
                name=users_names[i],
                email=users_emails[i],
                password_hash=generate_password_hash(users_pass[i]),
            )
            session.add(user)

        session.commit()

    except Exception as e:
        session.rollback()
        raise e


create_users(TEST_URI)
