import pytest
from fastapi import status
from fastapi.testclient import TestClient
from app.main import app


client = TestClient(app)

@pytest.fixture
def create_holder():
    response = client.post("/holders/", json={"cpf": "29105533023", "name": "Holder Full Name"})
    return response

def test_create_holder(create_holder):
    assert create_holder.status_code == status.HTTP_201_CREATED

def test_duplicated_holder(create_holder):
    response = client.post("/holders/", json={"cpf": "29105533023", "name": "Holder Full Name"})
    assert response.status_code == status.HTTP_409_CONFLICT

def test_delete_holder(create_holder):
    response = client.delete("/holders/29105533023")
    assert response.status_code == status.HTTP_204_NO_CONTENT

def test_delete_non_existent_holder(create_holder):
    response = client.delete("/holders/11122233300")
    assert response.status_code == status.HTTP_404_NOT_FOUND

def test_get_all_holders(create_holder):
    response = client.get("/holders/")
    assert response.status_code == status.HTTP_200_OK
