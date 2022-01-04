const mongoose = require("mongoose");
const url = `mongodb://mongo:27017/mongo-dock`;

const connect = async () => {
  mongoose
  .connect(url, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .catch((error) => {
    console.log("MongoDB Error:", error);
  });
}

module.exports = { connect }