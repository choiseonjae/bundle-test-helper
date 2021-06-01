package kr.co.bundletesthelper.exception;

import kr.co.bundletesthelper.element.Element;

public class ElementExecuteException extends RuntimeException {

	private static final long serialVersionUID = 274777745366632297L;
	
	private Class<? extends Element> clazz = null;

	public ElementExecuteException(Exception e, Class<? extends Element> clazz) {
		super(String.format("%s in %s", e.getMessage(), e.getClass().getSimpleName()));
		this.clazz = clazz;
	}

	public ElementExecuteException(String message, Class<? extends Element> clazz) {
		super(message);
		this.clazz = clazz;
	}

	public ElementExecuteException(String message, Throwable cause, Class<? extends Element> clazz) {
		super(String.format("%s in %s", cause.getMessage(), message), cause);
		this.clazz = clazz;
	}
	
	public ElementExecuteException(String message, Throwable cause) {
		super(String.format("%s in %s", cause.getMessage(), message), cause);
	}
	
	public ElementExecuteException(String message) {
		super(message);
	}

	public Class<? extends Element> getOccuredClass(){
		return clazz;
	}

}
