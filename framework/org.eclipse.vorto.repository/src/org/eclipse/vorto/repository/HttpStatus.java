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
package org.eclipse.vorto.repository;

public enum HttpStatus {
	SC_CONTINUE(100, "Continue"),
	SC_SWITCHING_PROTOCOLS(101,"Switching Protocols"),
    SC_PROCESSING(102, "Processing"),
    
    SC_OK(200, "OK"),
    SC_CREATED(201, "Created"),
    SC_ACCEPTED(202, "Accepted"),
    SC_NON_AUTHORITATIVE_INFORMATION(203, "Non Authoritative Information"),
    SC_NO_CONTENT(204, "No Content"),
    SC_RESET_CONTENT(205, "Reset Content"),
    SC_PARTIAL_CONTENT(206, "Partial Content"),
    
    SC_MULTI_STATUS(207, "Multi-Status"),
    
    SC_MULTIPLE_CHOICES(300, "Multiple Choices"),
    SC_MOVED_PERMANENTLY(301, "Moved Permanently"),
    SC_MOVED_TEMPORARILY(302, "Moved Temporarily"),
    SC_SEE_OTHER(303, "See Other") ,
    SC_NOT_MODIFIED(304, "Not Modified"),
    SC_USE_PROXY(305, "Use Proxy"),
    SC_TEMPORARY_REDIRECT(307, "Temporary Redirect"),

    SC_BAD_REQUEST(400, "Bad Request"),
    SC_UNAUTHORIZED(401, "Unauthorized"),
    SC_PAYMENT_REQUIRED(402, "Payment Required"),
    SC_FORBIDDEN(403, "Forbidden"),
    SC_NOT_FOUND(404, "Not Found"),
    SC_METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    SC_NOT_ACCEPTABLE(406, "Not Acceptable"), 
    SC_PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    SC_REQUEST_TIMEOUT(408, "Request Timeout"),
    SC_CONFLICT(409, "Conflict"),
    SC_GONE(410, "Gone"),
    SC_LENGTH_REQUIRED(411, "Length Required"),
    SC_PRECONDITION_FAILED(412, "Precondition Failed"),
    SC_REQUEST_TOO_LONG(413, "Request Too Long"),
    SC_REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),
    SC_UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    SC_REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),
    SC_EXPECTATION_FAILED(417, "Expectation Failed"),
    SC_INSUFFICIENT_SPACE_ON_RESOURCE(419, "Insufficient Space On Resource"),
    SC_METHOD_FAILURE(420, "Method Failure"),
    SC_UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    SC_LOCKED(423, "Locked"),
    SC_FAILED_DEPENDENCY(424, "Failed Dependency"),

    SC_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SC_NOT_IMPLEMENTED(501, "Not Implemented"),
    SC_BAD_GATEWAY(502, "Bad Gateway"),
    SC_SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    SC_GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    SC_HTTP_VERSION_NOT_SUPPORTED(505, "Http Version Not Supported"),
    SC_INSUFFICIENT_STORAGE(507, "Insufficient Storage");

    private int code;
	private String message;
	
	private HttpStatus(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public static HttpStatus statusFor(int code) {
		for(HttpStatus status : HttpStatus.values()) {
			if (status.getCode() == code) {
				return status;
			}
		}
		
		return null;
	}
}
