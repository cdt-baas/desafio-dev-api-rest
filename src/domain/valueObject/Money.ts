import Decimal from "decimal.js";

export class Money {
  private value: Decimal;

  constructor(amount: number | string | Decimal) {
    this.value = new Decimal(amount);
  }

  plus(amount: Money): Money {
    return new Money(this.value.plus(amount.value));
  }

  minus(amount: Money): Money {
    return new Money(this.value.minus(amount.value));
  }

  greaterThan(amount: Money): boolean {
    return this.value.greaterThan(amount.value);
  }

  lessThanOrEqualTo(amount: Money): boolean {
    return this.value.lessThanOrEqualTo(amount.value);
  }

  isNegative(): boolean {
    return this.value.isNegative();
  }

  toString(): string {
    return this.value.toFixed(2);
  }

  toNumber(): number {
    return this.value.toNumber();
  }

  static zero(): Money {
    return new Money(0);
  }
}
