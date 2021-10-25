import datetime

from app import db, ma


class Transacoes(db.Model):
    idTransacao = db.Column(db.Integer, primary_key=True, autoincrement=True)
    idConta = db.Column(db.Integer, db.ForeignKey('contas.idConta'), nullable=False)
    valor = db.Column(db.Numeric, nullable=False)
    dataTransacao = db.Column(db.DateTime, default=datetime.datetime.now())

    def __init__(self, idConta, valor):
        self.idConta = idConta
        self.valor = valor


"""Definindo o Schema do Marshmallow para facilitar a utilização de JSON"""


class TransacoesSchema(ma.Schema):
    class Meta:
        fields = ('idTransacao', 'idConta', 'valor', 'dataTransacao')


transacao_schema = TransacoesSchema(strict=True)
transacoes_schema = TransacoesSchema(many=True)
