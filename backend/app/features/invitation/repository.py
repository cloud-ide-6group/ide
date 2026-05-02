from app.shared.extensions import db
from app.shared.dbmodels import Project, UserInProject, User, Notification
import datetime


class ProjectRepository:
    """
    Репозиторий для работы с проектами

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель Project
    """

    def get_by_name(self, name):
        """
        Получить проект по имени.

        Args:
            name (str): Название проекта.

        Returns:
            Project: Проект.
        """
        return db.session.query(Project).filter(Project.name == name).first()

    def add_user_in_project(self, _project_id, _user_id):
        """
        Создает сущность UserInProject для связи проекта и пользователя

        Args:
            _project_id (int): Id проекта.
            _user_id (int): Id пользователя.
        """
        userInProject = UserInProject(project_id=_project_id, user_id=_user_id)
        db.session.add(userInProject)
        db.session.commit()

    def delete_user_from_project(self, _project_id, _user_id):
        """
        Удаляет связь UserInProject.

        Args:
            _project_id (int): Id проекта.
            _user_id (int): Id пользователя.
        """
        user_in_project = (
            db.session.query(UserInProject)
            .filter(
                (UserInProject.project_id == _project_id)
                & (UserInProject.user_id == _user_id)
            )
            .first()
        )
        if user_in_project:
            db.session.delete(user_in_project)
            db.session.commit()


class UserRepository:
    """
    Репозиторий для работы с пользователями.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель User
    """

    def user_exists(self, id):
        """
        Проверяет, существует ли пользователь.

        Args:
            id (int): Id пользователя.

        Returns:
            User: Пользователь

        Example:
            >>> repo = UserRepository()
            >>> user = repo.get_by_id(123)
        """
        user = db.session.query(Project).filter(Project.id == id).first()
        if user == None:
            return False
        else:
            return True

    def get_by_email(self, email):
        """
        Получить пользователя по email.

        Args:
            email (str): Email пользователя.

        Returns:
            User: Пользователь

        Example:
            >>> repo = UserRepository()
            >>> user = repo.get_by_id(123)
        """
        return db.session.query(User).filter(User.email == email).first()


class NotificationRepository:
    """
    Репозиторий для работы с уведомлениями.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель User
    """

    def add_notification(self, _project_id, _sender_id, _receiver_id):
        """
        Добавляет уведомление в БД.

        Args:
            _project_id (int): Id проекта.
            _sender_id (int): Id отправителя.
            _receiver_id (int): Id получаетеля.
        """
        notification = Notification(
            project_id=_project_id,
            sender_id=_sender_id,
            receiver_id=_receiver_id,
            send_time=datetime.datetime.now(),
        )
        db.session.add(notification)
        db.session.commit()


project_repo = ProjectRepository()
user_repo = UserRepository()
notification_repo = NotificationRepository()
