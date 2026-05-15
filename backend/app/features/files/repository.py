from app.shared.extensions import db
from app.shared.dbmodels import File, Project, UserInProject


class FileRepository:
    """
    Репозиторий для работы с файлами

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель File
    """

    def get_by_id(self, id):
        """
        Получить файл по id.

        Args:
            id (int): Id файла.

        Returns:
            File: Файл.
        """
        return db.session.query(File).filter(File.id == id).first()

    def get_by_name(self, name):
        """
        Получить файл по имени.

        Args:
            name (str): Название файла.

        Returns:
            File: Файл.
        """
        return db.session.query(File).filter(File.name == name).first()

    def create_file(self, _name, _parent_id, _project_id, _is_folder):
        """
        Создать файл.

        Args:
            _name (str): Название файла.
            _parent_id (int): Id родителя.
            _project_id (int): Id проекта.
            _is_folder (boolean): Папка ли.
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
        Удалить файл.

        Args:
            _id (int): Id файла.

        Returns:
            Project: Проект.
        """
        file = db.session.query(File).filter(File.id == _id).first()
        if file:
            db.session.delete(file)
            db.session.commit()
            return True
        return False

    def get_project(self, file_id):
        """
        Получить проект, в котором содержится файл.

        Args:
            file_id (int): Id файла.

        Returns:
            File: Файл.
        """
        file = db.session.query(File).filter(File.id == file_id).first()
        if file:
            return (
                db.session.query(Project).filter(Project.id == file.project_id).first()
            )
        return None

    def is_file_exists(self, name, is_folder, project_id, parent):
        """
        Существует ли такой же файл.

        Args:
            name (str): Имя файла.
            is_folder (boolean): Папка ли.
            project_id (int): Id проекта.
            parent (File): Файл-родитель.

        Returns:
            boolean: True, если файл существует, иначе False.
        """
        files = (
            db.session.query(File)
            .filter(
                File.project_id == project_id,
                File.parent_id == (parent.id if parent is not None else None),
                File.name == name,
                File.is_folder == is_folder,
            )
            .all()
        )
        if files:
            return True
        else:
            return False


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

        userInProject = (
            db.session.query(UserInProject)
            .filter(
                (UserInProject.project_id == project_id)
                & (UserInProject.user_id == user_id)
            )
            .first()
        )

        if userInProject:
            return True

        return False


file_repo = FileRepository()
project_repo = ProjectRepository()
