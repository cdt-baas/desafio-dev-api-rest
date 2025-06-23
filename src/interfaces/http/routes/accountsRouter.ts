import { RequestHandler, Router } from "express";
import { AppDataSource } from "@infra/database/data-source";
import { TypeOrmHolderRepository } from "@infra/database/repositories/TypeOrmHolderRepository";
import { validateRequestMiddleware } from "../middlewares/validateRequestMiddleware";
import HolderOrmEntity from "@infra/database/entities/HolderOrmEntity";
import { TypeOrmAccountRepository } from "@infra/database/repositories/TypeOrmAccountRepository";
import AccountOrmEntity from "@infra/database/entities/AccountOrmEntity";
import { AccountsController } from "../controllers/AccountsController";
import {
  createAccountSchema,
  depositSchema,
  getTransactionsSchema,
  withdrawSchema,
} from "../schemas/accountSchema";
import { TypeOrmAccountTransactionRepository } from "@infra/database/repositories/TypeOrmAccountTransactionRepository";
import AccountTransactionOrmEntity from "@infra/database/entities/AccountTransactionOrmEntity";

const router = Router();

const accountRepository = new TypeOrmAccountRepository(
  AppDataSource.getRepository(AccountOrmEntity),
);
const holderRepository = new TypeOrmHolderRepository(
  AppDataSource.getRepository(HolderOrmEntity),
);
const accountTransactionRepository = new TypeOrmAccountTransactionRepository(
  AppDataSource.getRepository(AccountTransactionOrmEntity),
  AppDataSource.getRepository(AccountOrmEntity),
);
const accountsController = new AccountsController(
  accountRepository,
  holderRepository,
  accountTransactionRepository,
);

/**
 * @swagger
 * /api/accounts:
 *   post:
 *     tags:
 *       - Contas
 *     summary: Cria uma nova conta bancária
 *     requestBody:
 *        required: true
 *        content:
 *          application/json:
 *            schema:
 *              type: object
 *              required:
 *                - cpf
 *                - agency
 *                - number
 *                - balance
 *              properties:
 *                cpf:
 *                  type: string
 *                  example: "12345678901"
 *                agency:
 *                  type: string
 *                  example: "1234"
 *                number:
 *                  type: string
 *                  example: "567890"
 *                balance:
 *                  type: number
 *                  format: float
 *                  example: 1000.00
 *     responses:
 *       201:
 *         description: Conta criada com sucesso
 *       400:
 *         description: Erro de validação
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/ErrorResponse'
 */
router.post(
  "/",
  validateRequestMiddleware(createAccountSchema) as RequestHandler,
  accountsController.create.bind(accountsController),
);

/**
 * @swagger
 * /api/accounts/{id}/transactions:
 *   get:
 *     tags:
 *       - Contas
 *     summary: Lista as transações da conta em um período
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID da conta
 *       - in: query
 *         name: startDate
 *         required: true
 *         schema:
 *           type: string
 *           format: date
 *         description: Data inicial do período
 *       - in: query
 *         name: endDate
 *         required: true
 *         schema:
 *           type: string
 *           format: date
 *         description: Data final do período
 *       - in: query
 *         name: page
 *         required: false
 *         schema:
 *           type: integer
 *         description: Página da paginação
 *       - in: query
 *         name: size
 *         required: false
 *         schema:
 *           type: integer
 *         description: Tamanho da página
 *     responses:
 *       200:
 *         description: Lista de transações retornada com sucesso
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 $ref: '#/components/schemas/Transaction'
 *       404:
 *         description: Conta não encontrada
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/ErrorResponse'
 */
router.get(
  "/:id/transactions",
  validateRequestMiddleware(getTransactionsSchema) as RequestHandler,
  accountsController.getTransactions.bind(accountsController),
);

/**
 * @swagger
 * /api/accounts/{agency}/{number}:
 *   get:
 *     tags:
 *       - Contas
 *     summary: Busca conta por agência e número
 *     parameters:
 *       - in: path
 *         name: agency
 *         required: true
 *         schema:
 *           type: string
 *         description: Agência da conta
 *       - in: path
 *         name: number
 *         required: true
 *         schema:
 *           type: string
 *         description: Número da conta
 *     responses:
 *       200:
 *         description: Conta encontrada
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Account'
 *       404:
 *         description: Conta não encontrada
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/ErrorResponse'
 */
router.get(
  "/:agency/:number",
  accountsController.getByAgencyAndNumber.bind(accountsController),
);

/**
 * @swagger
 * /api/accounts/{id}/close:
 *   delete:
 *     tags:
 *       - Contas
 *     summary: Encerra uma conta bancária
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID da conta
 *     responses:
 *       204:
 *         description: Conta encerrada com sucesso
 *       404:
 *         description: Conta não encontrada
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/ErrorResponse'
 */
router.delete("/:id/close", accountsController.close.bind(accountsController));

/**
 * @swagger
 * /api/accounts/{id}/block:
 *   post:
 *     tags:
 *       - Contas
 *     summary: Bloqueia uma conta bancária
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID da conta
 *     responses:
 *       204:
 *         description: Conta bloqueada com sucesso
 *       404:
 *         description: Conta não encontrada
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/ErrorResponse'
 */
router.post("/:id/block", accountsController.block.bind(accountsController));

/**
 * @swagger
 * /api/accounts/{id}/unblock:
 *   post:
 *     tags:
 *       - Contas
 *     summary: Desbloqueia uma conta bancária
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID da conta
 *     responses:
 *       204:
 *         description: Conta desbloqueada com sucesso
 *       404:
 *         description: Conta não encontrada
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/ErrorResponse'
 */
router.post(
  "/:id/unblock",
  accountsController.unblock.bind(accountsController),
);

/**
 * @swagger
 * /api/accounts/{id}/deposit:
 *   post:
 *     tags:
 *       - Contas
 *     summary: Realiza um depósito em uma conta bancária
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID da conta
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               amount:
 *                 type: number
 *                 example: 100.00
 *     responses:
 *       204:
 *         description: Depósito realizado com sucesso
 *       404:
 *         description: Conta não encontrada
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/ErrorResponse'
 */
router.post(
  "/:id/deposit",
  validateRequestMiddleware(depositSchema) as RequestHandler,
  accountsController.deposit.bind(accountsController),
);

/**
 * @swagger
 * /api/accounts/{id}/withdraw:
 *   post:
 *     tags:
 *       - Contas
 *     summary: Realiza um saque em uma conta bancária
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID da conta
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               amount:
 *                 type: number
 *                 example: 50.00
 *     responses:
 *       204:
 *         description: Saque realizado com sucesso
 *       404:
 *         description: Conta não encontrada
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/ErrorResponse'
 */
router.post(
  "/:id/withdraw",
  validateRequestMiddleware(withdrawSchema) as RequestHandler,
  accountsController.withdraw.bind(accountsController),
);

export default router;
