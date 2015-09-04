package org.eclipse.vorto.repository.service;

public abstract class ModelRepositoryException extends RuntimeException {

	public ModelRepositoryException(String msg) {
		super(msg);
	}
	
	public ModelRepositoryException(String msg,Throwable cause) {
		super(msg,cause);
	}
}
