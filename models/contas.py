from config.database import *
from sqlalchemy import Column, Integer, ForeignKey, Float, Boolean


class Contas(Base):
    __tablename__ = 'contas'
    id = Column(Integer, primary_key=True)
    pessoa_id = Column(Integer, ForeignKey('pessoas.id'))
    saldo = Column(Float, nullable=False)
    limite_saque_diario = Column(Float, nullable=False)
    flag_ativo = Column(Boolean, nullable=False)
    tipo_conta = Column(Integer, nullable=False)
    data_criacao = Column(Integer, nullable=False)
    pessoa_ref = relationships

    def __init__(self, pessoa_id, saldo, limite_saque_diario, flag_ativo, tipo_conta, data_criacao):
        self.pessoa_id = pessoa_id
        self.saldo = saldo
        self.limite_saque_diario = limite_saque_diario
        self.flag_ativo = flag_ativo
        self.tipo_conta = tipo_conta
        self.data_criacao = data_criacao

    def __repr__(self):
        return '<Saldo da Conta: {}>'.format(self.saldo)

    def serialize(self):
        return {"id": self.id,
                "pessoa_id": self.pessoa_id,
                "saldo": self.saldo,
                "limite_saque_diario": self.limite_saque_diario,
                "flag_ativo": self.flag_ativo,
                "tipo_conta": self.tipo_conta,
                "data_criacao": self.data_criacao
                }

    def save(self):
        db_session.add(self)
        db_session.commit()

    def delete(self):
        db_session.delete(self)
        db_session.commit()

    # def update(self, id, valor):

