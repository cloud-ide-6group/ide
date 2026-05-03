from flask_sqlalchemy import SQLAlchemy
import pytest
from app import create_app, db
from app.features.invitation.service import (
    add_user_in_project,
    delete_user_from_project,
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


PROJECT_NAME = "InviteTest"
INVITE_EMAIL = "invite_user@mail.ru"
OWNER_EMAIL = "invite_owner@mail.ru"


@pytest.fixture(scope="session", autouse=True)
def global_setup_teardown(app_context):
    existing_project = (
        db.session.query(Project).filter(Project.name == PROJECT_NAME).first()
    )
    if existing_project:
        db.session.delete(existing_project)

    existing_users = (
        db.session.query(User).filter(User.email.in_([INVITE_EMAIL, OWNER_EMAIL])).all()
    )
    for user in existing_users:
        db.session.delete(user)

    db.session.commit()

    user1 = User(
        name="invite_owner",
        password_hash="fff",
        photo_path="888",
        email=INVITE_EMAIL,
    )
    db.session.add(user1)
    user2 = User(
        name="invite_user",
        password_hash="fff",
        photo_path="888",
        email=OWNER_EMAIL,
    )
    db.session.add(user2)
    owner = db.session.query(User).filter(User.email == OWNER_EMAIL).first()
    invite = db.session.query(User).filter(User.email == INVITE_EMAIL).first()
    project = Project(name=PROJECT_NAME, owner_id=owner.id, language_id=1)
    db.session.add(project)
    db.session.commit()

    user1_id = owner.id
    user2_id = invite.id
    project_id = (
        db.session.query(Project).filter(Project.name == PROJECT_NAME).first().id
    )

    yield

    db.session.delete(db.session.get(User, user1_id))
    db.session.delete(db.session.get(User, user2_id))
    db.session.delete(db.session.get(Project, project_id))
    db.session.commit()


@pytest.mark.parametrize(
    "project_name, invited_user_email, owner_email, assert_result",
    [
        (PROJECT_NAME, INVITE_EMAIL, OWNER_EMAIL, ResultsCodes.OK),
        (PROJECT_NAME, "aaa@mail.ru", OWNER_EMAIL, ResultsCodes.USER_NOT_FOUND),
        (PROJECT_NAME, INVITE_EMAIL, INVITE_EMAIL, ResultsCodes.PROJECT_NOT_FOUND),
        ("aProject", INVITE_EMAIL, OWNER_EMAIL, ResultsCodes.PROJECT_NOT_FOUND),
    ],
)
def test_create_project(
    project_name, invited_user_email, owner_email, assert_result, app_context
):
    owner_id = db.session.query(User).filter(User.email == owner_email).first().id
    result = add_user_in_project(project_name, invited_user_email, owner_id)

    assert assert_result == result

    if result == ResultsCodes.OK:
        delete_user_from_project(project_name, invited_user_email, owner_id)
