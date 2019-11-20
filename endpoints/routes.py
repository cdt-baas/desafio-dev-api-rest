from flask import request, jsonify
from flask_restful import reqparse, Resource

from config.config import *
from config.database import db_session
from models.contas import Contas
from models.pessoas import Pessoas
from models.transacoes import Transacoes

parser = reqparse.RequestParser()

parser.add_argument('nome', type=str, help='Parâmetro nome é necessário para criar Pessoa')
parser.add_argument('cpf', type=str, help='Parâmetro cpf é necessário para criar Pessoa')
parser.add_argument('data_nascimento', type=int, help='Parâmetro data_nascimento é necessário para criar Pessoa')

parser.add_argument('pessoa_id', type=str, help='Parâmetro pessoa_id é necessário para Conta')
parser.add_argument('saldo', type=str, help='Parâmetro saldo é necessário para Conta')
parser.add_argument('limite_saque_diario', type=int, help='Parâmetro limite_saque_diario é necessário para Conta')
parser.add_argument('flag_ativo', type=str, help='Parâmetro flag_ativo é necessário para Conta')
parser.add_argument('tipo_conta', type=str, help='Parâmetro tipo_conta é necessário para Conta')
parser.add_argument('data_criacao', type=str, help='Parâmetro data_criacao é necessário para Conta')

parser.add_argument('conta_id', type=str, help='Parâmetro conta_id é necessário para criar Pessoa')
parser.add_argument('valor', type=str, help='Parâmetro valor é necessário para criar Pessoa')
parser.add_argument('data_transacao', help='Parâmetro data_transacao é necessário para criar Pessoa')


class Home(Resource):
    def get(self):
        return {
            'Home': {
                'DockAPI': ['Teste de Nivelamento.']
            },
            'Desenvolvido': {
                'por': ['Guilherme Jannotti Arantes.']
            }

        }


class Versao(Resource):
    def get(self):
        return {
            'DockAPI': {
                'Versão': ['v1.']
            }
        }


class Pessoa(Resource):
    def get(self, nome):
        pessoa = Pessoas.query.filter_by(nome=nome).first()
        try:
            response = {
                'nome': pessoa.nome,
                'id': pessoa.id,
                'data_nascimento': pessoa.data_nascimento,
                'cpf': pessoa.cpf
            }
        except AttributeError:
            response = {
                'status': 'error',
                'mensagem': 'Pessoa nao encontrada'
            }
        return response

    def delete(self, nome):
        pessoa = Pessoas.query.filter_by(nome=nome).first()
        try:
            pessoa.delete()
            mensagem = 'Pessoa {} excluida com sucesso'.format(pessoa.nome)
            return {'status': 'sucesso', 'mensagem': mensagem}
        except AttributeError:
            response = {
                'status': 'error',
                'mensagem': 'Pessoa nao encontrada'
            }
        return response


class PessoaCollection(Resource):
    def get(self):
        pessoas = Pessoas.query.all()
        try:
            response = [{'id': i.id, 'nome': i.nome,
                         'cpf': i.cpf, 'data_nascimento': i.data_nascimento} for i in pessoas]
        except AttributeError:
            response = {
                'status': 'error',
                'mensagem': 'Pessoa nao encontrada'
            }
        return response

    def post(self):
        nome = request.json['nome']
        cpf = request.json['cpf']
        data_nascimento = request.json['data_nascimento']
        nova_pessoa = Pessoas(nome=nome, cpf=cpf, data_nascimento=data_nascimento)
        db_session.add(nova_pessoa)
        db_session.commit()
        return jsonify(nova_pessoa.serialize())


class Conta(Resource):
    def get(self, id):
        conta = Contas.query.filter_by(id=id).first()
        try:
            response = {
                'id': conta.id,
                'pessoa_id': conta.pessoa_id,
                'saldo': conta.saldo,
                'limite_saque_diario': conta.limite_saque_diario,
                'flag_ativo': conta.flag_ativo,
                'tipo_conta': conta.tipo_conta,
                'data_criacao': conta.data_criacao
            }
        except AttributeError:
            response = {
                'status': 'error',
                'mensagem': 'Conta nao encontrada'
            }
        return response

    def delete(self, id):
        conta = Contas.query.filter_by(id=id).first()
        mensagem = 'Conta {} excluida com sucesso'.format(conta.id)
        conta.delete()
        return {'status': 'sucesso', 'mensagem': mensagem}


class ContaCollection(Resource):
    def get(self):
        contas = Contas.query.all()
        response = [{'id': i.id, 'pessoa_id': i.pessoa_id, 'saldo': i.saldo,
                     'limite_saque_diario': i.limite_saque_diario,
                     'flag_ativo': i.flag_ativo, 'tipo_conta': i.tipo_conta,
                     'data_criacao': i.data_criacao} for i in contas]
        return response

    def post(self):
        pessoa_id = request.json['pessoa_id']
        saldo = request.json['saldo']
        limite_saque_diario = request.json['limite_saque_diario']
        flag_ativo = request.json['flag_ativo']
        tipo_conta = request.json['tipo_conta']
        data_criacao = request.json['data_criacao']
        nova_conta = Contas(pessoa_id=pessoa_id, saldo=saldo, limite_saque_diario=limite_saque_diario,
                            flag_ativo=flag_ativo, tipo_conta=tipo_conta, data_criacao=data_criacao)
        db_session.add(nova_conta)
        db_session.commit()
        return jsonify(nova_conta.serialize())


