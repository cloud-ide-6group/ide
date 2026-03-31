from .repository import user_repo
from ...shared.consts import ResultsCodes


def get_user_data(id):
    return user_repo.get_by_id(id), ResultsCodes.OK
