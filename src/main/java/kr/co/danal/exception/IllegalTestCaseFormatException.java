package kr.co.danal.exception;

import kr.co.danal.element.Element;

public class IllegalTestCaseFormatException extends ElementExecuteException {

	private static final long serialVersionUID = 274777745366632297L;

	public IllegalTestCaseFormatException(Exception e, Class<? extends Element> clazz) {
		super(e, clazz);
	}

	public IllegalTestCaseFormatException(String message, Class<? extends Element> clazz) {
		super(message, clazz);
	}

	public IllegalTestCaseFormatException(String message, Throwable cause, Class<? extends Element> clazz) {
		super(message, cause, clazz);
	}

	public IllegalTestCaseFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalTestCaseFormatException(String message) {
		super(message);
	}

	
}
