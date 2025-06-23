export class AccountNotFoundError extends Error {
  statusCode: number;
  constructor() {
    super(`Conta não encontrada ao consultar ou operar`);
    this.name = "AccountNotFoundError";
    this.statusCode = 404;
  }
}
