from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker, relationships
from sqlalchemy.ext.declarative import declarative_base

engine = create_engine(
    'sqlite:///config/dockapi.db',
    connect_args={'check_same_thread': False}
)

db_session = scoped_session(sessionmaker(autocommit=False,
                                         bind=engine))
Base = declarative_base()
Base.query = db_session.query_property()
Base.metadata.create_all(bind=engine)


def init_db():
    Base.metadata.create_all(bind=engine)
