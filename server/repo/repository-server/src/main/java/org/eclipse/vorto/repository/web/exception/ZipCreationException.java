package org.eclipse.vorto.repository.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ZipCreationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5192945054105747833L;

	public ZipCreationException(String message, Throwable cause) {
		super(message, cause);
	}

}
