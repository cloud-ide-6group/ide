from app.shared.dbmodels import Project, File, Chat
from app.shared.extensions import db
from app.shared.base_repositories import (
    BaseMessageRepository,
    BaseProjectRepository,
    BaseUserRepository,
    BaseFileRepository,
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

    def get_chats(self, project_id):
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
    pass


project_repo = ProjectRepository()
file_repo = FileRepository()
message_repo = MessageRepository()
user_repo = UserRepository()
