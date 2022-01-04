const Mongoose = require('mongoose')
const Schema = Mongoose.Schema

const personSchema = new Schema({
  name: {
    type: String,
    required: true
  },
  cpf: {
    type: String,
    required: true
  },
  dataNascimento: {
    type: Date,
    required: true
  }
})

module.exports = Mongoose.model('pessoa', personSchema)
