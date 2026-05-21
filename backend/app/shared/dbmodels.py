from .extensions import db
from sqlalchemy.orm import validates
from .consts import ResultsCodes
import re
from email_validator import validate_email, EmailNotValidError


class Project(db.Model):
    """Модель проекта в IDE.

    Example:
        >>> project = Project(
        ...     name="MyFirstProject",
        ...     owner_id=1,
        ...     language_id=3
        ... )
    """

    __tablename__ = "project"

    id = db.Column(db.Integer, primary_key=True)
    """Уникальный идентификатор проекта"""

    name = db.Column(db.Text, unique=True, nullable=False)
    """Название проекта. Должно быть уникальным в системе"""

    owner_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    """ID пользователя-владельца (внешний ключ к user.id)"""

    language_id = db.Column(db.Integer, db.ForeignKey("language.id"), nullable=False)
    """ID языка программирования проекта"""

    chats = db.relationship(
        "Chat",
        backref="project",
        cascade="all, delete-orphan",
    )

    @validates("name")
    def validate_name(self, key, name):
        if name == None or name == "":
            raise ValueError(ResultsCodes.INCORRECT_NAME)

        name = re.sub(r"\s+", " ", name)

        return name


class File(db.Model):
    """Модель файла или папки в проекте.

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
    """Уникальный идентификатор файла"""

    name = db.Column(db.Text, nullable=False)
    """Имя файла или папки"""

    parent_id = db.Column(
        db.Integer, db.ForeignKey("file.id", ondelete="CASCADE"), nullable=True
    )
    """ID родительской папки (null если в корне) (внешний ключ к File.id)"""

    project_id = db.Column(db.Integer, db.ForeignKey("project.id"), nullable=False)
    """ID проекта-владельца"""

    is_folder = db.Column(db.Boolean, nullable=False)
    """True = папка, False = файл"""

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
    """Уникальный идентификатор языка"""

    name = db.Column(db.Text)
    """Имя языка"""

    description = db.Column(db.Text)
    """Описание языка"""

    image_name = db.Column(db.Text)
    """Docker образ для создания контейнера"""

    command = db.Column(db.Text)
    """Команда для запуска кода"""


class Chat(db.Model):
    """Модель чата. Может быть в разных файлах, но в одном проекте.

    Example:
        >>> chat = Chat(
        ...     author_id=1,
        ...     project_id=10,
        ... )
    """

    __tablename__ = "chat"

    id = db.Column(db.Integer, primary_key=True)
    """Уникальный идентификатор"""

    author_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    """ID создавшего участника (внешний ключ к User.id)"""

    project_id = db.Column(
        db.Integer, db.ForeignKey("project.id", ondelete="CASCADE"), nullable=False
    )
    """ID проекта-владельца"""

    messages = db.relationship(
        "Message",
        backref="chat",
        cascade="all, delete-orphan",
    )


class Message(db.Model):
    """Модель сообщения в чате.

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
    """Уникальный идентификатор"""

    text = db.Column(db.Text)
    """Текст сообщения"""

    author_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    """ID создавшего участника (внешний ключ к User.id)"""

    chat_id = db.Column(
        db.Integer, db.ForeignKey("chat.id", ondelete="CASCADE"), nullable=False
    )
    """ID чата-владельца"""

    send_time = db.Column(db.DateTime, nullable=False)
    """Время и дата отправки"""


class User(db.Model):
    """Модель пользователя.

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
    """Уникальный идентификатор"""

    name = db.Column(db.Text, nullable=False)
    """Имя пользователя"""

    photo_path = db.Column(db.Text, default="users_imgs/default.png")
    """Путь к фотографии профиля"""

    password_hash = db.Column(db.Text, nullable=False)
    """Хэш пароля"""

    email = db.Column(db.Text, nullable=False, unique=True)
    """Почта-логин"""

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
    """Уникальный идентификатор"""

    project_id = db.Column(db.Integer, db.ForeignKey("project.id"))
    """ID проекта"""

    user_id = db.Column(db.Integer, db.ForeignKey("user.id"))
    """ID пользователя"""


class Notification(db.Model):
    """Модель уведомлений.

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
    """Уникальный идентификатор"""

    project_id = db.Column(db.Integer, db.ForeignKey("project.id"), nullable=False)
    """ID проекта"""

    receiver_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    """ID пользователя, которому отправлено уведомление (внешний ключ к User.id)"""

    sender_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    """ID отправителя (внешний ключ к User.id)"""

    send_time = db.Column(db.DateTime, nullable=False)
    """Дата и время отправки уведомления"""