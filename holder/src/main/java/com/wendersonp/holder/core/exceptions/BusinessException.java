package com.wendersonp.holder.core.exceptions;

import com.wendersonp.holder.util.ExceptionMessageEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    private final String code;

    public BusinessException(ExceptionMessageEnum exceptionMessageEnum) {
        super(exceptionMessageEnum.getMessage());
        this.code = exceptionMessageEnum.getCode();
    }
}
