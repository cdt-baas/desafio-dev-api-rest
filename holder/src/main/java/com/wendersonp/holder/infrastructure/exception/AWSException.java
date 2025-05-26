package com.wendersonp.holder.infrastructure.exception;

import com.wendersonp.holder.util.ExceptionMessageEnum;
import lombok.Getter;

@Getter
public class AWSException extends RuntimeException {

    private final String code;

    public AWSException(ExceptionMessageEnum exceptionMessageEnum, Exception cause) {
        super(exceptionMessageEnum.getMessage(), cause);
        this.code = exceptionMessageEnum.getCode();
    }
}
