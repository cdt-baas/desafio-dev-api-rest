export class InsufficientBalanceError extends Error {
  statusCode: number;
  constructor() {
    super(`Saldo insuficiente para realizar a operação`);
    this.name = "InsufficientBalanceError";
    this.statusCode = 422;
  }
}
