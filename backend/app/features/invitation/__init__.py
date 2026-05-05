from flask import Blueprint

invitation_bp = Blueprint("invitation", __name__)
from . import routes
