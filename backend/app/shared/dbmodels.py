from .extensions import db
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.orm import validates
from .consts import ResultsCodes
from email_validator import validate_email, EmailNotValidError


class Project(db.Model):
    """Модель проекта в IDE.

    Attributes:
        id: Уникальный идентификатор проекта
        name: Название проекта. Должно быть уникальным в системе
        owner_id: ID пользователя-владельца (внешний ключ к user.id)
        language_id: ID языка программирования проекта

    Example:
        >>> project = Project(
        ...     name="MyFirstProject",
        ...     owner_id=1,
        ...     language_id=3
        ... )
    """

    __tablename__ = "project"

    id = db.Column(db.Integer, primary_key=True)
    """int: Уникальный идентификатор проекта (первичный ключ)"""

    name = db.Column(db.Text, unique=True, nullable=False)
    """str: Уникальное название проекта. Обязательное поле."""

    owner_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    """int: ID владельца проекта. Внешний ключ к таблице user."""

    language_id = db.Column(db.Integer, nullable=False)
    """int: ID языка программирования. Обязательное поле."""


class File(db.Model):
    """Модель файла или папки в проекте.

    Attributes:
        id: Уникальный идентификатор файла
        name: Имя файла или папки
        parent_id: ID родительской папки (null если в корне)
        project_id: ID проекта-владельца
        is_folder: True = папка, False = файл

    Example:
        >>> file = File(
        ...     name="file5.txt",
        ...     parent_id=1,
        ...     project_id=3,
        ...     is_folder=false
        ... )
    """

    __tablename__ = "file"

    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.Text, nullable=False)
    parent_id = db.Column(db.Integer, db.ForeignKey("file.id"), nullable=True)
    project_id = db.Column(db.Integer, db.ForeignKey("project.id"), nullable=False)
    is_folder = db.Column(db.Boolean, nullable=False)


class Language(db.Model):
    """Модель языка программирования.

    Attributes:
        id: Уникальный идентификатор языка
        name: Имя языка
        description: Описание языка
        image_name: Docker образ для создания контейнера

    Example:
        >>> lang = Language(
        ...     name="Java 17",
        ...     parent_id=1,
        ...     description="Java 17 and maven",
        ...     image_name="java-17"
        ... )
    """

    __tablename__ = "language"

    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.Text)
    description = db.Column(db.Text)
    image_name = db.Column(db.Text)


class Chat(db.Model):
    """Модель чата. Может быть в разных файлах, но в одном проекте.

    Attributes:
        id: Уникальный идентификатор
        author_id: ID создавшего участника
        project_id: ID проекта-владельца

    Example:
        >>> chat = Chat(
        ...     author_id=1,
        ...     project_id=10,
        ... )
    """

    __tablename__ = "chat"

    id = db.Column(db.Integer, primary_key=True)
    author_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    project_id = db.Column(db.Integer, db.ForeignKey("project.id"), nullable=False)


class Message(db.Model):
    """Модель сообщения в чате.

    Attributes:
        id: Уникальный идентификатор
        text: Текст сообщения
        author_id: ID создавшего участника
        chat_id: ID чата-владельца
        send_time: Время и дата отправки

    Example:
        >>> message = Message(
        ...     text="message 1",
        ...     author_id=10,
        ...     chat_id=7,
        ...     send_time=""
        ... )
    """

    __tablename__ = "message"

    id = db.Column(db.Integer, primary_key=True)
    text = db.Column(db.Text)
    author_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    chat_id = db.Column(db.Integer, db.ForeignKey("chat.id"), nullable=False)
    send_time = db.Column(db.DateTime, nullable=False)


class User(db.Model):
    """Модель пользователя.

    Attributes:
        id: Уникальный идентификатор
        name: Имя пользователя
        photo_path: Путь к фотографии профиля
        password_hash: Хэш пароля
        email: Почта-логин

    Example:
        >>> user = User(
        ...     name="user_name",
        ...     photo_path="path/to/photo.png",
        ...     password_hash="scrypt:32768:8:1$cLJQcYflEEer26Ri$9fc",
        ...     email="email@mail.ru"
        ... )
    """

    __tablename__ = "user"

    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.Text, nullable=False)
    photo_path = db.Column(db.Text, default="users_imgs/default.png")
    password_hash = db.Column(db.Text, nullable=False)
    email = db.Column(db.Text, nullable=False, unique=True)

    @validates("name")
    def validate_name(self, key, name):
        if not name or name == "":
            raise ValueError(ResultsCodes.INCORRECT_USER_NAME)

        return name

    @validates("email")
    def validate_email(self, key, email):
        if not email:
            raise ValueError(ResultsCodes.INVALID_EMAIL)

        try:
            valid = validate_email(email)
            return valid.normalized
        except EmailNotValidError as e:
            raise ValueError(ResultsCodes.INVALID_EMAIL) from e


class UserInProject(db.Model):
    """Таблица связи пользователя и проекта для реализации связи многие-ко-многим.

    Attributes:
        id: Уникальный идентификатор
        project_id: ID проекта
        user_id: ID пользователя

    Example:
        >>> user_in_project = UserInProject(
        ...     project_id=12,
        ...     user_id=15
        ... )
    """

    __tablename__ = "user_in_project"

    id = db.Column(db.Integer, primary_key=True)
    project_id = db.Column(db.Integer, db.ForeignKey("project.id"))
    user_id = db.Column(db.Integer, db.ForeignKey("user.id"))


class Notification(db.Model):
    """Модель уведомлений.

    Attributes:
        id: Уникальный идентификатор
        project_id: ID проекта
        receiver_id: ID пользователя, которому отправлено уведомление
        sender_id: ID отправителя
        send_time: Дата и время отправки уведомления

    Example:
        >>> notification = Notification(
        ...     project_id=12,
        ...     receiver_id=15,
        ...     sender_id=17
        ...     send_time=""
        ... )
    """

    __tablename__ = "notification"

    id = db.Column(db.Integer, primary_key=True)
    project_id = db.Column(db.Integer, db.ForeignKey("project.id"), nullable=False)
    receiver_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    sender_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    send_time = db.Column(db.DateTime, nullable=False)
