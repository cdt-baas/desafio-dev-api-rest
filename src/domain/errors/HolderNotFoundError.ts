export class HolderNotFoundError extends Error {
  statusCode: number;
  constructor() {
    super(`Portador não encontrado`);
    this.name = "HolderNotFoundError";
    this.statusCode = 404;
  }
}
