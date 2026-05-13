from app.shared.dbmodels import Project, File
from app.shared.extensions import db


class ProjectRepository:
    """
    Репозиторий для работы с проектами.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель Project
    """

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

    def get_project(self, _name):
        """
        Получить проект из базы по имени

        Args:
            _name (str): Имя проекта

        Returns:
            Project: Проект
        """
        return db.session.query(Project).filter(_name == Project.name).first()


class FileRepository:
    """
    Репозиторий для работы с проектами.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель Project
    """

    def get_root_file(self, id):
        return (
            db.session.query(File)
            .filter(File.project_id == id & File.parent_id == None)
            .first()
        )

    def get_children(self, parent_id):
        return db.session.query(File).filter(File.parent_id == parent_id).all()


project_repo = ProjectRepository()
file_repo = FileRepository()
