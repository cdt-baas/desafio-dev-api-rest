from flask import Flask
from flask_restful import Api

SQLALCHEMY_TRACK_MODIFICATIONS = False

app = Flask(__name__)
app.debug = True
api = Api(app)


