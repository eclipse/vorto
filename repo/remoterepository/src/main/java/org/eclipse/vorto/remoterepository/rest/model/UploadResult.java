/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.remoterepository.rest.model;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UploadResult {
	String status;
	String message;
	String exception;

	public UploadResult(Status status, String message) {
		this.status = status.name();
		this.message = message;
	}

	public UploadResult(Status status, String message, Exception exception) {
		this.status = status.name();
		this.message = message;
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
	
		this.exception = sw.toString();
	}
	
	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public String getException() {
		return exception;
	}

	public enum Status {
		SUCCESS, FAILURE
	};
}

