package com.baeldung.camunda.domain.exceptions;

import lombok.Getter;

@Getter
public class BusinessException extends Exception {

	private final String errorCode;

	public BusinessException(String errorCode) {
		super("Ошибка BPMN");
		this.errorCode = errorCode;
	}

}
