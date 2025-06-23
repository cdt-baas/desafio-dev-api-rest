import { Money } from "@domain/valueObject/Money";

export enum TransactionType {
  DEPOSIT = "DEPOSIT",
  WITHDRAW = "WITHDRAW",
}

export interface IAccountTransaction {
  id?: number;
  accountId: number;
  type: TransactionType;
  amount: Money;
  createdAt?: Date;
}

export class AccountTransaction {
  public readonly id?: number;
  public readonly accountId: number;
  public readonly type: TransactionType;
  public readonly amount: Money;
  public readonly createdAt?: Date;

  constructor(props: IAccountTransaction) {
    this.id = props.id;
    this.accountId = props.accountId;
    this.type = props.type;
    this.amount = props.amount;
    this.createdAt = props.createdAt;
  }
}
