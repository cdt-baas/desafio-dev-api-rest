const Mongoose = require('mongoose')
const Schema = Mongoose.Schema
const ObjectId = Mongoose.Types.ObjectId

const transactionSchema = new Schema({
  idConta: {
    type: ObjectId,
    ref: 'conta',
    required: true
  },
  valor: {
    type: Number,
    default: 1
  },
  dataTransacao: {
    type: Date,
    default: new Date()
  }
})

module.exports = Mongoose.model('transacao', transactionSchema)
