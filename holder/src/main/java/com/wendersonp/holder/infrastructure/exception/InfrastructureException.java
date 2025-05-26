package com.wendersonp.holder.infrastructure.exception;

import com.wendersonp.holder.util.ExceptionMessageEnum;
import lombok.Getter;

@Getter
public class InfrastructureException extends RuntimeException{

    private final String code;

    public InfrastructureException(ExceptionMessageEnum exceptionMessageEnum, Throwable cause) {
        super(exceptionMessageEnum.getMessage(), cause);
        this.code = exceptionMessageEnum.getCode();
    }

}
