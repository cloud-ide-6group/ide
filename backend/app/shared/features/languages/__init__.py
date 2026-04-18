from flask import Blueprint

languages_bp = Blueprint("languages", __name__)
from . import routes