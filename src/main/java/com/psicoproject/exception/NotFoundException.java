package com.psicoproject.exception;

public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 3119186050415720855L;
	
	public NotFoundException(String msg) {
		super(msg);
	}

}
