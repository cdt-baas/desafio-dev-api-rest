from config.database import *
import time
from sqlalchemy import Column, Integer, String, ForeignKey, Float


class Transacoes(Base):
    __tablename__ = 'transacoes'
    id = Column(Integer, primary_key=True)
    conta_id = Column(Integer, ForeignKey('contas.id'))
    valor = Column(Float)
    tipo = Column(String(10))
    data_transacao = Column(Float)
    conta_ref = relationships

    def __init__(self, conta_id, valor, tipo):
        self.conta_id = conta_id
        self.valor = valor
        self.tipo = tipo
        self.data_transacao = time.time()

    def __repr__(self):
        return '<Valor da Transacao: {}>'.format(self.valor)

    def serialize(self):
        return {"id": self.id,
                "conta_id": self.conta_id,
                "valor": self.valor,
                "tipo": self.tipo,
                "data_transacao": self.data_transacao,
                }

    def save(self):
        db_session.add(self)
        db_session.commit()

    def delete(self):
        db_session.delete(self)
        db_session.commit()