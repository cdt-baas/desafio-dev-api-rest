# Cenário

A Dock está crescendo e expandindo seus negócios, gerando novas oportunidades de revolucionar o mercado financeiro e criar produtos diferenciados.
Nossa próxima missão é construir uma nova conta digital Dock para nossos clientes utilizarem através de endpoints, onde receberemos requisições em um novo backend que deverá gerenciar as contas e seus portadores (os donos das contas digitais).

# Requisitos

- Deve ser possível criar e remover **portadores**
    - Um **portador** deve conter apenas seu *nome completo* e *CPF*
    - O *CPF* deve ser válido e único no cadastro de **portadores**
- As **contas digital Dock** devem conter as seguintes funcionalidades:
    - A conta deve ser criada utilizando o *CPF* do **portador**
    - Uma conta deve ter seu *saldo*, *número* e *agência* disponíveis para consulta
    - Necessário ter funcionalidade para fazer a *consulta de extrato* da conta *por período*
    - Um **portador** pode fechar a **conta digital Dock** a qualquer instante
    - Executar as operações de *saque* e *depósito*
        - *Depósito* é liberado para todas as *contas ativas* e *desbloqueadas*
        - *Saque* é permitido para todas as *contas ativas* e *desbloqueadas* desde que haja *saldo disponível* e não ultrapasse o limite diário de *2 mil reais*

## Regulação obrigatória

- Precisamos *bloquear* e *desbloquear* a **conta digital Dock** a qualquer momento
- A **conta digital Dock** nunca poderá ter o *saldo negativo*


#  Orientações

Utilize qualquer uma das linguagens de programação:
- Java
- Javascript
- Typescript
- Python
- Kotlin
- Golang

Desenvolva o case seguindo as melhores práticas que julgar necessário, aplique todos os conceitos, se atente a qualidade, utilize toda e qualquer forma de governança de código válido. Vamos considerar toda e qualquer implementação, trecho de código, documentação e/ou intenção compartilhada conosco. Esperamos também que o desafio seja feito dentro do tempo disponibilizado e que esteja condizente com a posição pretendida.

É necessário ter o desafio 100% funcional contendo informações e detalhes sobre: como iniciar a aplicação, interagir com as funcionalidades disponíveis e qualquer outro ponto adicional.

## Diferenciais

- Práticas, padrões e conceitos de microservices será considerado um diferencial para nós por existir uma variedade de produtos e serviços dentro da Dock.
- Temos 100% das nossas aplicações e infraestrutura na nuvem, consideramos um diferencial, caso o desafio seja projeto para ser executado na nuvem.
- Nossos times são autônomos e têm liberdade para definir arquiteturas e soluções. Por este motivo será considerado diferencial toda: arquitetura, design, paradigma e documentação detalhando a sua abordagem.

### Instruções
      1. Faça o fork do desafio;
      2. Crie um repositório privado no seu github para o projeto e adicione como colaborador, os usuários informados no email pelo time de recrutameto ;
      3. Após concluir seu trabalho faça um push; 
      4. Envie um e-mail à pessoa que está mantendo o contato com você durante o processo notificando a finalização do desafio para validação.

# Iniciando projeto

Este projeto usa como base o docker e docker-compose, sendo a infraestrutura simples de implementar.

## Inicializando build
```bash
docker-compose build
```

### Inicializar o banco de dados
Para que as tabelas sejam criadas corretamente, na pasta infra/sql/database.sql possui o script geral pra criar as tabelas, possuindo tanbém a lista de script de migrations

Dessa forma para criar a estrutura deve-se iniciar o banco de dados
```bash
docker-compose up db
```

Em seguida utilize a sua ferramenta de SQL para conectar com o seu banco e rodar o script de estruturação

## Inicializando o projeto

```bash
docker-compose up api --build
```

# Endpoints

## Criar portador

```json
// POST /carrier
{
  "path": "/carrier",
  "body": {
    "cpf": "862.288.874-31",
    "name": "Victor Raton"
  },
  "response": {
    "id": "3eaf1072-6a83-45ce-8695-f68a63286479",
    "cpf": "862.288.875-31",
    "name": "Victor Raton"
  }
}
```

## Criar conta
```json
// POST /account
{
  "path": "/account",
  "body": {
    "cpf": "862.288.874-31",
    "agency":1
  },
  "response": {
    "id": "0221bba6-f03e-4bc6-9fd2-fb7a4193bc83",
    "cpf": "862.288.875-31",
    "carrier_id": "3eaf1072-6a83-45ce-8695-f68a63286479",
    "balance": 0,
    "status": 1,
    "agency": 1,
    "account_number": 1
  }
}
```

