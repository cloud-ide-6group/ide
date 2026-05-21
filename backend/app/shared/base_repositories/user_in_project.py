from sqlalchemy.exc import IntegrityError

from app.shared.extensions import db
from app.shared.dbmodels import UserInProject


class BaseUserInProjectRepository:
    """
    Репозиторий для работы с проектами.

    Attributes:
        session: Сессия SQLAlchemy для работы с БД
        model: Модель Project
    """

    def get_if_user_in_project(self, user_id, project_id):
        return (
            db.session.query(UserInProject)
            .filter(
                (UserInProject.project_id == project_id)
                & (UserInProject.user_id == user_id)
            )
            .first()
        )

    def add_user_in_project(self, _user_id, _project_id):
        try:
            userInProject = UserInProject(project_id=_project_id, user_id=_user_id)
            db.session.add(userInProject)
            db.session.commit()
        except IntegrityError as e:
            db.session.rollback()
            raise
        except Exception as e:
            db.session.rollback()
            raise

    def delete_user_from_project(self, _user_id, _project_id):
        user_in_project = self.get_if_user_in_project(_user_id, _project_id)
        if user_in_project:
            db.session.delete(user_in_project)
            db.session.commit()
