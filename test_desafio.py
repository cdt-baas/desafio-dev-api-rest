import os
import tempfile
from app import app
import pytest
from flask import json


def test_criar_usuario():
    response = app.test_client().post(
        '/v1/users',
        data=json.dumps({"name": "test", "username": "test", "password": "test", "email": "test@hotmail.com"}),
        content_type='application/json',
    )

    json.loads(response.get_data(as_text=True))

    assert response.status_code == 201


def test_autorizar():
    response = app.test_client().post(
        '/v1/authenticate',
        content_type='application/json',
        headers={'authorization': "Basic dGVzdDp0ZXN0"}
    )

    json.loads(response.get_data(as_text=True))
    assert response.status_code == 200


def test_criar_pessoa():
    response = app.test_client().post(
        '/v1/authenticate',
        content_type='application/json',
        headers={'authorization': "Basic dGVzdDp0ZXN0"}
    )

    data = json.loads(response.get_data(as_text=True))
    token = data['token']

    response2 = app.test_client().post(
        f'/v1/casdastrar-pessoa?token={token}',
        data=json.dumps({"nome": "test", "cpf": "06589940301", "dataNascimento": "1996-04-11"}),
        content_type='application/json',

    )

    json.loads(response2.get_data(as_text=True))

    assert response2.status_code == 201


def test_criar_conta():
    response = app.test_client().post(
        '/v1/authenticate',
        content_type='application/json',
        headers={'authorization': "Basic dGVzdDp0ZXN0"}
    )

    data = json.loads(response.get_data(as_text=True))
    token = data['token']

    response2 = app.test_client().post(
        f'/v1/nova-conta?token={token}',
        data=json.dumps({"idPessoa": 1, "limiteSaqueDiario": 5000, "tipoConta": 10}),
        content_type='application/json',

    )

    json.loads(response2.get_data(as_text=True))

    assert response2.status_code == 201


def test_depositar():
    response = app.test_client().post(
        '/v1/authenticate',
        content_type='application/json',
        headers={'authorization': "Basic dGVzdDp0ZXN0"}
    )

    data = json.loads(response.get_data(as_text=True))
    token = data['token']

    response2 = app.test_client().put(
        f'/v1/depositar?token={token}',
        data=json.dumps({"idConta": 1, "depositar": 1500}),
        content_type='application/json',

    )

    json.loads(response2.get_data(as_text=True))

    assert response2.status_code == 200


def test_saque():
    response = app.test_client().post(
        '/v1/authenticate',
        content_type='application/json',
        headers={'authorization': "Basic dGVzdDp0ZXN0"}
    )

    data = json.loads(response.get_data(as_text=True))
    token = data['token']

    response2 = app.test_client().put(
        f'/v1/saque?token={token}',
        data=json.dumps({"idConta": 1, "saque": 500}),
        content_type='application/json',

    )

    json.loads(response2.get_data(as_text=True))

    assert response2.status_code == 200


def test_saque_limite():
    response = app.test_client().post(
        '/v1/authenticate',
        content_type='application/json',
        headers={'authorization': "Basic dGVzdDp0ZXN0"}
    )

    data = json.loads(response.get_data(as_text=True))
    token = data['token']

    response2 = app.test_client().put(
        f'/v1/saque?token={token}',
        data=json.dumps({"idConta": 1, "saque": 15000}),
        content_type='application/json',

    )

    json.loads(response2.get_data(as_text=True))

    assert response2.status_code == 500


def test_saque_sem_saldo():
    response = app.test_client().post(
        '/v1/authenticate',
        content_type='application/json',
        headers={'authorization': "Basic dGVzdDp0ZXN0"}
    )

    data = json.loads(response.get_data(as_text=True))
    token = data['token']

    response2 = app.test_client().put(
        f'/v1/saque?token={token}',
        data=json.dumps({"idConta": 1, "saque": 2000}),
        content_type='application/json',

    )

    json.loads(response2.get_data(as_text=True))

    assert response2.status_code == 500


def test_consulta_saldo():
    response = app.test_client().post(
        '/v1/authenticate',
        content_type='application/json',
        headers={'authorization': "Basic dGVzdDp0ZXN0"}
    )

    data = json.loads(response.get_data(as_text=True))
    token = data['token']

    response2 = app.test_client().post(
        f'/v1/saldo?token={token}',
        data=json.dumps({"idConta": 1}),
        content_type='application/json',

    )

    json.loads(response2.get_data(as_text=True))

    assert response2.status_code == 200


def test_bloquear():
    response = app.test_client().post(
        '/v1/authenticate',
        content_type='application/json',
        headers={'authorization': "Basic dGVzdDp0ZXN0"}
    )

    data = json.loads(response.get_data(as_text=True))
    token = data['token']

    response2 = app.test_client().put(
        f'/v1/bloquear?token={token}',
        data=json.dumps({"idConta": 1}),
        content_type='application/json',

    )

    json.loads(response2.get_data(as_text=True))

    assert response2.status_code == 200


def test_saque_bloqueado():
    response = app.test_client().post(
        '/v1/authenticate',
        content_type='application/json',
        headers={'authorization': "Basic dGVzdDp0ZXN0"}
    )

    data = json.loads(response.get_data(as_text=True))
    token = data['token']

    response2 = app.test_client().put(
        f'/v1/saque?token={token}',
        data=json.dumps({"idConta": 1, "saque": 100}),
        content_type='application/json',

    )

    json.loads(response2.get_data(as_text=True))

    assert response2.status_code == 500


def test_extrato():
    response = app.test_client().post(
        '/v1/authenticate',
        content_type='application/json',
        headers={'authorization': "Basic dGVzdDp0ZXN0"}
    )

    data = json.loads(response.get_data(as_text=True))
    token = data['token']

    response2 = app.test_client().post(
        f'/v1/extrato?token={token}',
        data=json.dumps({"idConta": 1}),
        content_type='application/json',

    )

    json.loads(response2.get_data(as_text=True))

    assert response2.status_code == 200


def test_extrato_periodo():
    response = app.test_client().post(
        '/v1/authenticate',
        content_type='application/json',
        headers={'authorization': "Basic dGVzdDp0ZXN0"}
    )

    data = json.loads(response.get_data(as_text=True))
    token = data['token']

    response2 = app.test_client().post(
        f'/v1/extrato?token={token}',
        data=json.dumps({"idConta": 16, "data_inicio": "2021-10-20", "data_fim": "2021-10-30"}),
        content_type='application/json',

    )

    json.loads(response2.get_data(as_text=True))

    assert response2.status_code == 200
