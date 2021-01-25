const NodeCache = require("node-cache");

//Inicializa o cache padrao
const myCache = new NodeCache();

module.exports.getFromCache = async (key) => {
    return await myCache.get(key);
}

module.exports.setCache = (key, value) => {
    console.log("Inserindo dados no cache");
    myCache.set(key, value);
}