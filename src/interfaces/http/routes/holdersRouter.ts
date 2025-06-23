import { RequestHandler, Router } from "express";
import { HoldersController } from "../controllers/HoldersController";
import { AppDataSource } from "@infra/database/data-source";
import { TypeOrmHolderRepository } from "@infra/database/repositories/TypeOrmHolderRepository";
import { validateRequestMiddleware } from "../middlewares/validateRequestMiddleware";
import { createHolderSchema } from "../schemas/holderSchema";
import HolderOrmEntity from "@infra/database/entities/HolderOrmEntity";

const router = Router();

const holderRepository = new TypeOrmHolderRepository(
  AppDataSource.getRepository(HolderOrmEntity),
);
const holdersController = new HoldersController(holderRepository);

/**
 * @swagger
 * /api/holders:
 *   post:
 *     tags:
 *       - Portadores
 *     summary: Cria um novo portador
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required:
 *               - fullName
 *               - cpf
 *             properties:
 *               fullName:
 *                 type: string
 *                 example: "Maria Silva"
 *               cpf:
 *                 type: string
 *                 example: "12345678901"
 *     responses:
 *       201:
 *         description: Portador criado com sucesso
 *       400:
 *         description: Erro de validação
 */
router.post(
  "/",
  validateRequestMiddleware(createHolderSchema) as RequestHandler,
  holdersController.create.bind(holdersController),
);

/**
 * @swagger
 *  /api/holders/{id}:
 *   delete:
 *     tags:
 *       - Portadores
 *     summary: Remove um portador pelo ID
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID do portador
 *     responses:
 *       204:
 *         description: Portador removido com sucesso
 *       404:
 *         description: Portador não encontrado
 */
router.delete("/:id", holdersController.remove.bind(holdersController));

export default router;
