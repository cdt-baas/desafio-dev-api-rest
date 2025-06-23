import { AccountStatus } from "@domain/entities/AccountEntity";
import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  ManyToOne,
  CreateDateColumn,
  DeleteDateColumn,
  JoinColumn,
} from "typeorm";
import HolderOrmEntity from "./HolderOrmEntity";

@Entity("accounts")
export default class AccountOrmEntity {
  @PrimaryGeneratedColumn()
  id: number;

  @ManyToOne(() => HolderOrmEntity, { nullable: false })
  @JoinColumn()
  holder: HolderOrmEntity;

  @Column({ type: "decimal", precision: 10, scale: 2, default: 0 })
  balance: string;

  @Column()
  agency: string;

  @Column()
  number: string;

  @Column({ type: "enum", enum: AccountStatus, default: AccountStatus.ACTIVE })
  status: AccountStatus;

  @Column({ type: "boolean", default: false })
  isBlocked: boolean;

  @CreateDateColumn({ type: "timestamp" })
  createdAt: Date;

  @DeleteDateColumn({ type: "timestamp", nullable: true })
  deletedAt: Date | null;
}
