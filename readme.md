# Dock Dev Api Challenge

Visando criar uma nova conta digital Dock, o Digital Account é uma API REST para cadastro e gerenciamento de contas. Desenvolvido com Spring Boot, Spring Data JPA e PostgreSQL.

## Funcionalidades
- ✅ **Criação de Contas**: Endpoint de criação de conta para usuário
- ✅ **Depósitos**: Endpoint para clientes depositarem valores em suas contas
- ✅ **Saques**: Endpoint para clientes realizarem saques de suas contas
- ✅ **Extratos**: Geração de extratos detalhados das movimentações na conta
- ✅ **Bloqueio/Desbloqueio de conta**: A conta pode bloqueada ou desbloqueada a qualquer momento

## Arquitetura

### Estrutura do Projeto 
```
src/main/java/br/com/desafio/digitalaccount/
├── config/             # Configurações (Controller Advice e Criptografia)
├── controllers/        # Controllers REST
├── domain/             # Classes de domínio da aplicação/Entidades JPA
├── dto/                # Data Transfer Objects
├── repositories/       # Repostórios JPA
├── services/           # Lógica do negócio
├── utils/              # Métodos utilitários
└── DigitalAccountApplication.java
```

## Tecnologias

- **Java 21** - Linguagem utilizada
- **Spring Boot 3.5.4** - Framework base
- **Spring Data JPA** - Persistência
- **PostgreSQL** - Banco de dados
- **Flyway** - Migrations do Banco de dados
- **Spring Boot Starter Validation** - Utilitário que simplifica validações
- **Spring Security Crypto** - Utilitário de criptografia
- **Spring Boot Actuator** - Monitoramento

## Monitoramento

### Health Check
```http
GET /actuator/health
```

### Métricas
```http
GET /actuator/metrics
```

### Swagger UI
```http 
GET /swagger-ui.html
```

## Endpoints

### Portadores
```http
# Criar portador
POST /account-holders
Content-Type: application/json
Body: 
{
   "name": "John Doe",
   "cpf": "XXX.YYY.ZZZ-WW"
}


# Excluir portador
DELETE /account-holders
Content-Type: application/json
Body: 
{
   "name": "John Doe",
   "cpf": "XXX.YYY.ZZZ-WW"
}

*Informar um CPF válido
```

### Contas
```http
# Criar conta
POST /account
Content-Type: application/json
Body: 
{
    "name": "John Doe",
    "cpf": "XXX.YYY.ZZZ-WW",
    "initialBalance": 0.00
}

DELETE /account
Content-Type: application/json
Body: {
    "number": "862151-9",
    "cpf": "XXX.YYY.ZZZ-WW"
}

# Bloquear conta
PUT /account/block
Content-Type: application/json
Body: {
    "accountNumber": "862151-9",
    "accountHolderCpf": "XXX.YYY.ZZZ-WW"
}

# Desbloquear conta
PUT /account/unblock
Content-Type: application/json
Body: {
    "accountNumber": "862151-9",
    "accountHolderCpf": "XXX.YYY.ZZZ-WW"
}

# Realizar Depósito
POST /account/deposit
Content-Type: application/json
Body: {
    "accountNumber": "551361-8",
    "accountHolderCpf": "XXX.YYY.ZZZ-WW",
    "amount": "1.00"
}

# Realizar Saque
POST /account/withdraw
Content-Type: application/json
Body: {
    "accountNumber": "551361-8",
    "accountHolderCpf": "XXX.YYY.ZZZ-WW",
    "amount": "20.00"
}


# Consulta Extrato
POST /account/statement
Content-Type: application/json
Body: {
    "accountNumber": "862151-9",
    "accountHolderCpf": "XXX.YYY.ZZZ-WW",
    "startDate": "2025-08-02",
    "endDate": "2025-08-04" 
}

*Informar CPF Válido
```

## Execução

### Desenvolvimento
````bash
git clone <repo-url>
cd digital-account

# Executar com Gradle
./gradlew bootRun
````

### Docker(Opcional)
````bash
docker build -t digital-account .

# Via docker-compose, vai rodar imagem do PostgreSQL junto
docker compose up
````

### Desenvolvimento com Banco de Dados (Obrigatório)
````bash
#PostgreSQL e PgAdmin(Opcional)
docker compose -f docker-compose-light.yaml 
```` 