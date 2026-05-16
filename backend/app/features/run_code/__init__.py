from flask import Blueprint

run_code_bp = Blueprint("run_code", __name__)
from . import routes
