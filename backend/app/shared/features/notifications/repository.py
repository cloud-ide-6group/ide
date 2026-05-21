from app.shared.extensions import db
from app.shared.dbmodels import Project
from app.shared.base_repositories import (
    BaseNotificationRepository,
    BaseUserRepository,
    BaseProjectRepository,
)


class NotificationRepository(BaseNotificationRepository):
    pass


class UserRepository(BaseUserRepository):
    pass


class ProjectRepository(BaseProjectRepository):
    def get_name(self, project_id):
        """
        Получить имя проекта по id.

        Args:
            project_id (int): Id проекта.

        Returns:
            str: Имя. Может быть None
        """
        project = db.session.query(Project).filter(Project.id == project_id).first()
        if project:
            return project.name
        else:
            return None


notification_repo = NotificationRepository()
user_repo = UserRepository()
project_repo = ProjectRepository()
