# Teste Dock - Wellison Victor Belusso

Implementada API para cadastro, deposito, saque e extrado de uma conta bancária.

### Inicialização em ambiente de desenvolvimento:

1. run `npm i`
2. run `node src/index.js`

### Inicialização com docker

1. run `docker build . -t api-dock`
2. run `docker run -p 3434:3434 -d api-dock`

## Documentação

1. http://127.0.0.1:3434/docs

Com a aplicação em execução, o endereço contém a documentação dos end-points e possibilita testar das rotinas(swagger).

## Banco de dados

O banco de dados escolhido foi SQLite, utilizado TypeORM para persistência e leitura dos dados. A criação das tabelas e feita automaticamente pelo TypeORM conforme os schemas definidos em `src/entity`.

## Considerações

Cadastrado pessoa com idPessoa = 1 automaticamente ao inicializar a aplicação;
Criada trigger "updateSaldo" na tabela transacoes, para atualização do saldo na tabela contas.
