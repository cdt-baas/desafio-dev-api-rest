const sql = require("../config/dbconfig");

// constructor
const Conta = function(conta) {
    this.idPessoa = conta.idPessoa;
    this.saldo = conta.saldo;
    this.limiteSaqueDiario = conta.limiteSaqueDiario;
    this.flagAtivo = conta.flagAtivo;
    this.tipoConta = conta.tipoConta;
    this.dataCriacao = conta.dataCriacao;
  };

Conta.findById =  (idConta, result) => {
  
    sql.query(`SELECT * FROM contas WHERE idconta = ${idConta}`, (err, res) => {
      if (err) {
        console.log("error: ", err);
        result(err, null);
        return;
      }
  
      if (res.length) {
        console.log("Conta encontrada: ", res[0]);
        result(null, res[0]);
        return;
      }
        
      result({ kind: "not_found" }, null);
    });
  
  };

Conta.create = (novaConta, result) => {
    sql.query("INSERT INTO contas SET ?", novaConta, (err, res) => {
      if (err) {
        console.log("error: ", err);
        result(err, null);
        return;
      }
  
      console.log("Nova conta criada: ", { id: res.insertId, ...novaConta });
      result(null, { idConta: res.insertId, ...novaConta });
    });
  };

Conta.getSaldo = (idConta, result) => {
     sql.query(`SELECT saldo FROM contas WHERE idconta = ${idConta}`, (err, res) => {
      if (err) {
        console.log("error: ", err);
        result(err, null);
        return;
      }
  
      if (res.length) {
        console.log("Saldo: ", res[0]);
        result(null, res[0]);
        return;
      }
        
      result({ kind: "not_found" }, null);
    });
      
  };

Conta.inativar = (idConta, result) => {
    sql.query(
      `UPDATE contas SET flagAtivo = 0 WHERE idConta = ${idConta}`,
      (err, res) => {
        if (err) {
          console.log("error: ", err);
          result(null, err);
          return;
        }
  
        if (res.affectedRows == 0) {
          // not found Customer with the id
          result({ kind: "not_found" }, null);
          return;
        }
  
        console.log("Conta inativada:  ", { id: idConta,});
        result(null, "Conta Inativada com sucesso.");
      }
    );
  };

  Conta.atualizarSaldo = (idConta, saldoAtualizado, result) => {
    sql.query(
      `UPDATE contas SET saldo = ${saldoAtualizado} WHERE idConta = ${idConta}`,
      (err, res) => {
        if (err) {
          console.log("error: ", err);
          result(null, err);
          return;
        }
  
        if (res.affectedRows == 0) {
          // not found Customer with the id
          result({ kind: "not_found" }, null);
          return;
        }
  
        console.log("Saldo atualizado  ", { res });
        result(null, "Saldo atualizado com sucesso.");
      }
    );
  };
 

  module.exports = Conta;