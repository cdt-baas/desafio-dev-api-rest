export class AccountUnBlockedError extends Error {
  statusCode: number;
  constructor() {
    super(`Conta não está bloqueada`);
    this.name = "AccountUnBlockedError";
    this.statusCode = 403;
  }
}
