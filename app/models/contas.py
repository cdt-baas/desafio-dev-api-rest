import datetime

from app import db, ma


class Contas(db.Model):
    idConta = db.Column(db.Integer, primary_key=True, autoincrement=True)
    idPessoa = db.Column(db.Integer, db.ForeignKey('pessoas.idPessoa'), nullable=False)
    saldo = db.Column(db.Numeric, nullable=False)
    limiteSaqueDiario = db.Column(db.Numeric, nullable=False)
    flagAtivo = db.Column(db.Boolean, nullable=False)
    tipoConta = db.Column(db.Integer, nullable=False)
    dataCriacao = db.Column(db.DateTime, default=datetime.datetime.now())

    def __init__(self, idPessoa, saldo, limiteSaqueDiario, flagAtivo, tipoConta):
        self.idPessoa = idPessoa
        self.saldo = saldo
        self.limiteSaqueDiario = limiteSaqueDiario
        self.flagAtivo = flagAtivo
        self.tipoConta = tipoConta


"""Definindo o Schema do Marshmallow para facilitar a utilização de JSON"""


class ContasSchema(ma.Schema):
    class Meta:
        fields = ('idConta', 'idPessoa', 'saldo', 'limiteSaqueDiario', 'flagAtivo', 'tipoConta', 'dataCriacao')


conta_schema = ContasSchema(strict=True)
contas_schema = ContasSchema(many=True)
