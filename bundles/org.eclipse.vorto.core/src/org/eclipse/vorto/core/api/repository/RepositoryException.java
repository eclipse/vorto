package org.eclipse.vorto.core.api.repository;

// Marker exception for all repository exceptions
public class RepositoryException extends RuntimeException {

	private static final long serialVersionUID = 3846464162093109545L;

	public RepositoryException(String message) {
		super(message);
	}

	public RepositoryException(Throwable cause) {
		super(cause);
	}
	
	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
	}
}
