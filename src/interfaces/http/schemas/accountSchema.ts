import { z } from "zod";

export const createAccountSchema = z.object({
  body: z.object({
    agency: z
      .string({
        required_error: "Agency é obrigatória.",
      })
      .nonempty("Agency não pode ser vazia."),
    number: z
      .string({
        required_error: "Number é obrigatória.",
      })
      .nonempty("Number não pode ser vazio."),
    balance: z
      .number({
        required_error: "Balance é obrigatório.",
      })
      .nonnegative("Balance não pode ser negativo."),
    cpf: z
      .string({
        required_error: "CPF é obrigatório.",
      })
      .refine((doc) => {
        const replacedDoc = doc.replace(/\D/g, "");
        return replacedDoc.length === 11;
      }, "CPF deve conter 11 caracteres.")
      .refine((doc) => {
        const replacedDoc = doc.replace(/\D/g, "");
        return !!Number(replacedDoc);
      }, "CPF deve conter apenas números."),
  }),
});

export const depositSchema = z.object({
  body: z.object({
    amount: z
      .number({
        required_error: "Amount é obrigatório.",
      })
      .positive("Amount deve ser um numero positivo"),
  }),
});

export const withdrawSchema = z.object({
  body: z.object({
    amount: z
      .number({
        required_error: "Amount é obrigatório.",
      })
      .positive("Amount deve ser um numero positivo"),
  }),
});

export const getTransactionsSchema = z.object({
  query: z.object({
    page: z.coerce.number().min(1).default(1),
    limit: z.coerce.number().min(1).default(10),
    startDate: z.string().date(),
    endDate: z.string().date(),
  }),
});
