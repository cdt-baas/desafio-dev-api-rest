# API Rest que realizam operações bancárias. Desafio-Docker-tech
 
## API REST usando framework Flask com SQLAlchemy e Marshmallow

### Instalar

```bash
# clone o repositório
$ https://github.com/msvasconcelos/desafio-dev-api-rest-docker.git
$ cd desafio-dev-api-rest-docker
```

**Crie um virtualenv e ative-o:**

```bash
$ python3 -m venv venv
$ . venv/bin/activate
```

**Instalar dependencias do projeto:**

```bash
$ python3 -m pip install --upgrade pip
$ pip install -r requirements.txt
```

***Não foi terminado a parte do docker, por isso para conectar ao banco de dados é nece
necessário conexão com mysql***

***Altere em config.py sua conexão com o banco***

```bash
SQLALCHEMY_DATABASE_URI = 'mysql://root@127.0.0.1:3306/docker'
```

**Rodar:**
```bash
$ python run.py
```

**Testes:**
***Está sendo utilizado o pytest***

***Os testes ainda estão bem manuais, mas o script já é o necessarios para rodar a primeira vez e ja deixar uma pequena base de dados***

***OBS: os campos de authorization estão setados manual com um codigo auth basic, basta gerar um via requisicao e alterar no codigo***
```bash
# para rodar o teste
$ py.test
```

**API Client:**

***Estou deixando meu workspace do Insominia para ajudar nas requisições(tambem pode ser utilizada no Postman)***

## Listar de todos os Endpoints da aplicação


### Listar usuários filtrando por nome

**Definição/Request**

`GET /v1/users?name=a`

**Response**

- `200 OK` ao ter sucesso




## Retornar um usuário especifico

`GET /v1/users/<id>`

**Response**

- `404 Not Found` usuário não existe

- `200 OK` ao ter sucesso


### Registrando novo usuário

**Definição/Request**

`POST /v1/users`

**Argumentos**

- `"username":string` usuário que será mostrado e feito para usar a api
- `"password":string` senha que será encriptada antes de ir para o banco
- `"name":string` nome do usuário
- `"email":string` email que será usado para comunicação

**Response**

- `201 Created` ao ter sucesso

- `200 Created` ao ter erro com usuário ou email existente

- `500 Internal error` ao ter erro com o servidor ou sistema


### Atualizando usuário

**Definição/Request**

`PUT /v1/users/<id>`

**Argumentos**

- `"username":string` usuário que será mostrado e feito para usar a api(eventualmente)
- `"password":string` senha que será encriptada antes de ir para o banco(eventualmente)
- `"name":string` nome do usuário
- `"email":string` email que será usado para comunicação(caso necessário)

**Response**

- `201 Created` ao ter sucesso

- `404 Not Found` usuário não existe

- `500 Internal error` ao ter erro com servidor ou sistema


### Deletar usuário

**Definição**

`DELETE /v1/users/<id>`

**Response**

- `200 No Content` ao ter sucesso

- `404 Not Found` usuário não existente

- `500 Internal error` erro com servidor ou sistema


### Autenticação do token com servidor JWT

`POST /v1/authenticate`

**No header do seu JavaScript será necessário passar os dados do usuário.**

***uthorization: 'Basic ' + btoa(username + ':' + password)***

**Response**

- `401 Not Found` caso não exista
- `200 OK` ao ter sucesso


### Cadastrar pessoa 
**Definição/Request**

`POST /v1/casdastrar-pessoa/?token=<token>`

**Argumentos**

- `"nome":string` nome da pessoa que será relação com a conta 
- `"cpf":string` campo cpf da pessoa
- `"dataNascimento":string` data de nascimento da pessoa

**Response**

- `201 Created` pessoa cadastrada com sucesso 

- `500 Internal error` ao ter erro com o servidor ou sistema


### Cadastrar conta
**Definição/Request**

`POST /v1/nova-conta/?token=<token>`

**Argumentos**

- `"idPessoa":numerico` id da pessoa vinculado a conta 
- `"limiteSaqueDiario":numerico` valor de limite de saque diario 
- `"tipoConta":numerico` informar o numero do tipo da conta 

**Response**

- `201 Created` conta criada com sucesso 

- `500 Internal error` ao ter erro com o servidor ou sistema


### Realizar deposito
**Definição/Request**

`PUT /v1/depositar/?token=<token>`

**Argumentos**

- `"idConta":numerico` id da conta que será realizado o deposito
- `"depositar":numerico` valor que será depositado 

**Response**

- `200 Created` deposito realizado 

- `404 Not found` conta não encontrada, tente novamente

- `500 Internal error` esta conta esta bloqueada, nao é possivel realizar deposito

- `500 Internal error` não foi possivel depositar, tente novamante


### Realizar saque
**Definição/Request**

`PUT /v1/saque/?token=<token>`

**Argumentos**

- `"idConta":numerico` id da conta que será realizado o saque
- `"saque":numerico` valor que será sacado 

**Response**

- `200 Created` saque realizado com sucesso

- `404 Not found` conta não encontrada, tente novamente

- `500 Internal error` esta conta esta bloqueada, nao é possivel realizar saque 

- `500 Internal error` voce nao tem limite para esse saque, consulte o limite e tente novamente

- `500 Internal error` voce nao tem saldo para esse saque, consulte o saldo e tente novamente

- `500 Internal error` não foi possivel sacar, tente novamante


### Consultar saldo
**Definição/Request**

`POST /v1/saldo/?token=<token>`

**Argumentos**

- `"idConta":numerico` id da conta que deseja verificar o saldo 

**Response**

- `200 Created` seu saldo é: <saldo>

- `404 Not found` conta não encontrada, tente novamente

- `500 Internal error` não foi possivel consultar o saldo, tente novamante


### Bloquear conta 
**Definição/Request**

`PUT /v1/bloquear/?token=<token>`

**Argumentos**

- `"idConta":numerico` id da conta que deseja bloquear 

**Response**

- `200 Created` conta bloqueada com sucesso

- `404 Not found` conta não encontrada, tente novamente

- `500 Internal error` não foi possivel bloquear, tente novamante


### Extrato da conta
**Definição/Request**

`POST /v1/extrato/?token=<token>`

**Argumentos**

- `"idConta":numerico` id da conta que deseja o extrato

**Response**

- `200 Created` extrato realizado

- `500 Internal error` não foi possivel realizar o extrato, tente novamante


### Extrato da conta por periodo
**Definição/Request**

`POST /v1/extrato-periodo/?token=<token>`

**Argumentos**

- `"idConta":numerico` id da conta que deseja o extrato
- `"data_inicio":data` data inicio do periodo do extrato 
- `"data_fim":data` data fim do periodo do extrato 

**Response**

- `200 Created` extrato por periodo realizado

- `500 Internal error` não foi possivel realizar extrato por periodo, tente novamante