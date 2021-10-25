from werkzeug.security import generate_password_hash
from app import db
from datetime import datetime
from flask import request, jsonify
from ..models.contas import Contas, conta_schema, contas_schema
from ..models.transacoes import Transacoes, transacao_schema, transacoes_schema


def nova_conta():
    idPessoa = request.json['idPessoa']
    saldo = 0
    limiteSaqueDiario = request.json['limiteSaqueDiario']
    flagAtivo = True
    tipoConta = request.json['tipoConta']

    conta = Contas(idPessoa, saldo, limiteSaqueDiario, flagAtivo, tipoConta)

    try:
        db.session.add(conta)
        db.session.commit()
        result = conta_schema.dump(conta)
        return jsonify({'message': 'Conta criada', 'data': result.data}), 201
    except:
        return jsonify({'message': 'Conta não criada, tente novamente', 'data': {}}), 500


def bloquear():
    idConta = request.json['idConta']
    getConta = Contas.query.get(idConta)

    if not getConta:
        return jsonify({'message': "Conta não encontrada, tente novamente", 'data': {}}), 404

    if getConta:
        try:
            getConta.flagAtivo = False
            db.session.commit()
            result = conta_schema.dump(getConta)
            return jsonify({'message': 'conta bloqueada com sucesso', 'data': result.data}), 200
        except:
            return jsonify({'message': 'Não foi possivel bloquear, tente novamante', 'data': {}}), 500


def consulta_saldo():
    idConta = request.json['idConta']
    getConta = Contas.query.get(idConta)

    if not getConta:
        return jsonify({'message': "Conta não encontrada, tente novamente", 'data': {}}), 404

    if getConta:
        try:
            result = conta_schema.dump(getConta)
            print(result)
            return jsonify({'message': f'seu saldo é: {getConta.saldo}', 'data': result.data}), 200
        except:
            return jsonify({'message': 'Não foi possivel consultar o saldo, tente novamante', 'data': {}}), 500


def depositar():
    idConta = request.json['idConta']
    depositar = request.json['depositar']
    getConta = Contas.query.get(idConta)
    saldo_futuro = getConta.saldo + depositar
    verificar_bloqueio = getConta.flagAtivo

    transacao = Transacoes(idConta, depositar)

    if verificar_bloqueio is False:
        return jsonify({'message': "Esta conta esta bloqueada, nao é possivel realizar deposito", 'data': {}}), 500

    if not getConta:
        return jsonify({'message': "Conta não encontrada, tente novamente", 'data': {}}), 404

    if getConta:
        try:
            getConta.saldo = saldo_futuro
            db.session.add(transacao)
            db.session.commit()
            result = conta_schema.dump(getConta)
            return jsonify({'message': 'Deposito realizado com sucesso', 'data': result.data}), 200
        except:
            return jsonify({'message': 'Não foi possivel depositar, tente novamante', 'data': {}}), 500


def saque():
    idConta = request.json['idConta']
    saque = request.json['saque']
    hoje = datetime.today().strftime('%Y-%m-%d')
    getConta = Contas.query.get(idConta)
    limite_saque_diario = getConta.limiteSaqueDiario
    verificar_bloqueio = getConta.flagAtivo

    getTrasacoes = Transacoes.query.filter(Transacoes.dataTransacao.like(f'%{hoje}%'), Transacoes.idConta.like(f'%{idConta}%')).all()
    saques_realizados = 0
    interacao = 0
    for i in getTrasacoes:
        if getTrasacoes[interacao].valor < 0:
            saques_realizados = saques_realizados + getTrasacoes[interacao].valor

        interacao = interacao + 1

    valor_ja_sacado_mais_saque = saques_realizados - saque
    check_limite = limite_saque_diario + valor_ja_sacado_mais_saque

    saldo_futuro = getConta.saldo - saque
    valor_negativo = -1 * saque
    transacao = Transacoes(idConta, valor_negativo)

    if verificar_bloqueio is False:
        return jsonify({'message': "Esta conta esta bloqueada, nao é possivel realizar saque", 'data': {}}), 500

    if check_limite < 0:
        return jsonify({'message': "Voce nao tem limite para esse saque, consulte o limite e tente novamente", 'data': {}}), 500

    if saldo_futuro < 0:
        return jsonify({'message': "Voce nao tem saldo para esse saque, consulte o saldo e tente novamente", 'data': {}}), 500

    if not getConta:
        return jsonify({'message': "Conta não encontrada, tente novamente", 'data': {}}), 404

    if getConta:
        try:
            getConta.saldo = saldo_futuro
            db.session.add(transacao)
            db.session.commit()
            result = conta_schema.dump(getConta)
            return jsonify({'message': 'Saque realizado com sucesso', 'data': result.data}), 200
        except:
            return jsonify({'message': 'Não foi possivel sacar, tente novamante', 'data': {}}), 500
