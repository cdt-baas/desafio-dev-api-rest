export class InvalidDepositAmountError extends Error {
  statusCode: number;
  constructor() {
    super(`Valor do deposito deve ser maior que zero`);
    this.name = "InvalidDepositAmountError";
    this.statusCode = 400;
  }
}
