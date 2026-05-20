from .extensions import db
from sqlalchemy.orm import validates
from .consts import ResultsCodes
import re
from email_validator import validate_email, EmailNotValidError

"""
Модели. id инкрементно ставится БД, при создании его не задаем.
"""


class Project(db.Model):
    """Модель проекта в IDE.

    Attributes:
        id (int): Уникальный идентификатор проекта
        name (str): Название проекта. Должно быть уникальным в системе
        owner_id (int): ID пользователя-владельца (внешний ключ к user.id)
        language_id (int): ID языка программирования проекта

    Example:
        >>> project = Project(
        ...     name="MyFirstProject",
        ...     owner_id=1,
        ...     language_id=3
        ... )
    """

    __tablename__ = "project"

    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.Text, unique=True, nullable=False)
    owner_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    language_id = db.Column(db.Integer, db.ForeignKey("language.id"), nullable=False)

    @validates("name")
    def validate_name(self, key, name):
        if name == None or name == "":
            raise ValueError(ResultsCodes.INCORRECT_NAME)

        name = re.sub(r"\s+", " ", name)

        return name


class File(db.Model):
    """Модель файла или папки в проекте.

    Attributes:
        id (int): Уникальный идентификатор файла
        name (str): Имя файла или папки
        parent_id (int): ID родительской папки (null если в корне) (внешний ключ к File.id)
        project_id (int): ID проекта-владельца
        is_folder (bool): True = папка, False = файл

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
    parent_id = db.Column(
        db.Integer, db.ForeignKey("file.id", ondelete="CASCADE"), nullable=True
    )
    project_id = db.Column(db.Integer, db.ForeignKey("project.id"), nullable=False)
    is_folder = db.Column(db.Boolean, nullable=False)

    children = db.relationship(
        "File",
        backref=db.backref("parent", remote_side=[id]),
        cascade="all, delete-orphan",
    )

    @validates("name")
    def validate_name(self, key, name):
        if name == None or name == "":
            raise ValueError(ResultsCodes.INCORRECT_NAME)

        name = re.sub(r"\s+", " ", name)

        return name


class Language(db.Model):
    """Модель языка программирования.

    Attributes:
        id (int): Уникальный идентификатор языка
        name (str): Имя языка
        description (str): Описание языка
        image_name (str): Docker образ для создания контейнера
        command (str): Команда для запуска кода

    Example:
        >>> lang = Language(
        ...     name="Java 17",
        ...     parent_id=1,
        ...     description="Java 17 and maven",
        ...     image_name="java-17",
        ...     command="run"
        ... )
    """

    __tablename__ = "language"

    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.Text)
    description = db.Column(db.Text)
    image_name = db.Column(db.Text)
    command = db.Column(db.Text)


class Chat(db.Model):
    """Модель чата. Может быть в разных файлах, но в одном проекте.

    Attributes:
        id (int): Уникальный идентификатор
        author_id (int): ID создавшего участника (внешний ключ к User.id)
        project_id (int): ID проекта-владельца

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
        id (int): Уникальный идентификатор
        text (str): Текст сообщения
        author_id (int): ID создавшего участника (внешний ключ к User.id)
        chat_id (int): ID чата-владельца
        send_time (datetime): Время и дата отправки

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
        id (int): Уникальный идентификатор
        name (str): Имя пользователя
        photo_path (str): Путь к фотографии профиля
        password_hash (str): Хэш пароля
        email (str): Почта-логин

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

        name = re.sub(r"\s+", " ", name)

        return name

    @validates("email")
    def validate_email(self, key, email):
        if not email:
            raise ValueError(ResultsCodes.INVALID_EMAIL)

        email = email.replace(" ", "")

        try:
            valid = validate_email(email)
            return valid.normalized
        except EmailNotValidError as e:
            raise ValueError(ResultsCodes.INVALID_EMAIL) from e


class UserInProject(db.Model):
    """Таблица связи пользователя и проекта для реализации связи многие-ко-многим.

    Attributes:
        id (int): Уникальный идентификатор
        project_id (int): ID проекта
        user_id (int): ID пользователя

    Example:
        >>> user_in_project = UserInProject(
        ...     project_id=12,
        ...     user_id=15
        ... )
    """

    __tablename__ = "user_in_project"

    __table_args__ = (
        db.UniqueConstraint("project_id", "user_id", name="uq_project_user"),
    )

    id = db.Column(db.Integer, primary_key=True)
    project_id = db.Column(db.Integer, db.ForeignKey("project.id"))
    user_id = db.Column(db.Integer, db.ForeignKey("user.id"))


class Notification(db.Model):
    """Модель уведомлений.

    Attributes:
        id (int): Уникальный идентификатор
        project_id (int): ID проекта
        receiver_id (int): ID пользователя, которому отправлено уведомление (внешний ключ к User.id)
        sender_id (int): ID отправителя (внешний ключ к User.id)
        send_time (str): Дата и время отправки уведомления

    Example:
        >>> notification = Notification(
        ...     project_id=12,
        ...     receiver_id=15,
        ...     sender_id=17
        ...     send_time=""
        ... )
    """

    __tablename__ = "notification"

    __table_args__ = (
        db.UniqueConstraint("project_id", "receiver_id", name="uq_project_receiver"),
    )

    id = db.Column(db.Integer, primary_key=True)
    project_id = db.Column(db.Integer, db.ForeignKey("project.id"), nullable=False)
    receiver_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    sender_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    send_time = db.Column(db.DateTime, nullable=False)
