from flask_sqlalchemy import SQLAlchemy
import pytest
from app import create_app, db
from app.features.files.service import (
    create_file,
    delete_file,
    delete_file_from_disk,
    rename_file,
    save_file_content,
    is_user_in_project,
)
from app.shared.dbmodels import *
from config import DBTestConfig
from pathlib import Path
import shutil
import os
from dotenv import load_dotenv

load_dotenv()


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


PROJECT_NAME = "FileTest"
OWNER_EMAIL = "file_owner@mail.ru"
USER_EMAIL = "file_user@mail.ru"
FILE_NAME = "test_file.txt"


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

    file_path = os.path.join(os.getenv("PROJECTS_PATH"), PROJECT_NAME)
    path = Path(file_path)
    if path.exists():
        shutil.rmtree(path)


@pytest.mark.parametrize(
    "project_name, user_email, file_name, parent_id, is_folder, assert_result",
    [
        (PROJECT_NAME, OWNER_EMAIL, FILE_NAME, None, False, ResultsCodes.OK),
        (
            PROJECT_NAME,
            OWNER_EMAIL,
            FILE_NAME,
            None,
            False,
            ResultsCodes.OK,
        ),
        (
            PROJECT_NAME,
            USER_EMAIL,
            FILE_NAME,
            None,
            False,
            ResultsCodes.CANT_CHANGE_FILE,
        ),
        (
            "NoProject",
            OWNER_EMAIL,
            FILE_NAME,
            None,
            False,
            ResultsCodes.PROJECT_NOT_FOUND,
        ),
    ],
)
def test_create_file(
    project_name,
    user_email,
    file_name,
    parent_id,
    is_folder,
    assert_result,
    app_context,
):
    user = db.session.query(User).filter(User.email == user_email).first()
    project = db.session.query(Project).filter(Project.name == project_name).first()

    if user and project:
        result = create_file(file_name, project.id, parent_id, is_folder, user.id)
        if result == ResultsCodes.OK:
            from app.features.files.repository import file_repo

            files = (
                db.session.query(File)
                .filter(File.name == file_name, File.project_id == project.id)
                .all()
            )
            for f in files:
                file_repo.delete_file(f.id)
        assert result == assert_result
    else:
        assert assert_result == ResultsCodes.PROJECT_NOT_FOUND


@pytest.mark.parametrize(
    "user_email, assert_result",
    [
        (OWNER_EMAIL, ResultsCodes.OK),
        (USER_EMAIL, ResultsCodes.CANT_CHANGE_FILE),
        ("wrong@mail.ru", ResultsCodes.FILE_NOT_EXIST),
    ],
)
def test_delete_file(user_email, assert_result, app_context):
    owner = db.session.query(User).filter(User.email == OWNER_EMAIL).first()
    user = (
        db.session.query(User).filter(User.email == user_email).first()
        if user_email != "wrong@mail.ru"
        else None
    )
    project = db.session.query(Project).filter(Project.name == PROJECT_NAME).first()

    result_create = create_file(FILE_NAME, project.id, None, False, owner.id)

    from app.features.files.repository import file_repo

    files = (
        db.session.query(File)
        .filter(File.name == FILE_NAME, File.project_id == project.id)
        .all()
    )

    if files and user:
        result = delete_file(files[0].id, user.id)
        assert result == assert_result
    elif user:
        result = delete_file(99999, user.id)
        assert result == assert_result
    else:
        result = delete_file(99999, 99999)
        assert result == assert_result


@pytest.mark.parametrize(
    "user_email, assert_result",
    [
        (OWNER_EMAIL, ResultsCodes.FILE_ALREADY_EXIST),
        (USER_EMAIL, ResultsCodes.CANT_CHANGE_FILE),
        ("wrong@mail.ru", ResultsCodes.FILE_NOT_EXIST),
    ],
)
def test_rename_file(user_email, assert_result, app_context):
    owner = db.session.query(User).filter(User.email == OWNER_EMAIL).first()
    user = (
        db.session.query(User).filter(User.email == user_email).first()
        if user_email != "wrong@mail.ru"
        else None
    )
    project = db.session.query(Project).filter(Project.name == PROJECT_NAME).first()

    create_file(FILE_NAME, project.id, None, False, owner.id)

    from app.features.files.repository import file_repo

    files = (
        db.session.query(File)
        .filter(File.name == FILE_NAME, File.project_id == project.id)
        .all()
    )

    if files and user:
        result = rename_file(files[0].id, "new_name.txt", user.id)
        assert result == assert_result
        file_repo.delete_file(files[0].id)
    elif user:
        result = rename_file(99999, "new_name.txt", user.id)
        assert result == assert_result
    else:
        result = rename_file(99999, "new_name.txt", 99999)
        assert result == assert_result

    for f in files:
        db.session.delete(f)
        delete_file_from_disk(
            f.name, db.session.get(File, f.parent_id), PROJECT_NAME, f.is_folder
        )
    db.session.commit()


@pytest.mark.parametrize(
    "file_exists, assert_result",
    [
        (True, ResultsCodes.OK),
        (False, ResultsCodes.FILE_NOT_EXIST),
    ],
)
def test_save_file_content(file_exists, assert_result, app_context):
    owner = db.session.query(User).filter(User.email == OWNER_EMAIL).first()
    project = db.session.query(Project).filter(Project.name == PROJECT_NAME).first()

    if file_exists:
        create_file(FILE_NAME, project.id, None, False, owner.id)
        from app.features.files.repository import file_repo

        files = (
            db.session.query(File)
            .filter(File.name == FILE_NAME, File.project_id == project.id)
            .all()
        )
        if files:
            result = save_file_content(files[0].id, "new content")
            assert result == assert_result
            file_repo.delete_file(files[0].id)
    else:
        result = save_file_content(99999, "new content")
        assert result == assert_result


@pytest.mark.parametrize(
    "user_email, project_name, expected",
    [
        (OWNER_EMAIL, PROJECT_NAME, True),
        (USER_EMAIL, PROJECT_NAME, False),
    ],
)
def test_is_user_in_project(user_email, project_name, expected, app_context):
    user = db.session.query(User).filter(User.email == user_email).first()
    project = db.session.query(Project).filter(Project.name == project_name).first()

    if user and project:
        result = is_user_in_project(user.id, project.id)
        assert result == expected
    else:
        assert expected == False
