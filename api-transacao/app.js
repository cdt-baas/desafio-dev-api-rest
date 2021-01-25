const express = require('express');
const app = express();

app.use(express.json()); // support json encoded bodies

require("./routes/api-routes.js")(app);

const port = 3001;

app.listen(port, () => console.log(`api-transacao esta ouvindo na porta: ${port}!`));