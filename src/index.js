const fastify = require('fastify')({ logger: false });
const typeorm = require('typeorm');
const AutoLoad = require('fastify-autoload');
const path = require('path');

const PessoaCtrl = require('./controllers/pessoa.controller').PessoaCtrl;
const ContaCtrl = require('./controllers/conta.controller').ContaCtrl;

(async function () {
  // cria o banco de dados conforme os schemas em src/entity
  // por questoes de praticidade foi utilizado sqlite, configurado em ormconfig.json
  await typeorm.createConnection();

  // cadastro Pessoa Padrão, com idPessoa = 1
  await PessoaCtrl.save(1, 'Pessoa Padrão', '000.000.000-11', '1992-10-04');

  // criacao de Trigger para atualizar automaticamente o saldo
  await ContaCtrl.init();

  fastify
    .register(require('fastify-swagger'), {
      exposeRoute: true,
      routePrefix: '/docs',
      swagger: {
        info: {
          title: 'Dock - Wellison Victor Belusso',
          version: '1.0.0',
        },
        host: '127.0.0.1:3434',
        consumes: ['application/json'],
        produces: ['application/json'],
        tags: [
          {
            name: 'API Status',
            description: 'URIs para verificar status da api',
          },
          {
            name: 'Contas',
            description: 'URIs para manipulação dos dados das contas',
          },
        ],
      },
    })
    .register(require('fastify-cors'))
    .register(AutoLoad, {
      dir: path.join(__dirname, 'routes'),
      options: { prefix: '/api' },
    });

  // inicializa a API
  await fastify.listen(3434, '0.0.0.0', function (err, address) {
    if (err) {
      console.log('Erro ao inicializar API. ' + err);
      process.exit(1);
    }

    console.log(`API inicializada: ${address}/api`);
    console.log(`API documentação: ${address}/docs`);
  });
})();
