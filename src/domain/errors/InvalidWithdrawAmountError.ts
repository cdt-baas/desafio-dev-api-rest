export class InvalidWithdrawAmountError extends Error {
  statusCode: number;
  constructor() {
    super(`Valor do saque deve ser maior que zero`);
    this.name = "InvalidWithdrawAmountError";
    this.statusCode = 400;
  }
}
