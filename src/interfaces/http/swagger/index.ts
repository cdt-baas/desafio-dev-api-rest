export const swaggerDefinition = {
  openapi: "3.0.0",
  info: {
    title: "API Desafio",
    version: "1.0.0",
    description: "Todas as APIs utilizadas no desafio",
  },
  components: {
    schemas: {
      Account: {
        type: "object",
        required: ["cpf", "balance", "agency", "number"],
        properties: {
          id: {
            type: "integer",
            example: 10,
          },
          cpf: {
            type: "string",
            example: "12345678900",
          },
          balance: {
            type: "number",
            example: 1000.0,
          },
          agency: {
            type: "string",
            example: "0001",
          },
          number: {
            type: "string",
            example: "123456-7",
          },
          status: {
            type: "string",
            example: "active",
          },
        },
      },
      Transaction: {
        type: "object",
        required: ["id", "accountId", "type", "amount", "createdAt"],
        properties: {
          id: {
            type: "integer",
            example: 100,
          },
          accountId: {
            type: "integer",
            example: 10,
          },
          type: {
            type: "string",
            enum: ["deposit", "withdraw", "transfer"],
            example: "deposit",
          },
          amount: {
            type: "number",
            example: 200.0,
          },
          createdAt: {
            type: "string",
            format: "date-time",
            example: "2024-06-01T12:00:00Z",
          },
        },
      },
      GetAccountByAgencyAndNumber: {
        type: "object",
        required: ["agency", "number"],
        properties: {
          agency: {
            type: "string",
            example: "0001",
          },
          number: {
            type: "string",
            example: "123456-7",
          },
        },
      },
      ErrorResponse: {
        type: "object",
        required: ["status", "message"],
        properties: {
          status: {
            type: "integer",
            example: 404,
          },
          message: {
            type: "string",
            example: "Conta não encontrada",
          },
          details: {
            type: "string",
            example: "A conta informada não existe.",
          },
        },
      },
    },
  },
};
