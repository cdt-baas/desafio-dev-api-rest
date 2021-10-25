from app import app
from flask import jsonify, url_for, redirect
from ..views import users, helper, contas, pessoas, transacoes

"""Neste arquivo iremos criar todas rotas para aplicação para manter o código limpo usando
 as views(controllers)  e as relacionando por meio de funções"""


@app.route('/v1', methods=['GET'])
@helper.token_required
def root(current_user):
    return jsonify({'message': f'Hello {current_user.name}'})


@app.route('/v1/authenticate', methods=['POST'])
def authenticate():
    return helper.auth()


@app.route('/v1/users', methods=['GET'])
def get_users():
    return users.get_users()


@app.route('/v1/users/<id>', methods=['GET'])
def get_user(id):
    return users.get_user(id)


@app.route('/v1/users', methods=['POST'])
def post_users():
    return users.post_user()


@app.route('/v1/users/<id>', methods=['DELETE'])
def delete_users(id):
    return users.delete_user(id)


@app.route('/v1/users/<id>', methods=['PUT'])
def update_users(id):
    return users.update_user(id)


@app.route('/v1/auth', methods=['POST'])
def auth():
    pass


# teste docker

@app.route('/v1/nova-conta', methods=['POST'])
@helper.token_required
def nova_conta(current_user):
    return contas.nova_conta()


@app.route('/v1/casdastrar-pessoa', methods=['POST'])
@helper.token_required
def cadastrar_pessoa(current_user):
    return pessoas.cadastrar_pessoa()


@app.route('/v1/depositar', methods=['PUT'])
@helper.token_required
def depositar(current_user):
    return contas.depositar()


@app.route('/v1/saque', methods=['PUT'])
@helper.token_required
def saque(current_user):
    return contas.saque()


@app.route('/v1/saldo', methods=['POST'])
@helper.token_required
def consulta_saldo(current_user):
    return contas.consulta_saldo()


@app.route('/v1/bloquear', methods=['PUT'])
@helper.token_required
def bloquear(current_user):
    return contas.bloquear()


@app.route('/v1/extrato', methods=['POST'])
@helper.token_required
def extrato(current_user):
    return transacoes.extrato()


@app.route('/v1/extrato-periodo', methods=['POST'])
@helper.token_required
def extrato_periodo(current_user):
    return transacoes.extrato_periodo()
