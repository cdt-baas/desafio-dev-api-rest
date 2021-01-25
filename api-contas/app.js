const express = require('express');
const app = express();

app.use(express.json());

require("./routes/api-routes.js")(app);

const port = 3000;

app.listen(port, () => console.log(`api-contas esta ouvindo na porta: ${port}!`));