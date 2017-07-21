package org.eclipse.vorto.repository.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ImageNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1755698991667967052L;

	public ImageNotFoundException(String message) {
		super(message);
	}

}
