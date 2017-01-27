/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.repository.api.upload;

/**
 * Generic server response type for client request.
 * @author Nagavijay Sivakumar - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class ServerResponse {

	private String message = null;
	private Boolean isSuccess = null;
	private Object obj = null;

	/*
	 * Dummy constructor needed by ResponseEntity for JSON parsing of http response 
	 */
	public ServerResponse() {
		
	}
		
	public ServerResponse(String message, Boolean isSuccess, Object obj) {
		this.message = message;
		this.isSuccess = isSuccess;
		this.obj = obj;
	}

	/**
	 * Status message returned from server about request.
	 **/
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Boolean flag to denote server status about request. true if the request
	 * is successful or false otherwise.
	 **/
	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	/**
	 * Generic object which may optionally used for providing additional details
	 * about request. Typically used for UI and returned as Map(key, value)
	 * format.
	 **/
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ServerResponse {\n");

		sb.append("  message: ").append(message).append("\n");
		sb.append("  isSuccess: ").append(isSuccess).append("\n");
		sb.append("  obj: ").append(obj).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
}