const express = require('express');
const app = express();

app.use(express.json()); // support json encoded bodies
app.use(express.urlencoded({ extended: false })); // support encoded bodies

const mysql = require('mysql');

// setup database
db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'password1234',
    database: 'desafio-dev'
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
  
  db.query(sql, [values], function(err, data, fields) {
    if (err) throw err;
    res.json({
      status: 200,
      message: "Conta criada com sucesso."
    })
  })

})

app.put('/conta/inativar/:idConta', (req, res) => {
  let sql = 'UPDATE contas SET flagAtivo = 0 WHERE idConta =?';
  
  db.query(sql, req.params.idConta, function(err, data, fields) {
    if (err) throw err;
    res.json({
      status: 200,
      message: "Conta inativada com sucesso."
    })
  })
});

// Api de depositos
app.post('/conta/deposito', (req, res) => {

    let valor = req.body.valor;
    const idConta = req.body.idConta;

  new Promise ((resolve, reject) =>{
    
    let sql = 'SELECT saldo FROM contas WHERE idConta =?';
    
    db.query(sql, idConta, function(err, data, fields) {
      if (err) reject(err);
      resolve(data[0].saldo)

    })
  }).then(value => {
      console.log(value);
      let saldoAtualizado = atualizaSaldo(value, valor);
      return saldoAtualizado
  }).then(saldoAtualizado=> {
    let sql2 = 'UPDATE contas SET saldo =? WHERE idConta =?';

  let values = [
    saldoAtualizado,
    idConta
  
  ]
  console.log('Values insinde the query ' + values)
  
  db.query(sql2, values, function(err, data, fields) {
    if (err) throw err;
    res.json({
      status: 200,
      message: "Desposito realizado com sucesso"
    })
  })
  }).catch(err => {
    console.log(err);
  });
    
  
});

function atualizaSaldo (saldoAtual, valor){
  console.log('SaldoAtual: ' + saldoAtual);
  return saldoAtual + valor;
}


app.listen(port, () => console.log(`Example app listening on port ${port}!`));