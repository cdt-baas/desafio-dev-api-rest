# DESAFIO TÉCNICO


## Pré requisitos
> *JDK 11*

> *Maven 4.0*

## Collection Postman
Na raiz do projeto fiz o commit do arquivo Dock.postman_collection.json para auxiliar nos testes pelo aplicativo Postman.

## ESCOPO MÍNIMO:

### Implementar path que realiza a criação de uma conta

```
curl --location --request POST 'http://localhost:8080/conta' \
--header 'Content-Type: application/json' \
--data-raw '{
    "idConta": 321,
    "pessoa": {
        "idPessoa": 1,
        "nome": "Felipe",
        "cpf": "33582795888",
        "dataCriacao": "20/06/2020" },
    "saldo": 0.00,
    "limiteSaqueDiario": 0.00,
    "flagAtivo": true,
    "tipoConta": 1,
    "dataCriacao": "25/06/2020"
}'
```

```
curl --location --request POST 'http://localhost:8080/conta' \
--header 'Content-Type: application/json' \
--data-raw '{
    "idConta": 322,
    "pessoa": {
        "idPessoa": 2,
        "nome": "Mariana",
        "cpf": "33582795888",
        "dataCriacao": "20/06/2020" },
    "saldo": 0.00,
    "limiteSaqueDiario": 1000.00,
    "flagAtivo": true,
    "tipoConta": 1,
    "dataCriacao": "25/06/2020"
}'
```

```
curl --location --request POST 'http://localhost:8080/conta' \
--header 'Content-Type: application/json' \
--data-raw '{
    "idConta": 323,
    "pessoa": {
        "idPessoa": 3,
        "nome": "Nilson",
        "cpf": "33582795888",
        "dataCriacao": "20/06/2020" },
    "saldo": 0.00,
    "limiteSaqueDiario": 1000.00,
    "flagAtivo": true,
    "tipoConta": 1,
    "dataCriacao": "25/06/2020"
}'
```


### Implementar path que realiza operação de depósito em uma conta


```
curl --location --request POST 'http://localhost:8080/transacao' \
--header 'Content-Type: application/json' \
--data-raw '{
    "idTransacao": 9797,
    "conta": {
        "idConta": 321,
        "pessoa": {
            "idPessoa": 1,
            "nome": "Felipe",
            "cpf": "33582795888",
            "dataCriacao": "20/06/2020" },
        "saldo": 0.00,
        "limiteSaqueDiario": 1000.00,
        "flagAtivo": true,
        "tipoConta": 1,
        "dataCriacao": "25/06/2020"
    },
    "valor": 50.00,
    "dataTransacao": "26/06/2020"
}'
```


### Implementar path que realiza operação de consulta de saldo em determinada conta

```
curl --location --request GET 'http://localhost:8080/conta/321/saldo'
```


### Implementar path que realiza operação de saque em uma conta

```
curl --location --request POST 'http://localhost:8080/transacao' \
--header 'Content-Type: application/json' \
--data-raw '{
    "idTransacao": 9797,
    "conta": {
        "idConta": 321,
        "pessoa": {
            "idPessoa": 1,
            "nome": "Felipe",
            "cpf": "33582795888",
            "dataCriacao": "20/06/2020" },
        "saldo": 0.00,
        "limiteSaqueDiario": 1000.00,
        "flagAtivo": true,
        "tipoConta": 1,
        "dataCriacao": "25/06/2020"
    },
    "valor": -15.00,
    "dataTransacao": "26/06/2020"
}'
```


### Implementar path que realiza o bloqueio de uma conta

```
curl --location --request PATCH 'http://localhost:8080/conta/{idConta}/bloqueio'
```


### Implementar path que recupera o extrato de transações de uma conta

```
curl --location --request GET 'http://localhost:8080/conta/321/extrato'
```


## Item diferencial

### Elaborar documentação
http://localhost:8080/swagger-ui.html#/
