export class AccountBlockedError extends Error {
  statusCode: number;
  constructor() {
    super(`Conta está bloqueada`);
    this.name = "AccountBlockedError";
    this.statusCode = 403;
  }
}
