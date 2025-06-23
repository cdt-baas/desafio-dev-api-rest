export class DailyLimitExceededError extends Error {
  statusCode: number;
  constructor(dailyLimit: number) {
    const formattedLimit = new Intl.NumberFormat("pt-BR", {
      style: "currency",
      currency: "BRL",
    }).format(dailyLimit);
    super(`Valor excede o limite diário de saque de ${formattedLimit}`);
    this.name = "DailyLimitExceededError";
    this.statusCode = 422;
  }
}
