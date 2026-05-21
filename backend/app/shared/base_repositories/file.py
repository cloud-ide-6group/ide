from app.shared.extensions import db
from app.shared.dbmodels import File
from app.shared.consts import ResultsCodes
from . import BaseProjectRepository


class BaseFileRepository:
    """
    Репозиторий для работы с файлами

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель File
    """

    def __init__(self):
        self.project_repo = BaseProjectRepository()

    def get_by_id(self, id):
        """
        Получить файл по id.

        Args:
            id (int): Id файла.

        Returns:
            File: Файл.
        """
        if id == "" or id is None:
            return None
        return db.session.query(File).filter(File.id == int(id)).first()

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
            boolean: Удалось ли удалить файл
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
            return self.project_repo.get_by_id(file.project_id)
        return None

    def is_file_exists(self, name, project_id, parent):
        """
        Существует ли такой же файл.

        Args:
            name (str): Имя файла.
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
            )
            .all()
        )
        if files:
            return True
        else:
            return False

    def rename_file(self, id, new_name):
        """
        Переименовывает файл в БД

        Args:
            id (int): Id файла.
            name (str): Новое имя файла.

        Returns:
            ResultsCodes: результат переименования файла
        """
        file = db.session.query(File).filter(File.id == id).first()
        if file:
            file.name = new_name
            db.session.commit()
            return ResultsCodes.OK
        return ResultsCodes.FILE_NOT_EXIST
