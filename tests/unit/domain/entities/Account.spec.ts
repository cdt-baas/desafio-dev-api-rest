import { Account, AccountStatus } from "@domain/entities/AccountEntity";
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

const baseProps = {
  holderId: 123,
  balance: new Money(1000),
  agency: "0001",
  number: "123456",
  status: AccountStatus.ACTIVE,
  isBlocked: false,
};
describe("Account", () => {
  describe("account", () => {
    it("should create an account with default properties", () => {
      const account = new Account(baseProps);
      expect(account).toBeInstanceOf(Account);
      expect(account.holderId).toBeDefined();
      expect(account.balance).toEqual(baseProps.balance);
      expect(account.agency).toEqual(baseProps.agency);
      expect(account.number).toEqual(baseProps.number);
      expect(account.status).toEqual(baseProps.status);
      expect(account.isBlocked).toBeFalsy();
    });
    it("should create an account with all properties", () => {
      const allProps = {
        id: 123,
        holderId: 1234,
        balance: new Money(1000),
        agency: "000-1",
        number: "12345-6",
        status: AccountStatus.ACTIVE,
        isBlocked: false,
        createdAt: new Date(),
        deletedAt: null,
      };
      const account = new Account(allProps);
      expect(account.id).toEqual(allProps.id);
      expect(account.holderId).toBeDefined();
      expect(account.balance).toEqual(allProps.balance);
      expect(account.agency).toEqual("0001");
      expect(account.number).toEqual("123456");
      expect(account.status).toEqual(allProps.status);
      expect(account.isBlocked).toBeFalsy();
      expect(account.createdAt).toEqual(allProps.createdAt);
      expect(account.deletedAt).toEqual(allProps.deletedAt);
    });
  });

  describe("deposit", () => {
    it("should deposit a valid amount", () => {
      const account = new Account({ ...baseProps, balance: new Money(100) });
      account.deposit(new Money(200));
      expect(account.balance.toNumber()).toBe(300);
    });

    it("should throw if account is closed", () => {
      const account = new Account({
        ...baseProps,
        status: AccountStatus.CLOSED,
      });
      expect(() => account.deposit(new Money(100))).toThrow(AccountClosedError);
    });

    it("should throw if account is blocked", () => {
      const account = new Account({ ...baseProps, isBlocked: true });
      expect(() => account.deposit(new Money(100))).toThrow(
        AccountBlockedError,
      );
    });

    it("should throw if deposit amount is zero or negative", () => {
      const account = new Account(baseProps);
      expect(() => account.deposit(Money.zero())).toThrow(
        InvalidDepositAmountError,
      );
      expect(() => account.deposit(new Money(-10))).toThrow(
        InvalidDepositAmountError,
      );
    });
  });

  describe("withdraw", () => {
    it("should withdraw a valid amount", () => {
      const account = new Account({ ...baseProps, balance: new Money(500) });
      account.withdraw(new Money(200), Money.zero());
      expect(account.balance.toNumber()).toBe(300);
    });

    it("should throw AccountClosedError if account is closed", () => {
      const account = new Account({
        ...baseProps,
        status: AccountStatus.CLOSED,
      });
      expect(() => account.withdraw(new Money(100), Money.zero())).toThrow(
        AccountClosedError,
      );
    });

    it("should throw AccountBlockedError if account is blocked", () => {
      const account = new Account({ ...baseProps, isBlocked: true });
      expect(() => account.withdraw(new Money(100), Money.zero())).toThrow(
        AccountBlockedError,
      );
    });

    it("should throw InvalidWithdrawAmountError for zero or negative amount", () => {
      const account = new Account({ ...baseProps });
      expect(() => account.withdraw(Money.zero(), Money.zero())).toThrow(
        InvalidWithdrawAmountError,
      );
      expect(() => account.withdraw(new Money(-50), Money.zero())).toThrow(
        InvalidWithdrawAmountError,
      );
    });

    it("should throw InsufficientBalanceError if withdraw amount exceeds balance", () => {
      const account = new Account({ ...baseProps, balance: new Money(100) });
      expect(() => account.withdraw(new Money(150), Money.zero())).toThrow(
        InsufficientBalanceError,
      );
    });

    it("should throw InsufficientBalanceError if balance is zero", () => {
      const account = new Account({ ...baseProps, balance: Money.zero() });
      expect(() => account.withdraw(new Money(10), Money.zero())).toThrow(
        InsufficientBalanceError,
      );
    });

    it("should throw DailyLimitExceededError if daily limit is exceeded", () => {
      const account = new Account({ ...baseProps, balance: new Money(3000) });
      expect(() => account.withdraw(new Money(1500), new Money(600))).toThrow(
        DailyLimitExceededError,
      );
    });

    it("should allow withdraw when total withdrawn equals daily limit exactly", () => {
      const account = new Account({ ...baseProps, balance: new Money(3000) });
      const alreadyWithdrawn = new Money(1500);
      const withdrawAmount = new Money(500);
      account.withdraw(withdrawAmount, alreadyWithdrawn);
      expect(account.balance.toNumber()).toBe(
        new Money(3000).minus(withdrawAmount).toNumber(),
      );
    });
  });

  describe("block/unblock", () => {
    it("should block the account", () => {
      const account = new Account(baseProps);
      account.block();
      expect(account.isBlocked).toBe(true);
    });

    it("should not block the account if it is already blocked", () => {
      const account = new Account({ ...baseProps, isBlocked: true });
      expect(() => account.block()).toThrow(AccountBlockedError);
    });

    it("should unblock the account", () => {
      const account = new Account({ ...baseProps, isBlocked: true });
      account.unblock();
      expect(account.isBlocked).toBe(false);
    });

    it("should not unblock the account if it is already unblock", () => {
      const account = new Account({ ...baseProps, isBlocked: false });
      expect(() => account.unblock()).toThrow(AccountUnBlockedError);
    });
  });

  describe("close", () => {
    it("should close the account", () => {
      const account = new Account({ ...baseProps, balance: Money.zero() });
      account.close();
      expect(account.status).toBe(AccountStatus.CLOSED);
    });

    it("should throw if account is already closed", () => {
      const account = new Account({
        ...baseProps,
        status: AccountStatus.CLOSED,
      });
      expect(() => account.close()).toThrow(AccountClosedError);
    });
    it("should not allow close if account balance is positive", () => {
      const account = new Account({ ...baseProps, balance: new Money(100) });
      expect(() => account.close()).toThrow(AccountBalancePositiveError);
    });
  });
});
