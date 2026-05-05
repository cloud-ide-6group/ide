from flask import Flask, request, Response
from flasgger import Swagger
from config import DebugConfig
from .shared.extensions import db, migrate, socketio
from flask_socketio import SocketIO
from flask_cors import CORS


def register_features(app):
    from .features.authorization.routes import auth_bp
    from .features.profile.routes import profile_bp
    from .features.project.routes import project_bp
    from .features.invitation.routes import invitation_bp
    from .shared.features.notifications.routes import notifications_bp

    app.register_blueprint(auth_bp)
    app.register_blueprint(profile_bp)
    app.register_blueprint(project_bp)
    app.register_blueprint(invitation_bp)
    app.register_blueprint(notifications_bp)


def register_shared_features(app):
    from .shared.features.jwt_token import jwt_token_bp
    from .shared.features.languages import languages_bp

    app.register_blueprint(jwt_token_bp)
    app.register_blueprint(languages_bp)


def create_app(config_class=DebugConfig):
    app = Flask(__name__)
    app.config.from_object(config_class)
    CORS(app, resources={r"/*": {"origins": "*"}})
    socketio.init_app(app, cors_allowed_origins="*", cors_credentials=True)

    db.init_app(app)
    if not app.config["DB_TEST"]:
        migrate.init_app(app, db)

    if app.config["SWAGGER_URL_PREFIX"]:
        swagger_config = {
            "headers": [],
            "specs": [
                {
                    "endpoint": "apispec",
                    "route": "/apispec.json",
                    "rule_filter": lambda rule: True,
                    "model_filter": lambda tag: True,
                }
            ],
            "static_url_path": "/flasgger_static",
            "swagger_ui": True,
            "specs_route": app.config["SWAGGER_URL_PREFIX"],
        }
        swagger = Swagger(app, config=swagger_config)

        @app.before_request
        def protect():
            if request.path.startswith(app.config["SWAGGER_URL_PREFIX"]):
                if not app.config.get("SWAGGER_SECRET_TOKEN"):
                    return

                auth = request.authorization
                if (
                    not auth
                    or auth.password != app.config["SWAGGER_SECRET_TOKEN"]
                    or auth.username != app.config["SWAGGER_SECRET_USER"]
                ):
                    return Response(
                        "Authentication required",
                        401,
                        {"WWW-Authenticate": 'Basic realm="Swagger Docs"'},
                    )

        url = app.config["SWAGGER_URL_PREFIX"]
        print(f"Swagger UI started on {url}")

    register_features(app)
    register_shared_features(app)

    with app.app_context():
        from .shared import dbmodels

    return app
