import config
from app import db
from sqlalchemy_utils import create_database, database_exists


def criar_base():
    url = config.SQLALCHEMY_DATABASE_URI
    if not database_exists(url):
        create_database(url)

    db.create_all()
