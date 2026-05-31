from flask_sqlalchemy import SQLAlchemy
import pytest
from app import create_app, db
from app.features.chat.service import (
    create_chat,
    delete_chat,
    send_message,
    get_messages,
    get_chat_project_id,
    get_chats,
)
from app.shared.dbmodels import *
from config import DBTestConfig


@pytest.fixture(scope="session")
def app():
    """Создает тестовое приложение"""
    app = create_app(DBTestConfig)
    return app


@pytest.fixture(scope="session")
def client(app):
    """Тестовый клиент"""
    return app.test_client()


@pytest.fixture(scope="session")
def app_context(app):
    """Контекст приложения для тестов"""
    with app.app_context():
        yield


PROJECT_NAME = "ChatTest"
OWNER_EMAIL = "chat_owner@mail.ru"
USER_EMAIL = "chat_user@mail.ru"
CHAT_IDENTIFICATOR = "test123"


@pytest.fixture(scope="session", autouse=True)
def global_setup_teardown(app_context):
    existing_project = (
        db.session.query(Project).filter(Project.name == PROJECT_NAME).first()
    )
    if existing_project:
        db.session.delete(existing_project)

    existing_users = (
        db.session.query(User).filter(User.email.in_([OWNER_EMAIL, USER_EMAIL])).all()
    )
    for user in existing_users:
        db.session.delete(user)

    db.session.commit()

    user1 = User(name="owner", password_hash="fff", photo_path="888", email=OWNER_EMAIL)
    user2 = User(name="user", password_hash="fff", photo_path="888", email=USER_EMAIL)
    db.session.add(user1)
    db.session.add(user2)
    db.session.commit()

    owner = db.session.query(User).filter(User.email == OWNER_EMAIL).first()
    project = Project(name=PROJECT_NAME, owner_id=owner.id, language_id=1)
    db.session.add(project)
    db.session.commit()

    owner_id = owner.id
    user_id = db.session.query(User).filter(User.email == USER_EMAIL).first().id
    project_id = project.id

    yield

    db.session.delete(db.session.get(User, owner_id))
    db.session.delete(db.session.get(User, user_id))
    db.session.delete(db.session.get(Project, project_id))
    db.session.commit()


@pytest.mark.parametrize(
    "project_name, user_email, identificator, assert_result",
    [
        (PROJECT_NAME, OWNER_EMAIL, CHAT_IDENTIFICATOR, ResultsCodes.OK),
        (
            PROJECT_NAME,
            OWNER_EMAIL,
            CHAT_IDENTIFICATOR,
            ResultsCodes.OK,
        ),
        (
            PROJECT_NAME,
            USER_EMAIL,
            CHAT_IDENTIFICATOR,
            ResultsCodes.USER_IS_NOT_IN_PROJECT,
        ),
        (
            "NoProject",
            OWNER_EMAIL,
            CHAT_IDENTIFICATOR,
            ResultsCodes.USER_IS_NOT_IN_PROJECT,
        ),
    ],
)
def test_create_chat(
    project_name, user_email, identificator, assert_result, app_context
):
    user = db.session.query(User).filter(User.email == user_email).first()
    project = db.session.query(Project).filter(Project.name == project_name).first()

    if user and project:
        result = create_chat(project.id, user.id, identificator)
        if result[1] == ResultsCodes.OK:
            from app.features.chat.repository import chat_repo

            chat_repo.delete_chat(result[0].id)
        assert result[1] == assert_result
    else:
        assert assert_result == ResultsCodes.USER_IS_NOT_IN_PROJECT


