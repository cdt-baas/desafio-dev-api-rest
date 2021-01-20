const express = require('express');
const app = express();

const mysql = require('mysql');

// setup database
db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'root',
    database: 'desafio_dev'
  })

const port = 3000;

app.get('/conta/saldo/:idConta', (req, res) => {
    let sql = 'SELECT saldo FROM contas WHERE idConta =?';
    
    db.query(sql, req.params.idConta, function(err, data, fields) {
      if (err) throw err;
      res.json({
        status: 200,
        data,
        message: "User lists retrieved successfully"
      })
    })
  });

  app.get('/conta/', (req, res) => {
    let sql = 'SELECT * FROM contas';
    
    db.query(sql, function(err, data, fields) {
      if (err) throw err;
      res.json({
        status: 200,
        data,
        message: "User lists retrieved successfully"
      })
    })
  });

app.post('/conta', (req, res) => {

  let sql = `INSERT INTO contas(idPessoa, saldo, limiteSaqueDiario, flagAtivo, tipoConta, dataCriacao) VALUES (?)`;
  let values = [
    req.body.idPessoa,
    req.body.saldo,
    req.body.limiteSaqueDiario,
    req.body.flagAtivo,
    req.body.tipoConta,
    date = new Date()
  ];
  console.log(values);
  db.query(sql, [values], function(err, data, fields) {
    if (err) throw err;
    res.json({
      status: 200,
      message: "Conta criada com sucesso."
    })
  })

})



app.listen(port, () => console.log(`Example app listening on port ${port}!`));