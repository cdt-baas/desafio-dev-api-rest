**Instruções para poder rodar o projeto**
O projeto precisa ter um banco de dados mysql criado com a tabela 'dock' feita para poder fazer a população de dados iniciais.

1. Configuração da conexão com o banco no arquivo: `api/config/config.json`
2. Instalar projeto: `npm install`
3. Rodar em dev: `npm run start:dev`
4. Criação de modelos definidos: `npx sequelize-cli db:migrate`
5. População de registros: `npx sequelize-cli db:seed:all`
6. Para testes de todas as rotas feitas no mesmo projeto está a collection de postman com todos os casos requisitados, importar a colecction e rodar. 