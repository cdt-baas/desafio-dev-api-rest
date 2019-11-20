# Dock API

## Como executar?
Instalar os pacotes de dependencia
```
pip install -r requirements.txt
python run.py
```


### ROTAS
* Path que realiza a criação de uma conta; 

    POST /v1/contas  
    
* Path que realiza operação de depósito em uma conta;

    PUT /v1/contas/<conta.id>/deposita 

* Path que realiza operação de saque em uma conta;

    PUT /v1/contas/<conta.id>/saque

* Path que realiza o bloqueio de uma conta;

    POST /v1/contas/{conta.id}/bloqueia

* Path que recupera o extrato de transações de uma conta;

    GET /v1/transacoes/<conta.id>
    

* Path que realiza operação de consulta de saldo em determinada conta;

    GET /v1/contas/<conta.id>/saldo
