package com.wendersonp.holder.util;

import lombok.Getter;

@Getter
public enum ExceptionMessageEnum {
    DOCUMENT_NUMBER_NOT_FOUND("document_number_not_found", "Numero de documento não encontrado"),
    DOCUMENT_NUMBER_ALREADY_EXISTS("document_number_already_exists", "Numero de documento já existe"),
    COULD_NOT_RETRIEVE_SECRET("could_not_retrieve_retrieve_secret", "Nao foi possivel recuperar o secret"),
    COULD_NOT_HASH_DOCUMENT_NUMBER("could_not_hash_document_number", "Nao foi possivel gerar o hash do documento"),
    HOLDER_NOT_FOUND("holder_not_found", "Portador do documento nao foi encontrado");

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
