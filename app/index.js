const express = require("express");
const app = express();
const routes = require("./src/main/routes");
const bodyParser = require("body-parser");
const port = 3000;
const mongoose = require("mongoose");
const url = `mongodb://mongo:27017/mongo-dock`;

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use("/api", routes);

mongoose
  .connect(url, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .then((connect) => {
    app.listen(port, () => {
      console.log(`Running on port ${port}`);
    });
  })
  .catch((error) => {
    console.log("MongoDB Error:", error);
  });
