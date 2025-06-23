import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  ManyToOne,
} from "typeorm";
import AccountOrmEntity from "./AccountOrmEntity";
import { TransactionType } from "@domain/entities/AccountTransactionEntity";

@Entity("account_transactions")
export default class AccountTransactionOrmEntity {
  @PrimaryGeneratedColumn()
  id: number;

  @ManyToOne(() => AccountOrmEntity, { nullable: false })
  account: AccountOrmEntity;

  @Column({ type: "enum", enum: TransactionType })
  type: TransactionType;

  @Column({ type: "decimal", precision: 10, scale: 2 })
  amount: string;

  @CreateDateColumn({ type: "timestamp" })
  createdAt: Date;
}
