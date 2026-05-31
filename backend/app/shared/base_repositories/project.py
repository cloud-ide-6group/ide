from app.shared.extensions import db
from app.shared.dbmodels import Project
from .chat import BaseChatRepository
from .user_in_project import BaseUserInProjectRepository


class BaseProjectRepository:
    """
    Репозиторий для работы с проектами.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель Project
    """

    def __init__(self):
        self.chat_repo = BaseChatRepository()
        self.user_in_project_repo = BaseUserInProjectRepository()

    def get_chats(self, project_id):
        try:
            return self.chat_repo.get_by_project_id(project_id)
        except Exception as e:
            raise RuntimeError(f"Getting chats error: {e}")

    def get_by_id(self, id):
        """
        Получить проект по id.

        Args:
            id (int): Id проекта.

        Returns:
            Project: Проект.
        """
        return db.session.query(Project).filter(Project.id == id).first()

    def is_user_in_project(self, user_id, project_id):
        """
        В проекте ли человек.

        Args:
            user_id (int): Id пользователя.
            project_id (int): Id проекта.

        Returns:
            boolean: True, если пользователь приглашен или владеет проектом.
        """
        project = db.session.query(Project).filter(Project.id == project_id).first()
        if project.owner_id == user_id:
            return True

        userInProject = self.user_in_project_repo.get_if_user_in_project(
            user_id, project_id
        )

        if userInProject:
            return True

        return False

    def get_by_name(self, name):
        """
        Получить проект по имени.

        Args:
            name (str): Название проекта.

        Returns:
            Project: Проект.
        """
        return db.session.query(Project).filter(Project.name == name).first()
