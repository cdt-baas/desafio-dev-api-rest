module.exports = app => {
    const transacaoController = require("../controllers/transacao-controller.js");

    // Retorna todas as transacoes de uma conta
    app.get("/transacao/:idConta", transacaoController.findAll);

    //Cria uma nova transacao
    app.post("/transacao", transacaoController.create);

  };