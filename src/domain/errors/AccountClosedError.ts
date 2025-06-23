export class AccountClosedError extends Error {
  statusCode: number;
  constructor() {
    super(`Conta está fechada e não pode ser operada`);
    this.name = "AccountClosedError";
    this.statusCode = 422;
  }
}
