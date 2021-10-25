from werkzeug.security import generate_password_hash
from app import db
from flask import request, jsonify
from ..models.pessoas import Pessoas, pessoa_schema, pessoas_schema


def cadastrar_pessoa():
    nome = request.json['nome']
    cpf = request.json['cpf']
    dataNascimento = request.json['dataNascimento']

    pessoa = Pessoas(nome, cpf, dataNascimento)

    try:
        db.session.add(pessoa)
        db.session.commit()
        result = pessoa_schema.dump(pessoa)
        return jsonify({'message': 'Pessoa cadastrada com sucesso', 'data': result.data}), 201
    except:
        return jsonify({'message': 'Pessoa não cadastrada, tente novamente', 'data': {}}), 500



