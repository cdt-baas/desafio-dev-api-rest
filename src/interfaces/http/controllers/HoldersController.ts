import { NextFunction, Request, Response } from "express";
import { CreateHolder } from "@application/usecases/CreateHolder";
import { HolderRepository } from "@domain/repositories/holderRepository";
import { DeleteHolder } from "@application/usecases/DeleteHolder";

export class HoldersController {
  constructor(private readonly repository: HolderRepository) {}

  async create(req: Request, res: Response, next: NextFunction) {
    try {
      const { fullName, cpf } = req.body;
      const createHolder = new CreateHolder(this.repository);
      await createHolder.execute({ fullName, cpf });
      res.status(201).send();
    } catch (error) {
      next(error);
    }
  }
  async remove(req: Request, res: Response, next: NextFunction) {
    try {
      const { id } = req.params;
      const deleteHolder = new DeleteHolder(this.repository);
      await deleteHolder.execute(Number(id));
      res.status(204).json({ message: "Portador removido com sucesso" });
    } catch (error) {
      next(error);
    }
  }
}
