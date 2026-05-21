from app.shared.dbmodels import Project, File, UserInProject, Message, User, Chat
from app.shared.extensions import db
from app.shared.consts import ResultsCodes


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

    def get_by_id(self, id):
        """
        Получить проект из базы по id

        Args:
            id (int): Id проекта

        Returns:
            Project: Проект
        """
        return db.session.query(Project).filter(Project.id == id).first()

    def is_user_invited(self, project_id, user_id):
        """
        Приглашен ли пользователь в проект

        Args:
            project_id (int): Id проекта
            user_id (int): Id пользователя

        Returns:
            bool: True, если пользователь уже в проекте, иначе False
        """
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

    def get_chats(self, project_id):
        try:
            return db.session.query(Chat).filter(Chat.project_id == project_id).all()
        except Exception as e:
            raise RuntimeError(f"Getting chats error: {e}")


class FileRepository:
    """
    Репозиторий для работы с файлами.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель File
    """

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


class MessageRepository:
    """
    Репозиторий для работы с файлами

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель File
    """

    def get_chat_messages(self, chat_id):
        try:
            return db.session.query(Message).filter(Message.chat_id == chat_id).all()
        except Exception as e:
            raise RuntimeError(f"Getting messages error: {e}")


class UserRepository:
    """
    Репозиторий для работы с файлами

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель File
    """

    def get_name_by_id(self, id):
        """
        Получить файл по id.

        Args:
            id (int): Id файла.

        Returns:
            File: Файл.
        """
        user = db.session.query(User).filter(User.id == int(id)).first()
        if user:
            return user.name
        else:
            return ResultsCodes.UNKNOWN_USER


project_repo = ProjectRepository()
file_repo = FileRepository()
message_repo = MessageRepository()
user_repo = UserRepository()
