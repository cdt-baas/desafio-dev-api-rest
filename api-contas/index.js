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

app.get('/conta/:id', (req, res) => {
    let sql = 'SELECT * FROM contas WHERE idConta =?';
    
    db.query(sql, req.params.id, function(err, data, fields) {
      if (err) throw err;
      res.json({
        status: 200,
        data,
        message: "User lists retrieved successfully"
      })
    })
  });


app.listen(port, () => console.log(`Example app listening on port ${port}!`));('mysq')