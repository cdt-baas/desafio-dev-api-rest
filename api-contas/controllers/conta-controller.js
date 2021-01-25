const Conta = require("../models/conta-model.js");

exports.findOne = (req, res) => {
  Conta.findById(req.params.idConta, (err, data) => {
    if (err) {
      if (err.kind === "not_found") {
        res.status(404).send({
          message: `Nao foi encontrada a conta idConta ${req.params.idConta}.`
        });
      } else {
        res.status(500).send({
          message: "Erro ao retornar conta" + req.params.idConta
        });
      }
    } else res.send(data);
  });
};

exports.create = (req, res) => {
  // Validate request
  if (!req.body) {
    res.status(400).send({
      message: "Conteudo da requisicao esta vazio!"
    });
  }

  const conta = new Conta({
    idPessoa: req.body.idPessoa,
    saldo: req.body.saldo,
    limiteSaqueDiario: req.body.limiteSaqueDiario,
    flagAtivo: req.body.flagAtivo,
    tipoConta: req.body.tipoConta,
    dataCriacao: date = new Date()
  });

  Conta.create(conta, (err, data) => {
    if (err)
      res.status(500).send({
        message:
          err.message || "Ocorreu um erro na tentiva de criar a conta."
      });
    else res.send(data);
  });
};

//Retorna o saldo da conta
exports.getSaldo = (req, res) => {
  Conta.getSaldo(req.params.idConta, (err, data) => {
    if (err) {
      if (err.kind === "not_found") {
        res.status(404).send({
          message: `Nao foi encontrada a conta idConta ${req.params.idConta}.`
        });
      } else {
        res.status(500).send({
          message: "Erro ao retornar conta" + req.params.idConta
        });
      }
    } else res.send(data);
  });
};

//Inativa uma conta
exports.inativar = (req, res) => {
  Conta.inativar(req.params.idConta, (err, data) => {
    if (err) {
      if (err.kind === "not_found") {
        res.status(404).send({
          message: `Nao foi encontrada a conta idConta ${req.params.idConta}.`
        });
      } else {
        res.status(500).send({
          message: "Erro ao inativar conta" + req.params.idConta
        });
      }
    } else res.send(data);
  });
};

//Atualiza o saldo da conta
exports.atualizarSaldo = (req, res) => {

  Conta.atualizarSaldo(req.body.idConta, req.body.valorAtualizado, (err, data) => {
    if (err) {
      if (err.kind === "not_found") {
        res.status(404).send({
          message: `Nao foi encontrada a conta idConta ${req.params.idConta}.`
        });
      } else {
        res.status(500).send({
          message: "Erro ao realizar deposito na conta: " + req.params.idConta
        });
      }
    } else res.send(data);
  });
};