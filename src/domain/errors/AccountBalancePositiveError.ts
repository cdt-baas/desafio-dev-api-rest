export class AccountBalancePositiveError extends Error {
  statusCode: number;
  constructor() {
    super("Não é possível fechar uma conta com saldo positivo");
    this.name = "AccountBalancePositiveError";
    this.statusCode = 422;
  }
}
