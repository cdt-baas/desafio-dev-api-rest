import datetime

from app import db, ma


class Pessoas(db.Model):
    idPessoa = db.Column(db.Integer, primary_key=True, autoincrement=True)
    nome = db.Column(db.Text, nullable=False)
    cpf = db.Column(db.Text, nullable=False)
    dataNascimento = db.Column(db.DateTime, nullable=False)

    def __init__(self, nome, cpf, dataNascimento):
        self.nome = nome
        self.cpf = cpf
        self.dataNascimento = dataNascimento


"""Definindo o Schema do Marshmallow para facilitar a utilização de JSON"""


class PessoasSchema(ma.Schema):
    class Meta:
        fields = ('idPessoa', 'nome', 'cpf', 'dataNascimento')


pessoa_schema = PessoasSchema(strict=True)
pessoas_schema = PessoasSchema(many=True)
