export class InvalidCpfError extends Error {
  statusCode: number;
  constructor() {
    super("CPF inválido. Deve conter exatamente 11 dígitos.");
    this.name = "InvalidCpfError";
    this.statusCode = 400;
  }
}
