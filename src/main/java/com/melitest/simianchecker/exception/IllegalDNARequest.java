package com.melitest.simianchecker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class IllegalDNARequest extends RuntimeException {

	private static final long serialVersionUID = 189L;	

	public IllegalDNARequest(String message) {
		super(message);
	}

	public IllegalDNARequest(String message, Throwable cause) {
		super(message, cause);
	}
	
}
