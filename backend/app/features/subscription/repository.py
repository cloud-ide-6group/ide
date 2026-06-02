from app.shared.base_repositories import BaseUserRepository
from app.shared.extensions import db
from app.shared.dbmodels import User
from app.shared.consts import ResultsCodes


class UserRepository(BaseUserRepository):
    def subscribe(self, user_id, subscription_end):
        """
        Добавляет подписку в БД

        Args:
            user_id (int): Id пользователя
            subscription_end (datetime): Дата и время конца подписки

        Returns:
            ResultCodes: Результат выполнения операции
        """
        try:
            user = db.session.query(User).filter(User.id == user_id).first()

            if not user:
                return None, ResultsCodes.USER_NOT_FOUND

            user.subscription_end = subscription_end
            db.session.commit()
            return ResultsCodes.OK

        except Exception as e:
            db.session.rollback()
            print(f"Error in subscribe: {e}")
            return ResultsCodes.UNEXPECTED_ERROR


user_repo = UserRepository()
