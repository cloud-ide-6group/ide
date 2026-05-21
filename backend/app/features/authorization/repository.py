from app.shared.base_repositories import BaseUserRepository
from app.shared.dbmodels import User
from app.shared.extensions import db


class UserRepository(BaseUserRepository):
    def insert_user(self, email, password_hash, name):
        """
        Создать пользователя.

        Args:
            email (str): Email пользователя.
            password_hash (str): Password hash.
            name (str): Имя пользователя.

        Returns:
            User: Пользователь.

        Example:
            >>> repo = UserRepository()
            >>> user = repo.insert_user("email@mail.ru", "hash", "username")
        """
        try:
            user = User(
                name=name,
                email=email,
                password_hash=password_hash,
            )
            db.session.add(user)
            db.session.commit()

            return user

        except Exception as e:
            db.session.rollback()
            raise e


user_repo = UserRepository()
