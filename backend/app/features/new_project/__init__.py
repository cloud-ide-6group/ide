from flask import Blueprint

new_project_bp = Blueprint("new_project", __name__)
from . import routes
