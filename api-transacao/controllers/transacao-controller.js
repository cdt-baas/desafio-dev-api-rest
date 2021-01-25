const Transacao = require("../models/transacao-model.js");

exports.findAll = (req, res) => {
  Transacao.findAll(req.params.idConta, (err, data) => {
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

  if (!req.body) {
    res.status(400).send({
      message: "Conteudo da requisicao esta vazio!"
    });
  }


  const transacao = new Transacao({
    idConta: req.body.idConta,
    valor: req.body.valor,
    dataTransacao: date = new Date()

  });

  //Se o valor for positivo chamar o endopoint deposito
  if (transacao.valor > 0) {

    Transacao.deposito(transacao, (err, data) => {
      if (err)
        res.status(500).send({
          message:
            err.message || "Ocorreu um erro na tentiva de criar a transacao."
        });
      else res.send(data);
    })
  } else {

    Transacao.saque(transacao, (err, data) => {

      if (err) {
        if (err.kind != "") {
          res.status(400).send({
            message: err.kind
          });
        } else {
          res.status(500).send({
            message: "Erro criar transacao"
          });
        }
      } else res.send(data);
    });
  }

};