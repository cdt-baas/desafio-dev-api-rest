module.exports = {
  status: {
    description: 'Verificar status da API',
    tags: ['API Status'],
    response: {
      200: {
        type: 'object',
        properties: {
          status: { type: 'boolean' },
          message: { type: 'string' },
        },
      },
    },
  },

  create: {
    description: 'Cadastrar uma conta',
    tags: ['Contas'],
    response: {
      200: {
        type: 'object',
        properties: {
          idConta: { type: 'number' },
          message: { type: 'string' },
        },
      },
    },
    body: {
      type: 'object',
      properties: {
        idPessoa: { type: 'number', default: 1 },
        saldo: { type: 'number' },
        limiteSaqueDiario: { type: 'number', default: 50 },
        flagAtivo: { type: 'boolean', default: true },
        tipoConta: { type: 'number' },
      },
      required: ['idPessoa', 'limiteSaqueDiario', 'flagAtivo', 'tipoConta'],
    },
  },

  operacao: {
    description: 'Deposito/Saque em uma conta',
    tags: ['Contas'],
    response: {
      200: {
        type: 'object',
        properties: {
          message: { type: 'string' },
          saldo: { type: 'number' },
        },
      },
    },
    body: {
      type: 'object',
      properties: {
        idConta: { type: 'number' },
        valor: { type: 'number', minimum: 0.1 },
      },
      required: ['idConta', 'valor'],
    },
  },

  saldo: {
    description: 'Obter saldo de uma conta',
    tags: ['Contas'],
    response: {
      200: {
        type: 'object',
        properties: {
          saldo: { type: 'number' },
        },
      },
    },
    params: {
      id: { type: 'integer' },
    },
  },

  bloquear: {
    description: 'Bloquear uma conta',
    tags: ['Contas'],
    response: {
      200: {
        type: 'object',
        properties: {
          message: { type: 'string' },
        },
      },
    },
    body: {
      type: 'object',
      properties: {
        idConta: { type: 'number' },
      },
      required: ['idConta'],
    },
  },
  extrato: {
    description: 'Obter o extrato completo de uma conta',
    tags: ['Contas'],
    response: {
      200: {
        description: 'Extrato completo',
        type: 'array',
        items: {
          type: 'object',
          properties: {
            operacao: { type: 'string' },
            valor: { type: 'number' },
            data: { type: 'string' },
          },
        },
      },
    },
    params: {
      id: { type: 'integer' },
    },
  },
  extratoPeriodo: {
    description: 'Obter o extrato de um período',
    tags: ['Contas'],
    response: {
      200: {
        description: 'Extrato período',
        type: 'array',
        items: {
          type: 'object',
          properties: {
            operacao: { type: 'string' },
            valor: { type: 'number' },
            data: { type: 'string' },
          },
        },
      },
    },
    params: {
      id: { type: 'integer' },
      inicial: { type: 'string' },
      final: { type: 'string' },
    },
  },
};