class SaldoContaPorID(Resource):
    def get(self, id):
        conta = Contas.query.filter_by(id=id).first()
        try:
            response = {
                'saldo': conta.saldo
            }
        except AttributeError:
            response = {
                'status': 'error',
                'mensagem': 'Conta invalida'
            }
        return response


class DepositaContaPorID(Resource):
    def get(self, id):
        transacao = Transacoes.query.filter_by(id=id).first()
        try:
            response = {
                'conta_id': transacao.conta_id,
                'valor': transacao.valor,
                'data_transacao': transacao.data_transacao
            }
        except AttributeError:
            response = {
                'status': 'error',
                'mensagem': 'Transacao nao encontrada'
            }
        return response

    def put(self, id):
        conta = Contas.query.filter_by(id=id).first()
        valor_deposito = request.json['valor_deposito']
        conta.saldo += valor_deposito
        transacao = Transacoes(conta_id=conta.id, valor=valor_deposito, tipo='deposito')
        transacao.save()
        conta.save()
        return jsonify('Conta {} editada com sucesso'.format(conta.id))


class SaqueContaPorID(Resource):
    def put(self, id):
        conta = Contas.query.filter_by(id=id).first()
        valor_saque = request.json['valor_saque']
        if conta.saldo < valor_saque:
            response = {
                'status': 'error',
                'mensagem': 'Saldo insuficiente'
            }
            return response
        else:
            transacao = Transacoes(conta_id=conta.id, valor=valor_saque, tipo='saque')
            transacao.save()
            conta.saldo -= valor_saque
            conta.save()

        return jsonify('Saque de {} na conta {} realizada com sucesso'.format(valor_saque, conta.id))


class BloqueiaContaPorID(Resource):
    def put(self, id):
        conta = Contas.query.filter_by(id=id).first()
        conta.flag_ativo = False
        conta.save()
        return jsonify('Conta {} bloqueada com sucesso'.format(conta.id))


class Transacao(Resource):
    def get(self, id):
        transacao = Transacoes.query.filter_by(conta_id=id).all()
        response = list()

        try:
            start = request.json['start']
            end = request.json['end']
            for item in transacao:
                if start < item.data_transacao < end:
                    response.append({'id': item.id,
                                     'conta_id': item.conta_id,
                                     'valor': item.valor,
                                     'tipo': item.tipo,
                                     'data_transacao': item.data_transacao})

        except KeyError:
            for item in transacao:
                response.append({
                    'conta_id': item.conta_id,
                    'valor': item.valor,
                    'data_transacao': item.data_transacao,
                    'tipo': item.tipo,
                })

        finally:

            return jsonify(response)


class TransacaoCollection(Resource):
    def get(self):
        transacoes = Transacoes.query.all()
        response = [{'id': i.id, 'conta_id': i.conta_id, 'valor': i.valor, 'tipo': i.tipo, 'data_transacao': i.data_transacao} for i in
                    transacoes]
        return response


class ExtratoTransacoesPorID(Resource):
    def get(self, id):
        transacao = Transacoes.query.filter_by(id=id).first()
        try:
            response = {
                'conta_id': transacao.conta_id,
                'valor': transacao.valor,
                'data_transacao': transacao.data_transacao
            }
        except AttributeError:
            response = {
                'status': 'error',
                'mensagem': 'Transacao nao encontrada'
            }
        return response


# Rota para o Home
api.add_resource(Home, '/')  # Home
api.add_resource(Versao, '/v1')  # Versão

# Rotas para classe pessoa
api.add_resource(PessoaCollection, '/v1/pessoas')  # Lista todas as pessoas cadastradas
api.add_resource(Pessoa, '/v1/pessoas/<nome>')  # Lista uma pessoa especifica

# Rotas para classe Conta
api.add_resource(ContaCollection, '/v1/contas')  # Lista todas as contas cadastradas
api.add_resource(Conta, '/v1/contas/<id>')  # Lista uma conta especifica
api.add_resource(SaldoContaPorID, '/v1/contas/<id>/saldo')  # Consulta o saldo da conta especifica
api.add_resource(BloqueiaContaPorID, '/v1/contas/<id>')  # Consulta o saldo da conta especifica


# Rotas para classe transacoes
api.add_resource(TransacaoCollection, '/v1/transacoes') # Lista todas as transações
api.add_resource(Transacao, '/v1/transacoes/<id>')  # Lista uma transação específica
# api.add_resource(ExtratoTransacoesPorID, '/v1/transacoes/<id>')
api.add_resource(DepositaContaPorID, '/v1/transacoes/<id>/deposita')  # Deposita na conta especifica
api.add_resource(SaqueContaPorID, '/v1/transacoes/<id>/saque')  # Saque na conta especifica
