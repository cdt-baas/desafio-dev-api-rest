package com.baas.contas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ContaBloqueadaException extends RuntimeException {

	private static final long serialVersionUID = 5652759519048384634L;

	public ContaBloqueadaException() {
		super();
	}
}
