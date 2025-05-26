package com.wendersonp.account.util;

import lombok.Getter;

@Getter
public enum ExceptionMessageEnum {
    DOCUMENT_NUMBER_NOT_FOUND("document_number_not_found", "Numero de documento não encontrado"),
    DOCUMENT_NUMBER_ALREADY_EXISTS("document_number_already_exists", "Numero de documento já existe"),
    COULD_NOT_RETRIEVE_SECRET("could_not_retrieve_retrieve_secret", "Nao foi possivel recuperar o secret"),
    COULD_NOT_HASH_DOCUMENT_NUMBER("could_not_hash_document_number", "Nao foi possivel gerar o hash do documento"),
    HOLDER_INACTIVE("holder_inactive", "Portador do documento não está ativo"),
    HOLDER_NOT_FOUND("holder_not_found", "Portador do documento não foi encontrado"),
    WITHDRAW_DAILY_LIMIT("withdraw_daily_limit", "Limite diario de saque excedido"),
    BLOCKED_ACCOUNT("blocked_account", "Conta está bloqueada"),
    ACCOUNT_EXISTS("account_exists", "Conta ja cadastrada para o portador"),
    ACCOUNT_NOT_FOUND("account_not_found", "Conta não foi encontrada"),
    BALANCE_NOT_FOUND("balance_not_found", "Saldo nao foi encontrado"),
    INSUFFICIENT_BALANCE("insufficient_balance", "Saldo insuficiente"),
    INVALID_DATES("invalid_dates", "As datas inseridas para a consulta são inválidas"),
    FUTURE_DATE("future_date", "Data futura nao permitida"),
    HOLDER_SERVICE_UNAVAILABLE("holder_service_unavailable", "Servico de portadores indisponivel");

    private final String code;
    private final String message;

    ExceptionMessageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String messageFromCode(String code) {
        for (ExceptionMessageEnum exceptionMessageEnum : ExceptionMessageEnum.values()) {
            if (exceptionMessageEnum.getCode().equals(code)) {
                return exceptionMessageEnum.getMessage();
            }
        }
        return null;
    }
}
