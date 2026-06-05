from app.shared.dbmodels import Project, File, Chat, UserInProject, User
from app.shared.extensions import db
from app.shared.base_repositories import (
    BaseMessageRepository,
    BaseProjectRepository,
    BaseUserRepository,
    BaseFileRepository,
    BaseLanguageRepository,
)


class ProjectRepository(BaseProjectRepository):
    def create_project(self, _name, _language_id, _owner_id):
        """
        Добавить проект в базу

        Args:
            _name (str): Имя проекта
            _language_id (int): Id языка
            _owner_id (int): Id создающего пользователя

        Returns:
            Project: Проект
        """
        try:
            project = Project(name=_name, language_id=_language_id, owner_id=_owner_id)
            db.session.add(project)
            db.session.commit()

            return project

        except Exception as e:
            db.session.rollback()
            raise e

    def delete_project(self, _project_id):
        """
        Удалить проект из базы

        Args:
            _project_id (int): Id проекта

        Returns:
            bool: Удален ли
        """
        project = db.session.query(Project).filter(Project.id == _project_id).first()
        if project:
            db.session.delete(project)
            db.session.commit()
            return True

        return False

    def get_chats(self, project_id):
        """
        Получить все чаты проекта

        Args:
            _project_id (int): Id проекта

        Returns:
            list[Chat]: Список чатов
        """
        try:
            return db.session.query(Chat).filter(Chat.project_id == project_id).all()
        except Exception as e:
            raise RuntimeError(f"Getting chats error: {e}")


class FileRepository(BaseFileRepository):
    def get_root_files(self, id):
        """
        Возвращает файлы в корневой директории проекта

        Args:
            id (int): Id файла

        Returns:
            File: Файл
        """
        return (
            db.session.query(File)
            .filter((File.project_id == id) & (File.parent_id == None))
            .all()
        )

    def get_children(self, parent_id):
        """
        Возвращает файлы в директории

        Args:
            parent_id (int): Id файла

        Returns:
            list[File]: Список файлов
        """
        return db.session.query(File).filter(File.parent_id == parent_id).all()


class MessageRepository(BaseMessageRepository):
    pass


class UserRepository(BaseUserRepository):
    def get_by_project_id(self, project_id, owner_id):
        """
        Возвращает всех пользователей в проекте

        Args:
            parent_id (int): Id файла
            owner_id (int): Id владельца

        Returns:
            list[User]: Список пользователей
        """
        usersInProjects = (
            db.session.query(UserInProject)
            .filter(UserInProject.project_id == project_id)
            .all()
        )
        owner = db.session.query(User).filter(User.id == owner_id).first()

        users = [owner]
        for unip in usersInProjects:
            users.append(self.get_by_id(unip.user_id))

        return users

    def count_projects(self, owner_id):
        """
        Возвращает количество проекто впользователя

        Args:
            owner_id (int): Id владельца

        Returns:
            int: Количество
        """
        projects = db.session.query(Project).filter(Project.owner_id == owner_id).all()
        return len(projects)


class LanguageRepository(BaseLanguageRepository):
    pass


project_repo = ProjectRepository()
file_repo = FileRepository()
message_repo = MessageRepository()
user_repo = UserRepository()
language_repo = LanguageRepository()
