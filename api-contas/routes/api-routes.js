module.exports = app => {
    const contaController = require("../controllers/conta-controller.js");

    // Retrieve a single Customer with customerId
    app.get("/conta/:idConta", contaController.findOne);

    //Cria uma nova conta
    app.post("/conta", contaController.create);

    //Retorna o saldo da conta
    app.get("/conta/saldo/:idConta", contaController.getSaldo);

    //Inativa uma conta
    app.put("/conta/inativar/:idConta", contaController.inativar);

    app.put("/conta/atualizarSaldo", contaController.atualizarSaldo)
  };