@pytest.mark.parametrize(
    "user_email, assert_result",
    [
        (OWNER_EMAIL, ResultsCodes.OK),
        (USER_EMAIL, ResultsCodes.USER_IS_NOT_CHAT_CREATOR),
        ("wrong@mail.ru", ResultsCodes.CHAT_NOT_FOUND),
    ],
)
def test_delete_chat(user_email, assert_result, app_context):
    owner = db.session.query(User).filter(User.email == OWNER_EMAIL).first()
    user = (
        db.session.query(User).filter(User.email == user_email).first()
        if user_email != "wrong@mail.ru"
        else None
    )
    project = db.session.query(Project).filter(Project.name == PROJECT_NAME).first()

    chat, _ = create_chat(project.id, owner.id, CHAT_IDENTIFICATOR)

    if user:
        result = delete_chat(chat.id, user.id)
        assert result[1] == assert_result
        if result[1] != ResultsCodes.OK:
            from app.features.chat.repository import chat_repo

            chat_repo.delete_chat(chat.id)
    else:
        result = delete_chat(99999, 99999)
        assert result[1] == assert_result
        from app.features.chat.repository import chat_repo

        chat_repo.delete_chat(chat.id)


@pytest.mark.parametrize(
    "user_email, assert_result",
    [
        (OWNER_EMAIL, ResultsCodes.OK),
        (USER_EMAIL, ResultsCodes.USER_IS_NOT_IN_PROJECT),
        ("wrong@mail.ru", ResultsCodes.CHAT_NOT_FOUND),
    ],
)
def test_send_message(user_email, assert_result, app_context):
    owner = db.session.query(User).filter(User.email == OWNER_EMAIL).first()
    user = (
        db.session.query(User).filter(User.email == user_email).first()
        if user_email != "wrong@mail.ru"
        else None
    )
    project = db.session.query(Project).filter(Project.name == PROJECT_NAME).first()

    chat, _ = create_chat(project.id, owner.id, CHAT_IDENTIFICATOR)

    if user:
        result = send_message(chat.id, "test message", user.id)
        assert result == assert_result
    else:
        result = send_message(99999, "test message", 99999)
        assert result == assert_result

    from app.features.chat.repository import chat_repo

    chat_repo.delete_chat(chat.id)


@pytest.mark.parametrize(
    "chat_exists, assert_result",
    [
        (True, ResultsCodes.OK),
        (False, ResultsCodes.OK),
    ],
)
def test_get_messages(chat_exists, assert_result, app_context):
    owner = db.session.query(User).filter(User.email == OWNER_EMAIL).first()
    project = db.session.query(Project).filter(Project.name == PROJECT_NAME).first()

    if chat_exists:
        chat, _ = create_chat(project.id, owner.id, CHAT_IDENTIFICATOR)
        messages, result = get_messages(chat.id)
        assert result == assert_result
        from app.features.chat.repository import chat_repo

        chat_repo.delete_chat(chat.id)
    else:
        messages, result = get_messages(99999)
        assert result == assert_result


@pytest.mark.parametrize(
    "chat_exists, assert_result",
    [
        (True, ResultsCodes.OK),
        (False, ResultsCodes.CHAT_NOT_FOUND),
    ],
)
def test_get_chat_project_id(chat_exists, assert_result, app_context):
    owner = db.session.query(User).filter(User.email == OWNER_EMAIL).first()
    project = db.session.query(Project).filter(Project.name == PROJECT_NAME).first()

    if chat_exists:
        chat, _ = create_chat(project.id, owner.id, CHAT_IDENTIFICATOR)
        project_id, result = get_chat_project_id(chat.id)
        assert result == assert_result
        from app.features.chat.repository import chat_repo

        chat_repo.delete_chat(chat.id)
    else:
        project_id, result = get_chat_project_id(99999)
        assert result == assert_result


@pytest.mark.parametrize(
    "project_exists, assert_result",
    [
        (True, ResultsCodes.OK),
        (False, ResultsCodes.OK),
    ],
)
def test_get_chats(project_exists, assert_result, app_context):
    project = db.session.query(Project).filter(Project.name == PROJECT_NAME).first()

    if project_exists:
        chats, result = get_chats(project.id)
        assert result == assert_result
    else:
        chats, result = get_chats(99999)
        assert result == assert_result
