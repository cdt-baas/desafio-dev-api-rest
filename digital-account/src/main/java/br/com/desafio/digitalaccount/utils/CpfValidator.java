package br.com.desafio.digitalaccount.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public void initialize(ValidCpf constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext constraintValidatorContext) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }

        //Removes non-numeric characters
        cpf = cpf.replaceAll("[^0-9]", "");

        //CPF contains 11 digits?
        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

//        try {
//            long cpfNum = Long.parseLong(cpf);
//        } catch (NumberFormatException e) {
//            return false;
//        }

        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = Character.getNumericValue(cpf.charAt(i));
        }

        //Calculates first verifier digit
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += digits[i] * (10 - i);
        }
        int firstVerifier = 11 - (sum % 11);
        if (firstVerifier > 9) {
            firstVerifier = 0;
        }

        //Verifies first digit
        if (digits[9] != firstVerifier) {
            return false;
        }

        //Calculates second verifier digit
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += digits[i] * (11 - i);
        }
        int secondVerifier = 11 - (sum % 11);
        if (secondVerifier > 9) {
            secondVerifier = 0;
        }

        //Verifies second digit
        return digits[10] == secondVerifier;
    }
}
