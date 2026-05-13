from app import create_app
from app.shared.extensions import socketio

app = create_app()


if __name__ == "__main__":
    print("Server run")
    socketio.run(app, debug=True, port=3000)


# sphinx-build -b html source build/html
