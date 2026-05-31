from app.shared.extensions import db
from app.shared.dbmodels import User, Project, UserInProject
from app.shared.base_repositories import BaseUserRepository, BaseProjectRepository


class UserRepository(BaseUserRepository):
    def update_user(self, new_user):
        """
        Обновить значения полей пользователя

        Args:
            new_user (User): Обновленный пользователь

        Returns:
            User: Пользователь
        """
        try:
            old_user = db.session.get(User, new_user.id)

            if old_user == None or new_user == None:
                return None

            old_user.email = new_user.email
            old_user.name = new_user.name
            old_user.password_hash = new_user.password_hash
            old_user.photo_path = new_user.photo_path

            db.session.commit()
            return old_user
        except Exception as e:
            db.session.rollback()
            raise e


class ProjectRepository(BaseProjectRepository):
    def get_by_owner_id(self, owner_id):
        """
        Получить все проекты пользователя по его id.

        Args:
            owner_id (int): Id пользователя.

        Returns:
            list[Project]: Массив проектов

        Example:
            >>> repo = ProjectRepository()
            >>> list_of_projects = repo.get_by_owner_id(123)
        """
        return db.session.query(Project).filter(Project.owner_id == owner_id).all()

    def get_includes(self, user_id):
        """
        Получить все проекты, в которые пользователь приглашен

        Args:
            user_id (int): Id пользователя.

        Returns:
            list[Project]: Массив проектов

        Example:
            >>> repo = ProjectRepository()
            >>> list_of_projects = repo.get_by_owner_id(123)
        """
        user_in_projects = (
            db.session.query(UserInProject)
            .filter(UserInProject.user_id == user_id)
            .all()
        )

        projects = []
        for u in user_in_projects:
            projects.append(
                db.session.query(Project).filter(Project.id == u.project_id).first()
            )

        return projects


user_repo = UserRepository()
project_repo = ProjectRepository()
