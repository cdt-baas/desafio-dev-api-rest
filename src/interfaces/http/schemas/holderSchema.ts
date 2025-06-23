import { z } from "zod";

export const createHolderSchema = z.object({
  body: z.object({
    fullName: z
      .string({
        required_error: "Nome completo é obrigatório.",
      })
      .nonempty("Nome completo não pode ser vazio."),
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
