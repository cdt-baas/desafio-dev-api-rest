# Desafio Dev API REST

API RESTful para gerenciamento de portadores (holders), contas bancárias e transações.

---

## Tecnologias Utilizadas

- **Node.js** (TypeScript)
- **Express** (framework web)
- **TypeORM** (ORM para banco de dados relacional)
- **PostgreSQL** (banco de dados relacional)
- **Jest** e **Supertest** (testes automatizados)
- **Swagger** (documentação da API)
- **Docker** e **Docker Compose** (ambiente de desenvolvimento/testes)

---

## Estrutura do Projeto

```
src/
  interfaces/http/         # Rotas, controllers, middlewares, schemas e documentação Swagger
  infra/database/          # Configuração do banco e repositórios TypeORM
  application/usecases/    # Casos de uso (Application Service Layer)
  domain/                  # Entidades e interfaces de domínio (Domain Layer)
tests/
  unit/                    # Testes unitários
.docker/                   # Arquivos de configuração Docker (secrets)
```

---

## Padrões de Projeto Utilizados

- **Domain-Driven Design (DDD):** Separação clara entre camadas de domínio, aplicação, infraestrutura e interfaces.
- **Repository Pattern:** Abstração do acesso a dados, facilitando testes e manutenção.
- **Service Layer:** Casos de uso centralizam regras de negócio.
- **Injeção de Dependência:** Facilita testes (mock de repositórios) e desacoplamento.
- **Middlewares Express:** Para validação e tratamento de erros.

---

## Configuração e Execução

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd desafio-dev-api-rest
```

### 2. Configure as variáveis de ambiente

Copie o arquivo `.env-example` para `.env` e preencha os valores necessários:

```bash
cp .env-example .env
```

### 3. Rodando com Docker

O projeto já está pronto para rodar com Docker Compose, incluindo banco de dados PostgreSQL:

```bash
docker-compose up --build
```

- A API estará disponível em: `http://localhost:3000`
- O banco de dados estará disponível em: `localhost:5432` (usuário e senha conforme secrets em `.docker/secrets/`)

### 4. Rodando localmente (sem Docker)

Certifique-se de ter um banco PostgreSQL rodando e as variáveis de ambiente configuradas.

```bash
npm install
npm run dev
```

---

## Testes

Execute todos os testes automatizados com:

```bash
npm test
```

---

## Documentação da API

A documentação interativa (Swagger) estará disponível em:

```
http://localhost:3000/api/docs
```

---

## Por que usar essas tecnologias e padrões?
Optei pelo PostgreSQL como banco de dados devido à sua robustez, desempenho e confiabilidade na garantia de consistência dos dados.

Na camada de aplicação, utilizei o Express com TypeScript, por ser um framework leve, amplamente utilizado no mercado e de fácil compreensão para times que precisarão dar manutenção ou evoluir o projeto.

Além disso, segui princípios de arquitetura limpa (Clean Architecture) e apliquei padrões de design que deixam o código mais organizado, modular e facilmente testável, favorecendo a manutenção futura e a escalabilidade.

---

## Integração Contínua e Deploy (CI/CD)

Este projeto utiliza uma pipeline de integração contínua (CI) configurada via GitHub Actions, localizada em `.github/workflows/ci.yml`.
Essa Pipeline executa automaticamente com test, build e deploy.

### Etapas da pipeline:
- Instalação das dependências
- Execução dos testes
- Validação de lint e qualidade de código
- Deploy automatizado

---


## Algumas duvidas que surgiram a respeito do desafio:
- Quando um portador é removido, o que deve acontecer com as contas vinculadas?
- O número da conta e a agência devem ser únicos por sistema ou apenas únicos dentro de cada agência?
- Durante o processo de fechar conta, devemos manter o histórico de transações (extratos) acessível?
- Existe alguma limitação ou validação sobre o formato dos números de agência e conta?
