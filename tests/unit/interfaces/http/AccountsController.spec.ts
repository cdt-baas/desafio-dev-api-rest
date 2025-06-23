import { NextFunction, Request, Response } from "express";
import { HolderRepository } from "@domain/repositories/holderRepository";
import { AccountRepository } from "@domain/repositories/accountRepository";
import { AccountsController } from "@interfaces/http/controllers/AccountsController";
import { CreateAccount } from "@application/usecases/CreateAccount";
import { GetAccountByAgencyAndNumber } from "@application/usecases/GetAccountByAgencyAndNumber";
import { CloseAccount } from "@application/usecases/CloseAccount";
import { UnBlockAccount } from "@application/usecases/UnBlockAccount";
import { BlockAccount } from "@application/usecases/BlockAccount";
import { DepositByAccountId } from "@application/usecases/DepositByAccountId";
import { AccountTransactionRepository } from "@domain/repositories/accountTransactionRepository";
import { Money } from "@domain/valueObject/Money";
import { WithdrawByAccount } from "@application/usecases/WithdrawByAccount";
import {
  GetAccountTransactionsByPeriod,
  GetAccountTransactionsByPeriodResponse,
} from "@application/usecases/GetAccountTransactionsByPeriod";
import { AccountTransaction } from "@domain/entities/AccountTransactionEntity";

const mockHolderRepository = {
  create: jest.fn(),
  deleteById: jest.fn(),
  findById: jest.fn(),
  findByCpf: jest.fn(),
} as HolderRepository;

const mockAccountRepository = {
  create: jest.fn(),
  findByAgencyAndNumber: jest.fn(),
  save: jest.fn(),
  findById: jest.fn(),
} as AccountRepository;

const mockAccountTransactionRepository = {
  create: jest.fn(),
  findByAccountAndPeriod: jest.fn(),
  getTotalAmountByTypeAndPeriod: jest.fn(),
} as AccountTransactionRepository;

