package org.eclipse.vorto.repository.web.core.exceptions;

public class UploadTooLargeException extends RuntimeException {

	private static final long serialVersionUID = 6269154530443747274L;

	public UploadTooLargeException(String target, long limit) {
		super("The uploaded file for " + target + " is too large. It should be below " + limit);
	}

}
