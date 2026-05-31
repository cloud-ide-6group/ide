from sqlalchemy.exc import IntegrityError

from app.shared.extensions import db
from app.shared.dbmodels import User, Notification
from app.shared.base_repositories import (
    BaseProjectRepository,
    BaseUserRepository,
    BaseNotificationRepository,
)


class ProjectRepository(BaseProjectRepository):
    def add_user_in_project(self, _project_id, _user_id):
        """
        Создает сущность UserInProject для связи проекта и пользователя

        Args:
            _project_id (int): Id проекта.
            _user_id (int): Id пользователя.
        """
        self.user_in_project_repo.add_user_in_project(_user_id, _project_id)

    def delete_user_from_project(self, _project_id, _user_id):
        """
        Удаляет связь UserInProject.

        Args:
            _project_id (int): Id проекта.
            _user_id (int): Id пользователя.
        """
        self.user_in_project_repo.delete_user_from_project(_user_id, _project_id)


class UserRepository(BaseUserRepository):
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
        user = db.session.query(User).filter(User.id == id).first()
        if user == None:
            return False
        else:
            return True


class NotificationRepository(BaseNotificationRepository):
    def delete_by_reciever_project_id(self, receiver_id, project_id):
        nots = (
            db.session.query(Notification)
            .filter(
                (Notification.receiver_id == receiver_id)
                & (Notification.project_id == project_id)
            )
            .all()
        )
        for n in nots:
            db.session.delete(n)
        db.session.commit()


project_repo = ProjectRepository()
user_repo = UserRepository()
notification_repo = NotificationRepository()
