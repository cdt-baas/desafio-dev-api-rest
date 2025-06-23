import { NextFunction, Request, Response } from "express";
import { HolderRepository } from "@domain/repositories/holderRepository";
import { AccountRepository } from "@domain/repositories/accountRepository";
import { CreateAccount } from "@application/usecases/CreateAccount";
import { GetAccountByAgencyAndNumber } from "@application/usecases/GetAccountByAgencyAndNumber";
import { CloseAccount } from "@application/usecases/CloseAccount";
import { BlockAccount } from "@application/usecases/BlockAccount";
import { UnBlockAccount } from "@application/usecases/UnBlockAccount";
import { AccountTransactionRepository } from "@domain/repositories/accountTransactionRepository";
import { DepositByAccountId } from "@application/usecases/DepositByAccountId";
import { WithdrawByAccount } from "@application/usecases/WithdrawByAccount";
import { GetAccountTransactionsByPeriod } from "@application/usecases/GetAccountTransactionsByPeriod";

export class AccountsController {
  constructor(
    private readonly accountsRepository: AccountRepository,
    private readonly holderRepository: HolderRepository,
    private readonly accountTransactionRepository: AccountTransactionRepository,
  ) {}

  async create(req: Request, res: Response, next: NextFunction) {
    try {
      const useCase = new CreateAccount(
        this.accountsRepository,
        this.holderRepository,
      );
      await useCase.execute({
        cpf: req.body.cpf,
        balance: req.body.balance,
        agency: req.body.agency,
        number: req.body.number,
      });
      res.status(201).send();
    } catch (error) {
      next(error);
    }
  }

  async getByAgencyAndNumber(req: Request, res: Response, next: NextFunction) {
    try {
      const { agency, number } = req.params;
      const useCase = new GetAccountByAgencyAndNumber(this.accountsRepository);
      const account = await useCase.execute({
        agency,
        number,
      });
      res.json(account);
    } catch (error) {
      next(error);
    }
  }

  async close(req: Request, res: Response, next: NextFunction) {
    try {
      const accountId = Number(req.params.id);
      const useCase = new CloseAccount(this.accountsRepository);
      await useCase.execute(accountId);
      res.status(204).send();
    } catch (error) {
      next(error);
    }
  }

  async block(req: Request, res: Response, next: NextFunction) {
    try {
      const accountId = Number(req.params.id);
      const useCase = new BlockAccount(this.accountsRepository);
      await useCase.execute(accountId);
      res.status(204).send();
    } catch (error) {
      next(error);
    }
  }

  async unblock(req: Request, res: Response, next: NextFunction) {
    try {
      const accountId = Number(req.params.id);
      const useCase = new UnBlockAccount(this.accountsRepository);
      await useCase.execute(accountId);
      res.status(204).send();
    } catch (error) {
      next(error);
    }
  }

  async deposit(req: Request, res: Response, next: NextFunction) {
    try {
      const accountId = Number(req.params.id);
      const amount = Number(req.body.amount);
      const useCase = new DepositByAccountId(
        this.accountsRepository,
        this.accountTransactionRepository,
      );
      await useCase.execute(accountId, amount);
      res.status(204).send();
    } catch (error) {
      next(error);
    }
  }

  async withdraw(req: Request, res: Response, next: NextFunction) {
    try {
      const accountId = Number(req.params.id);
      const amount = Number(req.body.amount);

      const useCase = new WithdrawByAccount(
        this.accountsRepository,
        this.accountTransactionRepository,
      );
      await useCase.execute(accountId, amount);
      res.status(204).send();
    } catch (error) {
      next(error);
    }
  }

  async getTransactions(req: Request, res: Response, next: NextFunction) {
    try {
      const accountId = Number(req.params.id);
      const { startDate, endDate, page, size } = req.query;

      const useCase = new GetAccountTransactionsByPeriod(
        this.accountTransactionRepository,
      );
      const result = await useCase.execute({
        accountId,
        startDate: new Date(startDate as string),
        endDate: new Date(endDate as string),
        pagination: {
          page: Number(page) || 1,
          size: Number(size) || 10,
        },
      });
      res.json(result);
    } catch (error) {
      next(error);
    }
  }
}
