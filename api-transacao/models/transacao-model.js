const sql = require("../config/dbconfig");

const api_contas = require("../helpers/request-handler");

const cache = require("../helpers/cache-handler");

// constructor
const Transacao = function (transacao) {
    this.idConta = transacao.idConta;
    this.valor = transacao.valor;
    this.dataTransacao = transacao.dataTransacao;
};

Transacao.findAll = (idConta, result) => {

    sql.query(`SELECT * FROM transacoes WHERE idConta = ${idConta}`, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(err, null);
            return;
        }

        if (res.length) {
            console.log("Transacoes encontradas ", res);
            result(null, res);
            return;
        }

        result({ kind: "not_found" }, null);
    });

};

Transacao.deposito = async (novaTransacao, result) => {

    try {
        const conta = await api_contas.getConta(novaTransacao.idConta);

        let saldoAtual = conta.data.saldo;
        let valorDeposito = novaTransacao.valor
        let idConta = novaTransacao.idConta;

        let saldoAtualizado = +saldoAtual + +valorDeposito;

        console.log("Saldo Atualizado: " + saldoAtualizado);

        let atualizarSaldo = { idConta: idConta, valorAtualizado: saldoAtualizado };

        try {
            response = await api_contas.putAtualizaSaldo(atualizarSaldo);

        } catch (error) {
            result(err, null);
        }

        sql.query("INSERT INTO transacoes SET ?", novaTransacao, (err, res) => {
            if (err) {
                console.log("error: ", err);
                result(err, null);
                return;
            }

            console.log("Nova conta criada: ", { id: res.insertId, ...novaTransacao });
            result(null, { idConta: res.insertId, ...novaTransacao });
        });

    } catch (error) {
        console.log(error);
        result({ kind: error }, null);
    }

};

Transacao.saque = async (novaTransacao, result) => {

    try {
        const conta = await api_contas.getConta(novaTransacao.idConta);

        let idConta = novaTransacao.idConta;
        let flagAtivo = conta.data.flagAtivo;
        let limiteSaqueDiario = await defineValorLimiteDiario(conta.data.limiteSaqueDiario, idConta);
        let saldo = conta.data.saldo;
        let valorSaque = novaTransacao.valor;

        console.log("Limite saque: " + limiteSaqueDiario);

        let novoLimite = +limiteSaqueDiario + +valorSaque;

        let isSaquePermitido = verificaSaquePermitido(flagAtivo, valorSaque, saldo, limiteSaqueDiario);

        if (isSaquePermitido == "") {

            let saldoAtualizado = +saldo + +valorSaque;

            console.log("Saldo Atualizado: " + saldoAtualizado);

            await api_contas.putAtualizaSaldo({ idConta: idConta, valorAtualizado: saldoAtualizado });

            sql.query("INSERT INTO transacoes SET ?", novaTransacao, (err, res) => {
                if (err) {
                    console.log("error: ", err);
                    result(err, null);
                    return;
                }

                //Atualiza limite no cache.    
                cache.setCache(idConta, novoLimite);

                console.log("Novo saque efetuado com sucesso: ", { id: res.insertId, ...novaTransacao });
                result(null, { idConta: res.insertId, ...novaTransacao });
            });



        } else {

            result({ kind: isSaquePermitido }, null);

        }

    } catch (error) {
        console.log(error);
        if(error.response.data !=undefined){
            result({ kind: error.response.data }, null);
        }else{
            result({ kind: error }, null); 
        }
    }

};

function contaAtiva(flagAtivo) {
    if (flagAtivo == 1) {
        console.log("Conta Ativa!")
        return true;
    } else {
        console.log("Conta Bloqueada!");
        return false
    }
}

function valorDentroDoLimiteDiario(valorSaque, limiteSaqueDiario) {
    //Utilziar um cache para verificar quanto ja consumiu no dia
    if (Math.abs(valorSaque) < limiteSaqueDiario) {
        console.log("Valor dentro do limite diario permitido");
        return true;

    } else {

        console.log("Saque acima do limite permitido");
        return false;
    }
}

function saldoSuficiente(valorSaque, saldo) {
    if (Math.abs(valorSaque) <= saldo) {
        console.log("Saldo suficiente");
        return true;
    } else {
        console.log("Saldo insuficiente");
        return false;
    }
}

function verificaSaquePermitido(flagAtivo, valorSaque, saldo, limiteSaqueDiario) {
    if (!contaAtiva(flagAtivo)) {
        return "Conta Bloqueada!";
    }
    if (!valorDentroDoLimiteDiario(valorSaque, limiteSaqueDiario)) {
        return "Valor do saque excede o limite diario";
    }
    if (!saldoSuficiente(valorSaque, saldo)) {
        return "Saldo insufisciente";
    }

    return "";

}

///O objetivo desta funciton e retornar o limite diario
///Se nao encontrado no cache o valor assumido sera o retornado do banco
async function defineValorLimiteDiario(limiteSaqueDiarioDB, idConta) {
    const limiteCache = await cache.getFromCache(idConta);
    if (limiteCache == undefined) {
        return limiteSaqueDiarioDB
    } else {
        return limiteCache;
    }
}

module.exports = Transacao;