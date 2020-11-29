import os
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS, cross_origin

db = SQLAlchemy()

def create_app(test_config = None):
    # create and configure the app
    app = Flask(__name__, instance_relative_config=True)
    # app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://localhost/test'
    app.config.from_object("buying.config.Config")
    app.config['CORS_HEADERS'] = 'Content-Type'
    cors = CORS(app, resources={r"*": {"origins": "http://localhost:3000"}})

    db.init_app(app)

    with app.app_context():
        from . import routes
        db.create_all()

    return app