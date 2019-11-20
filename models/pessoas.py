from config.database import *
from sqlalchemy import Column, Integer, String, ForeignKey, Float, Boolean, DateTime


class Pessoas(Base):
    __tablename__ = 'pessoas'
    id = Column(Integer, primary_key=True)
    nome = Column(String(50), index=True)
    cpf = Column(String(30), nullable=False)
    data_nascimento = Column(Integer, nullable=False)

    def __init__(self, nome, cpf, data_nascimento):
        self.nome = nome
        self.cpf = cpf
        self.data_nascimento = data_nascimento

    def __repr__(self):
        return '<Nome do Cliente: {}>'.format(self.nome)

    def serialize(self):
        return {"id": self.id,
                "nome": self.nome,
                "cpf": self.cpf,
                "data_nascimento": self.data_nascimento}

    def save(self, commit=True):
        db_session.add(self)
        if commit:
            db_session.commit()
        return self

    def delete(self, commit=True):
        db_session.delete(self)
        return commit and db_session.commit()