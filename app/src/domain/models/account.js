const Mongoose = require('mongoose')
const Schema = Mongoose.Schema
const ObjectId = Mongoose.Types.ObjectId

const accountSchema = new Schema({
  idPessoa: {
    type: ObjectId,
    ref: 'pessoa',
    required: true
  },
  saldo: {
    type: Number,
    default: 0
  },
  limiteSaqueDiario: {
    type: Number,
    default: 0
  },
  flagAtivo: {
    type: Boolean,
    default: true
  },
  tipoConta: {
    type: Number
  },
  dataCriacao: {
    type: Date,
    default: new Date()
  }
})

module.exports = Mongoose.model('conta', accountSchema)
