from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
from flask_socketio import SocketIO
from redis import Redis
import os
from dotenv import load_dotenv

load_dotenv()

db = SQLAlchemy()
migrate = Migrate()
socketio = SocketIO()
redis_client = Redis.from_url(os.getenv("REDIS_URL"), decode_responses=True)
