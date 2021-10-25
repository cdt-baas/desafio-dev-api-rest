from werkzeug.security import generate_password_hash
from flask import request, jsonify
from ..models.transacoes import Transacoes, transacao_schema, transacoes_schema


def extrato():
    idConta = request.json['idConta']

    transacoes = Transacoes.query.filter(Transacoes.idConta.like(f'%{idConta}%')).all()

    if transacoes:
        result = transacoes_schema.dump(transacoes)
        return jsonify({'message': 'extrato realizado', 'data': result.data}), 200

    return jsonify({'message': 'Não foi possivel realizar o extrato, tente novamante', 'data': {}}), 500


def extrato_periodo():
    idConta = request.json['idConta']
    data_inicio = request.json['data_inicio']
    data_fim = request.json['data_fim']

    transacoes = Transacoes.query.filter(Transacoes.dataTransacao.between(data_inicio, data_fim),
                                         Transacoes.idConta.like(f'%{idConta}%'))

    if transacoes:
        result = transacoes_schema.dump(transacoes)
        return jsonify({'message': 'extrato por periodo realizado', 'data': result.data}), 200

    return jsonify({'message': 'Não foi possivel realizar extrato por periodo, tente novamante', 'data': {}}), 500
