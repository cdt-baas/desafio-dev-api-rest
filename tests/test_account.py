import pytest
from fastapi import status
from fastapi.testclient import TestClient
from app.main import app


client = TestClient(app)

acc = None

@pytest.fixture
def create_accounts():
    global acc
    client.post("/holders/", json={"cpf": "78700719005", "name": "Holder 1"})
    client.post("/holders/", json={"cpf": "40908274009", "name": "Holder 2"})
    client.post("/holders/", json={"cpf": "34841101047", "name": "Holder 3"})
    response = client.post("/accounts/", json={"agency": 1234, "holder_cpf": "78700719005"})
    assert response.status_code == status.HTTP_201_CREATED
    client.post("/accounts/", json={"agency": 1234, "holder_cpf": "40908274009"})
    client.post("/accounts/", json={"agency": 1234, "holder_cpf": "34841101047"})
    acc = response.json()
    return response
    # data = response.json()


def test_deposit(create_accounts):
    global acc
    response = client.post(f"/accounts/{acc['number']}/deposit/", params={"amount": 7500})
    assert response.status_code == status.HTTP_200_OK


@pytest.mark.dependency(depends=["test_deposit"])
def test_withdraw():
    global acc
    response = client.post(f"/accounts/{acc['number']}/withdraw/", params={"amount": 200})
    assert response.status_code == status.HTTP_200_OK
