const mysql = require("mysql2");

const connection = mysql.createPool({
  host: 'mysql',
  user: 'root',
  database: 'desafio-dev',
  waitForConnections: true,
  connectionLimit: 10,
  password: 'password1234',
  queueLimit: 0
});

module.exports = connection;