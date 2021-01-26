const axios = require('axios').default;

const API_CONTAS_URL = "http://api-contas:3000/conta/";

const API_ATUALIZA_SALDO = API_CONTAS_URL + "atualizarSaldo";

module.exports.getConta = async (idConta) => {

    const response = await axios.get(API_CONTAS_URL + idConta);

    return response;

}

module.exports.putAtualizaSaldo = async (data) => {
    try {
        const response = await axios.put(API_ATUALIZA_SALDO, data);

        return response.status;
    } catch (error) {
        return error;
    }
}