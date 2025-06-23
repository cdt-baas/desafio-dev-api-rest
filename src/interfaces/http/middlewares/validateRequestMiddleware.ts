import { Request, Response, NextFunction } from "express";
import { AnyZodObject } from "zod";

export const validateRequestMiddleware =
  (schema: AnyZodObject) =>
  (req: Request, res: Response, next: NextFunction) => {
    try {
      schema.parse({
        params: req.params,
        body: req.body,
        query: req.query,
      });
      return next();
    } catch (error) {
      next(error);
    }
  };
