from app.shared.extensions import db
from app.shared.dbmodels import File, Project


class FileRepository:
    """
    Репозиторий для работы с файлами

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель File
    """

    def get_by_id(self, id):
        return db.session.query(File).filter(File.id == id).first()

    def get_by_name_in_parent(self, name, parent_id):
        """
        Получить проект по имени.

        Args:
            name (str): Название проекта.

        Returns:
            Project: Проект.
        """
        return (
            db.session.query(File)
            .filter((File.name == name) & (File.parent_id == parent_id))
            .first()
        )

    def get_by_name(self, name):
        """
        Получить проект по имени.

        Args:
            name (str): Название проекта.

        Returns:
            Project: Проект.
        """
        return db.session.query(File).filter(File.name == name).first()

    def create_file(self, _name, _parent_id, _project_id, _is_folder):
        """
        Получить проект по имени.

        Args:
            name (str): Название проекта.

        Returns:
            Project: Проект.
        """
        file = File(
            name=_name,
            is_folder=_is_folder,
            project_id=_project_id,
            parent_id=_parent_id,
        )
        db.session.add(file)
        db.session.commit()

    def delete_file(self, _id):
        """
        Получить проект по имени.

        Args:
            name (str): Название проекта.

        Returns:
            Project: Проект.
        """
        file = db.session.query(File).filter(File.id == _id).first()
        if file:
            db.session.delete(file)
            db.session.commit()
            return True
        return False

    def get_project_id(self, file_id):
        file = db.session.query(File).filter(File.id == file_id).first()
        if file:
            return file.id
        return None


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

    def get_by_id(self, id):
        """
        Получить проект по имени.

        Args:
            name (str): Название проекта.

        Returns:
            Project: Проект.
        """
        return db.session.query(Project).filter(Project.id == id).first()


file_repo = FileRepository()
project_repo = ProjectRepository()
