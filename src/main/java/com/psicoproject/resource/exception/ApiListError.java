package com.psicoproject.resource.exception;

import java.util.Date;
import java.util.List;

import lombok.Getter;

@Getter
public class ApiListError extends ApiError {
	private static final long serialVersionUID = 1975260318483632553L;
	
	private List<String> errors;

	public ApiListError(int code, String msg, Date date, List<String> errors) {
		super(code, msg, date);
		this.errors = errors;
	}
	
	
	
}