## Conta
### Depósito
```json
// PUT /account/:account/:agency/deposit
{
  "path": "/account/1/1/deposit",
  "body": {
    "value": 150.25
  },
  "response": {
    "id": "0221bba6-f03e-4bc6-9fd2-fb7a4193bc83",
    "cpf": "862.288.875-31",
    "carrier_id": "3eaf1072-6a83-45ce-8695-f68a63286479",
    "balance": 150.25,
    "status": 1,
    "agency": 1,
    "account_number": 1
  }
}
```
### Saque
```json
// PUT /account/:account/:agency/withdrawal
{
  "path": "/account/1/1/withdrawal",
  "body": {
    "value": 10.50
  },
  "response": {
      "id": "0221bba6-f03e-4bc6-9fd2-fb7a4193bc83",
      "cpf": "862.288.875-31",
      "carrier_id": "3eaf1072-6a83-45ce-8695-f68a63286479",
      "balance": 139.75,
      "status": 1,
      "agency": 1,
      "account_number": 1
  }
}
```

### Ativar/Desativar
```json
// PUT /account/:account/:agency
{
  "path": "/account/1/1",
  "body": {
    "status": 1 // 1 : ativar , 0 : desativar
  },
  "response": {
      "id": "0221bba6-f03e-4bc6-9fd2-fb7a4193bc83",
      "cpf": "862.288.875-31",
      "carrier_id": "3eaf1072-6a83-45ce-8695-f68a63286479",
      "balance": 139.75,
      "status": 1,
      "agency": 1,
      "account_number": 1
    }
}
```

## Transações
### Extrato
```json
// GET /transactiom/:account/:agency
{
  "path": "/account/1/1",
  "query": {
    "start_at": "2022-11-11",
    "end_at": "2023-01-16"
  },
  "response": {
    "total": -15,
    "transactions": [
      {
        "from": {
          "id": "4d5487ba-f715-40cc-8cdf-e0af7f1a289b",
          "account_number": 1,
          "agency": 1,
          "cpf": "862.288.875-48"
        },
        "to": {
          "id": "705746d1-70ad-42e0-b3cd-5a0f0b5d5de0",
          "account_number": 2,
          "agency": 1,
          "cpf": "862.288.875-48"
        },
        "value": 10,
        "created_at": "2023-01-09T21:15:30.36Z"
      },
      {
        "from": {
          "id": "4d5487ba-f715-40cc-8cdf-e0af7f1a289b",
          "account_number": 1,
          "agency": 1,
          "cpf": "862.288.875-48"
        },
        "to": {
          "id": "705746d1-70ad-42e0-b3cd-5a0f0b5d5de0",
          "account_number": 2,
          "agency": 1,
          "cpf": "862.288.875-48"
        },
        "value": 50,
        "created_at": "2023-01-09T21:15:30.36Z"
      },
      {
        "from": {
          "id": "705746d1-70ad-42e0-b3cd-5a0f0b5d5de0",
          "account_number": 2,
          "agency": 1,
          "cpf": "862.288.875-48"
        },
        "to": {
          "id": "4d5487ba-f715-40cc-8cdf-e0af7f1a289b",
          "account_number": 1,
          "agency": 1,
          "cpf": "862.288.875-48"
        },
        "value": 5,
        "created_at": "2023-01-12T21:15:30.360034Z"
      },
      {
        "from": null,
        "to": {
          "id": "4d5487ba-f715-40cc-8cdf-e0af7f1a289b",
          "account_number": 1,
          "agency": 1,
          "cpf": "862.288.875-48"
        },
        "value": 10,
        "created_at": "2023-01-13T05:02:46.041081Z"
      },
      {
        "from": null,
        "to": {
          "id": "4d5487ba-f715-40cc-8cdf-e0af7f1a289b",
          "account_number": 1,
          "agency": 1,
          "cpf": "862.288.875-48"
        },
        "value": 10,
        "created_at": "2023-01-13T05:02:51.90107Z"
      },
      {
        "from": null,
        "to": {
          "id": "4d5487ba-f715-40cc-8cdf-e0af7f1a289b",
          "account_number": 1,
          "agency": 1,
          "cpf": "862.288.875-48"
        },
        "value": 10,
        "created_at": "2023-01-13T05:03:03.595946Z"
      },
      {
        "from": null,
        "to": {
          "id": "4d5487ba-f715-40cc-8cdf-e0af7f1a289b",
          "account_number": 1,
          "agency": 1,
          "cpf": "862.288.875-48"
        },
        "value": 10,
        "created_at": "2023-01-13T05:03:03.771125Z"
      }
    ]
  }
}
```
### Transferência
```json
// GET /transactiom
{
  "path": "/account/1/1",
  "body": {
    "from": {
      "account_number": 1,
      "agency": 1
    },
    "to": {
      "account_number": 2,
      "agency": 1
    },
    "value": 10
  },
  "response": {
    "from": {
      "id": "4d5487ba-f715-40cc-8cdf-e0af7f1a289b",
      "cpf": "862.288.875-48",
      "carrier_id": "3193a6ab-ba33-4d84-8cab-f2e7932b649f",
      "balance": 105,
      "status": 1,
      "agency": 1,
      "account_number": 1
    },
    "to": {
      "id": "705746d1-70ad-42e0-b3cd-5a0f0b5d5de0",
      "cpf": "862.288.875-48",
      "carrier_id": "3193a6ab-ba33-4d84-8cab-f2e7932b649f",
      "balance": 11,
      "status": 1,
      "agency": 1,
      "account_number": 2
    },
    "value": 1
  }
}
```