describe("Accounts Controller", () => {
  let controller: AccountsController;
  let response: Response;
  let next: NextFunction;

  beforeEach(() => {
    response = {
      send: jest.fn().mockReturnThis(),
      status: jest.fn().mockReturnThis(),
      json: jest.fn().mockReturnThis(),
    } as any;
    next = jest.fn();
    controller = new AccountsController(
      mockAccountRepository,
      mockHolderRepository,
      mockAccountTransactionRepository,
    );
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("should be defined", () => {
    expect(controller).toBeDefined();
    expect(next).toBeDefined();
    expect(response).toBeDefined();
  });

  describe("create", () => {
    it("should create an account", async () => {
      const body = {
        cpf: "12345678901",
        balance: 0,
        agency: "0001",
        number: "123456",
      };

      const executeSpy = jest
        .spyOn(CreateAccount.prototype, "execute")
        .mockResolvedValue();

      await controller.create({ body } as Request, response, next);

      expect(response.status).toHaveBeenCalledWith(201);
      expect(response.send).toHaveBeenCalled();
      expect(executeSpy).toHaveBeenCalledWith(body);
    });
    it("should handle errors when creating an account", async () => {
      const body = {
        cpf: "123",
        balance: 0,
        agency: "0001",
        number: "123456",
      };
      const expectedError = new Error("Database error");
      const executeSpy = jest
        .spyOn(CreateAccount.prototype, "execute")
        .mockRejectedValue(expectedError);
      await controller.create({ body } as Request, response, next);
      expect(executeSpy).toHaveBeenCalledWith(body);
      expect(next).toHaveBeenCalledWith(expectedError);
    });
  });

  describe("getByAgencyAndNumber", () => {
    it("should get account by agency and number", async () => {
      const mockResponse = {
        id: 1,
        balance: new Money(1000).toString(),
        agency: "0001",
        number: "123456",
      };

      const executeSpy = jest
        .spyOn(GetAccountByAgencyAndNumber.prototype, "execute")
        .mockResolvedValue(mockResponse);

      await controller.getByAgencyAndNumber(
        { params: { agency: "0001", number: "123456" } } as unknown as Request,
        response,
        next,
      );

      expect(response.json).toHaveBeenCalledWith(mockResponse);
      expect(executeSpy).toHaveBeenCalledWith({
        agency: "0001",
        number: "123456",
      });
    });

    it("should handle errors when getting account by agency and number", async () => {
      const expectedError = new Error("Account not found");
      const executeSpy = jest
        .spyOn(GetAccountByAgencyAndNumber.prototype, "execute")
        .mockRejectedValue(expectedError);

      await controller.getByAgencyAndNumber(
        { params: { agency: "0001", number: "123456" } } as unknown as Request,
        response,
        next,
      );

      expect(next).toHaveBeenCalledWith(expectedError);
      expect(executeSpy).toHaveBeenCalled();
    });
  });

  describe("close", () => {
    it("should close an account", async () => {
      const executeSpy = jest
        .spyOn(CloseAccount.prototype, "execute")
        .mockResolvedValue();

      await controller.close(
        { params: { id: "1" } } as unknown as Request,
        response,
        next,
      );

      expect(executeSpy).toHaveBeenCalledWith(1);
      expect(response.status).toHaveBeenCalledWith(204);
      expect(response.send).toHaveBeenCalled();
    });

    it("should handle errors when closing an account", async () => {
      const expectedError = new Error("Account not found");
      const executeSpy = jest
        .spyOn(CloseAccount.prototype, "execute")
        .mockRejectedValue(expectedError);

      await controller.close(
        { params: { id: "1" } } as unknown as Request,
        response,
        next,
      );
      expect(executeSpy).toHaveBeenCalledWith(1);
      expect(next).toHaveBeenCalledWith(expectedError);
    });
  });

  describe("unblock", () => {
    it("should unblock an account", async () => {
      const executeSpy = jest
        .spyOn(UnBlockAccount.prototype, "execute")
        .mockResolvedValue();

      await controller.unblock(
        { params: { id: "1" } } as unknown as Request,
        response,
        next,
      );

      expect(executeSpy).toHaveBeenCalledWith(1);
      expect(response.status).toHaveBeenCalledWith(204);
      expect(response.send).toHaveBeenCalled();
    });

    it("should handle errors when unblock an account", async () => {
      const expectedError = new Error("Account not found");
      const executeSpy = jest
        .spyOn(UnBlockAccount.prototype, "execute")
        .mockRejectedValue(expectedError);

      await controller.unblock(
        { params: { id: "1" } } as unknown as Request,
        response,
        next,
      );
      expect(executeSpy).toHaveBeenCalledWith(1);
      expect(next).toHaveBeenCalledWith(expectedError);
    });
  });

  describe("block", () => {
    it("should block an account", async () => {
      const executeSpy = jest
        .spyOn(BlockAccount.prototype, "execute")
        .mockResolvedValue();

      await controller.block(
        { params: { id: "1" } } as unknown as Request,
        response,
        next,
      );

      expect(executeSpy).toHaveBeenCalledWith(1);
      expect(response.status).toHaveBeenCalledWith(204);
      expect(response.send).toHaveBeenCalled();
    });

    it("should handle errors when unblock an account", async () => {
      const expectedError = new Error("Account not found");
      const executeSpy = jest
        .spyOn(BlockAccount.prototype, "execute")
        .mockRejectedValue(expectedError);

      await controller.block(
        { params: { id: "1" } } as unknown as Request,
        response,
        next,
      );
      expect(executeSpy).toHaveBeenCalledWith(1);
      expect(next).toHaveBeenCalledWith(expectedError);
    });
  });

  describe("deposit", () => {
    it("should deposit into an account", async () => {
      const executeSpy = jest
        .spyOn(DepositByAccountId.prototype, "execute")
        .mockResolvedValue();

      const req = {
        params: { id: "1" },
        body: { amount: 100 },
      } as unknown as Request;

      await controller.deposit(req, response, next);

      expect(executeSpy).toHaveBeenCalledWith(1, 100);
      expect(response.status).toHaveBeenCalledWith(204);
      expect(response.send).toHaveBeenCalled();
    });

    it("should handle errors when depositing into an account", async () => {
      const expectedError = new Error("Deposit failed");
      const executeSpy = jest
        .spyOn(DepositByAccountId.prototype, "execute")
        .mockRejectedValue(expectedError);

      const req = {
        params: { id: "1" },
        body: { amount: 100 },
      } as unknown as Request;

      await controller.deposit(req, response, next);

      expect(executeSpy).toHaveBeenCalledWith(1, 100);
      expect(next).toHaveBeenCalledWith(expectedError);
    });
  });

  describe("withdraw", () => {
    it("should withdraw to an account", async () => {
      const executeSpy = jest
        .spyOn(WithdrawByAccount.prototype, "execute")
        .mockResolvedValue();

      const req = {
        params: { id: "1" },
        body: { amount: 100 },
      } as unknown as Request;

      await controller.withdraw(req, response, next);

      expect(executeSpy).toHaveBeenCalledWith(1, 100);
      expect(response.status).toHaveBeenCalledWith(204);
      expect(response.send).toHaveBeenCalled();
    });

    it("should handle errors when withdrawing to an account", async () => {
      const expectedError = new Error("Withdraw failed");
      const executeSpy = jest
        .spyOn(WithdrawByAccount.prototype, "execute")
        .mockRejectedValue(expectedError);

      const req = {
        params: { id: "1" },
        body: { amount: 100 },
      } as unknown as Request;

      await controller.withdraw(req, response, next);

      expect(executeSpy).toHaveBeenCalledWith(1, 100);
      expect(next).toHaveBeenCalledWith(expectedError);
    });
  });

  describe("getTransactions", () => {
    it("should return transactions with correct result", async () => {
      const accountId = 1;
      const startDate = "2023-10-01";
      const endDate = "2023-10-31";
      const page = "2";
      const size = "5";
      const mockResult = {
        data: [
          {
            id: 1,
            accountId: 1,
            type: "DEPOSIT",
            amount: 500,
            createdAt: new Date("2023-10-15T10:00:00Z"),
          },
          {
            id: 2,
            accountId: 1,
            type: "WITHDRAW",
            amount: 200,
            createdAt: new Date("2023-10-20T12:00:00Z"),
          },
        ] as unknown as AccountTransaction[],
        pagination: {
          total: 2,
          totalPages: 1,
          currentPage: 2,
          pageSize: 5,
        },
      } as GetAccountTransactionsByPeriodResponse;
      const executeSpy = jest
        .spyOn(GetAccountTransactionsByPeriod.prototype, "execute")
        .mockResolvedValue(mockResult);

      const req = {
        params: { id: accountId.toString() },
        query: { startDate, endDate, page, size },
      } as unknown as Request;

      await controller.getTransactions(req, response, next);

      expect(executeSpy).toHaveBeenCalledWith({
        accountId,
        startDate: new Date(startDate),
        endDate: new Date(endDate),
        pagination: { page: Number(page), size: Number(size) },
      });
      expect(response.json).toHaveBeenCalledWith(mockResult);
    });

    it("should handle errors when retrieving transactions", async () => {
      const accountId = 1;
      const startDate = "2023-10-01";
      const endDate = "2023-10-31";
      const req = {
        params: { id: accountId.toString() },
        query: { startDate, endDate, page: "1", size: "10" },
      } as unknown as Request;

      const expectedError = new Error("Transaction retrieval failed");
      jest
        .spyOn(GetAccountTransactionsByPeriod.prototype, "execute")
        .mockRejectedValue(expectedError);

      await controller.getTransactions(req, response, next);

      expect(next).toHaveBeenCalledWith(expectedError);
    });
  });
});
