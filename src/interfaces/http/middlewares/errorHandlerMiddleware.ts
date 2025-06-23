import { ZodError, ZodIssue } from "zod";
import { ErrorRequestHandler } from "express";
import { QueryFailedError } from "typeorm";
import { logger } from "@infra/database/logger";

export const errorHandlerMiddleware: ErrorRequestHandler = (
  err,
  req,
  res,
  _,
) => {
  logger.error({
    message: err,
    path: req.path,
    method: req.method,
  });
  if (err instanceof ZodError) {
    const errorMessages = err.errors.map((issue: ZodIssue) => ({
      message: `${issue.path.join(".")} is ${issue.message}`,
    }));
    res.status(400).json({
      error: "Dados inválidos",
      details: errorMessages,
    });
    return;
  }

  if (err instanceof Error) {
    if (err.name && (err as any).statusCode) {
      res.status((err as any).statusCode).json({
        type: err.name,
        message: err.message,
      });
      return;
    }
  }

  if (err instanceof QueryFailedError) {
    if ((err as any).code === "23505") {
      res.status(409).json({
        type: "DuplicateCpfError",
        message: "CPF já cadastrado",
      });
      return;
    }
  }

  res.status(500).json({
    type: "InternalServerError",
    message: "Unexpected server error",
  });
};
