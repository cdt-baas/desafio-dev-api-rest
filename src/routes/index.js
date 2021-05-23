const ContasCtrl = require('../controllers/conta.controller').ContaCtrl;
const PessoaCtrl = require('../controllers/pessoa.controller').PessoaCtrl;

const schemas = require('./schemas');

module.exports = function (fastify, opts, next) {
  fastify.get('/', { schema: schemas.status }, async function (request, repl) {
    repl.send({ status: true, message: 'API em execução' });
  });

  fastify.post('/criar-conta', { schema: schemas.create }, async function (request, reply) {
    const novaConta = request.body;
    novaConta.pessoa = await PessoaCtrl.get(novaConta.idPessoa);
    if (!novaConta.pessoa) {
      reply.status(500).send('Pessoa não encontrada');
      return;
    }
    const conta = await ContasCtrl.save(novaConta);

    if (!conta) {
      reply.status(500).send('Erro ao salvar conta');
    } else {
      reply.send({ idConta: conta.idConta, message: 'Conta criada com sucesso' });
    }
  });

  fastify.post('/saldo/:id', { schema: schemas.saldo }, async function (request, reply) {
    const conta = await ContasCtrl.get(request.params.id);
    if (!conta) {
      reply.status(500).send('Conta não encontrada');
    } else {
      reply.send({ saldo: conta.saldo });
    }
  });

  fastify.put('/deposito', { schema: schemas.operacao }, async function (request, reply) {
    let conta = await ContasCtrl.get(request.body.idConta);

    if (!conta) {
      reply.status(500).send('Conta não encontrada');
      return;
    }

    if (conta.flagAtivo == 0) {
      reply.status(500).send('Conta encontra-se bloqueada');
      return;
    }

    const deposito = await ContasCtrl.transacao({ conta: conta, valor: request.body.valor });
    if (!deposito) {
      reply.status(500).send('Erro ao fazer deposito');
    } else {
      conta = await ContasCtrl.get(request.body.idConta);
      reply.send({ status: true, message: 'O deposito efetuado com sucesso', saldo: conta.saldo });
    }
  });

  fastify.put('/saque', { schema: schemas.operacao }, async function (request, reply) {
    let conta = await ContasCtrl.get(request.body.idConta);

    if (!conta) {
      reply.status(500).send('Conta não encontrada');
      return;
    }
    if (conta.flagAtivo == 0) {
      reply.status(500).send('Conta encontra-se bloqueada');
      return;
    }

    if (conta.limiteSaqueDiario < request.body.valor) {
      reply.status(500).send('Excedeu o limite diário');
      return;
    }

    const deposito = await ContasCtrl.transacao({ conta: conta, valor: request.body.valor * -1 });
    if (!deposito) {
      reply.status(500).send('Erro ao fazer deposito');
    } else {
      conta = await ContasCtrl.get(request.body.idConta);
      reply.send({ status: true, message: 'O saque efetuado com sucesso', saldo: conta.saldo });
    }
  });

  fastify.put('/bloquear', { schema: schemas.bloquear }, async function (request, reply) {
    const conta = await ContasCtrl.get(request.body.idConta);
    if (!conta) {
      reply.status(500).send('Conta não encontrada');
      return;
    }

    const bloquear = await ContasCtrl.bloquear(request.body.idConta);
    if (!bloquear) {
      reply.status(500).send('Erro ao bloquear conta');
      return;
    }

    reply.send({ message: 'Conta bloqueada com sucesso' });
  });

  fastify.get('/extrato/:id', { schema: schemas.extrato }, async function (request, reply) {
    const conta = await ContasCtrl.get(request.params.id);
    if (!conta) {
      reply.status(500).send('Conta não encontrada');
      return;
    }

    const extrato = await ContasCtrl.extrato(conta);
    if (!extrato) {
      reply.status(500).send('Erro ao gerar extrato');
      return;
    }

    reply.send(extrato);
  });

  fastify.get('/extrato/:id/:inicial/:final', { schema: schemas.extratoPeriodo }, async function (request, reply) {
    const conta = await ContasCtrl.get(request.params.id);
    if (!conta) {
      reply.status(500).send('Conta não encontrada');
      return;
    }

    const extrato = await ContasCtrl.extrato(conta, request.params.inicial, request.params.final);
    if (!extrato) {
      reply.status(500).send('Erro ao gerar extrato');
      return;
    }

    reply.send(extrato);
  });

  next();
};
