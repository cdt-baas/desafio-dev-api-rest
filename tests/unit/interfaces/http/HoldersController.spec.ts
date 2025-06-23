import { NextFunction, Request, Response } from "express";
import { HoldersController } from "@interfaces/http/controllers/HoldersController";
import { HolderRepository } from "@domain/repositories/holderRepository";
import { CreateHolder } from "@application/usecases/CreateHolder";
import { DeleteHolder } from "@application/usecases/DeleteHolder";
const mockRepository = {
  create: jest.fn(),
  deleteById: jest.fn(),
  findById: jest.fn(),
  findByCpf: jest.fn(),
} as HolderRepository;

describe("Holders Controller", () => {
  let controller: HoldersController;
  let response: Response;
  let next: NextFunction;

  beforeEach(() => {
    response = {
      send: jest.fn().mockReturnThis(),
      status: jest.fn().mockReturnThis(),
      json: jest.fn().mockReturnThis(),
    } as any;
    next = jest.fn();
    controller = new HoldersController(mockRepository);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("should be defined", () => {
    expect(controller).toBeDefined();
    expect(next).toBeDefined();
    expect(response).toBeDefined();
  });

  describe("/ POST", () => {
    it("should create a holder", async () => {
      const executeSpy = jest
        .spyOn(CreateHolder.prototype, "execute")
        .mockResolvedValue();
      const body = {
        fullName: "John Doe",
        cpf: "12345678901",
      };
      await controller.create(
        {
          body,
        } as Request,
        response,
        next,
      );
      expect(response.status).toHaveBeenCalledWith(201);
      expect(response.send).toHaveBeenCalled();
      expect(executeSpy).toHaveBeenCalledWith(body);
      expect(executeSpy).toHaveBeenCalledTimes(1);
    });

    it("should handle errors when creating a holder", async () => {
      const expectedError = new Error("Database error");
      const executeSpy = jest
        .spyOn(CreateHolder.prototype, "execute")
        .mockRejectedValue(expectedError);
      await controller.create(
        {
          body: {
            fullName: "John Doe",
            cpf: "1234",
          },
        } as Request,
        response,
        next,
      );
      expect(next).toHaveBeenCalledWith(expectedError);
      expect(executeSpy).toHaveBeenCalledTimes(1);
    });
  });
  describe("/:id DELETE", () => {
    it("should delete a holder", async () => {
      const executeSpy = jest
        .spyOn(DeleteHolder.prototype, "execute")
        .mockResolvedValue();
      await controller.remove({ params: { id: "1" } } as any, response, next);
      expect(response.status).toHaveBeenCalledWith(204);
      expect(response.json).toHaveBeenCalledWith({
        message: "Portador removido com sucesso",
      });
      expect(executeSpy).toHaveBeenCalledWith(1);
      expect(executeSpy).toHaveBeenCalledTimes(1);
    });

    it("should handle errors when deleting a holder", async () => {
      const expectedError = new Error("Database error");
      const executeSpy = jest
        .spyOn(DeleteHolder.prototype, "execute")
        .mockRejectedValue(expectedError);
      await controller.remove({ params: { id: "1" } } as any, response, next);
      expect(next).toHaveBeenCalledWith(expectedError);
      expect(executeSpy).toHaveBeenCalledTimes(1);
    });
  });
});
