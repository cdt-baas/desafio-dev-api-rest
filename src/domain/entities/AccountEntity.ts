import {
  InvalidDepositAmountError,
  InvalidWithdrawAmountError,
  AccountBlockedError,
  InsufficientBalanceError,
  AccountClosedError,
  DailyLimitExceededError,
  AccountBalancePositiveError,
  AccountUnBlockedError,
} from "@domain/errors";
import { Money } from "@domain/valueObject/Money";

export interface IAccount {
  id?: number;
  holderId: number;
  balance: Money;
  agency: string;
  number: string;
  createdAt?: Date;
  deletedAt?: Date | null;
  status: AccountStatus;
  isBlocked: boolean;
}

export enum AccountStatus {
  ACTIVE = "ACTIVE",
  CLOSED = "CLOSED",
}

const DAILY_WITHDRAW_LIMIT = new Money(2000);

export class Account {
  id?: number;
  holderId: number;
  balance: Money;
  agency: string;
  number: string;
  createdAt?: Date;
  deletedAt?: Date | null;
  status: AccountStatus;
  isBlocked: boolean;

  constructor(props: IAccount) {
    this.id = props.id;
    this.holderId = props.holderId;
    this.balance = props.balance;
    this.agency = props.agency.replace(/\D/g, "");
    this.number = props.number.replace(/\D/g, "");
    this.createdAt = props.createdAt;
    this.deletedAt = props.deletedAt;
    this.status = props.status;
    this.isBlocked = props.isBlocked;
  }

  deposit(amount: Money) {
    if (this.status !== AccountStatus.ACTIVE) {
      throw new AccountClosedError();
    }
    if (this.isBlocked) {
      throw new AccountBlockedError();
    }
    if (amount.lessThanOrEqualTo(Money.zero())) {
      throw new InvalidDepositAmountError();
    }
    this.balance = this.balance.plus(amount);
  }

  withdraw(amount: Money, totalWithDrawnToday: Money) {
    if (this.status !== AccountStatus.ACTIVE) {
      throw new AccountClosedError();
    }
    if (this.isBlocked) {
      throw new AccountBlockedError();
    }
    if (amount.lessThanOrEqualTo(Money.zero())) {
      throw new InvalidWithdrawAmountError();
    }
    if (amount.greaterThan(this.balance)) {
      throw new InsufficientBalanceError();
    }
    const newBalance = this.balance.minus(amount);
    if (newBalance.isNegative()) {
      throw new InsufficientBalanceError();
    }
    const newTotalForDay = totalWithDrawnToday.plus(amount);
    if (newTotalForDay.greaterThan(DAILY_WITHDRAW_LIMIT)) {
      throw new DailyLimitExceededError(DAILY_WITHDRAW_LIMIT.toNumber());
    }
    this.balance = newBalance;
  }

  block() {
    if (this.isBlocked) {
      throw new AccountBlockedError();
    }
    this.isBlocked = true;
  }

  unblock() {
    if (!this.isBlocked) {
      throw new AccountUnBlockedError();
    }
    this.isBlocked = false;
  }

  close() {
    if (this.status === AccountStatus.CLOSED) {
      throw new AccountClosedError();
    }
    if (this.balance.greaterThan(Money.zero())) {
      throw new AccountBalancePositiveError();
    }
    this.status = AccountStatus.CLOSED;
    this.deletedAt = new Date();
  }
}
