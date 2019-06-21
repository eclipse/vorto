/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.plugin.importer;

public class ValidationReport {

	private boolean valid;
	private String message;
	
	protected ValidationReport() {
		
	}
	
	public static ValidationReport valid(String message) {
		return new ValidationReport(true, message);
	}
	
	public static ValidationReport invalid(String message) {
		return new ValidationReport(false, message);
	}

	private ValidationReport(boolean valid, String message) {
		super();
		this.valid = valid;
		this.message = message;
	}

	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
