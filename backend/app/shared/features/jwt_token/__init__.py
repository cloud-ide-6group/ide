from flask import Blueprint

jwt_token_bp = Blueprint("jwt_token", __name__)
from . import routes
