from flask import Flask, request, Response
from flasgger import Swagger
from config import DebugConfig
from .shared.extensions import db, migrate


def create_app(config_class=DebugConfig):
    app = Flask(__name__)
    app.config.from_object(config_class)

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

    from .features.test_feature.routes import test_bp
    from .features.run_code.routes import run_bp
    from .features.authorization.routes import auth_bp

    app.register_blueprint(test_bp)
    app.register_blueprint(auth_bp)
    app.register_blueprint(run_bp)

    with app.app_context():
        from .shared import dbmodels

    return app
