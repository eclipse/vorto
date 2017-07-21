package org.eclipse.vorto.repository.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class FileAccessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8774823366831621407L;

	public FileAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
