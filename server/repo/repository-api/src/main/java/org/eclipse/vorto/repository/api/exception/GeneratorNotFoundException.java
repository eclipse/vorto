package org.eclipse.vorto.repository.api.exception;

public class GeneratorNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GeneratorNotFoundException(String msg, Throwable t) {
		super(msg,t);
	}
	
	public GeneratorNotFoundException(String msg) {
		super(msg);
	}
}

