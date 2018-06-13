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

public class StagingResult {
	private String importer;
	private boolean valid;
	private String errorMessage;
	private String stagingId;
	private Object stagingDetails;

	public static StagingResult success(String importer, String stagingId, Object stagingDetails) {
		StagingResult result = new StagingResult();
		
		result.setImporter(importer);
		result.setStagingId(stagingId);
		result.setValid(true);
		result.setStagingDetails(stagingDetails);
		
		return result;
	}
	
	public static StagingResult fail(String importer, String errorMessage, Object stagingDetails) {
		StagingResult result = new StagingResult();
		
		result.setImporter(importer);
		result.setValid(false);
		result.setErrorMessage(errorMessage);
		result.setStagingDetails(stagingDetails);
		
		return result;
	}
	
	private StagingResult() {
		
	}
	
	public String getImporter() {
		return importer;
	}

	public void setImporter(String importer) {
		this.importer = importer;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getStagingId() {
		return stagingId;
	}

	public void setStagingId(String stagingId) {
		this.stagingId = stagingId;
	}

	public Object getStagingDetails() {
		return stagingDetails;
	}

	public void setStagingDetails(Object stagingDetails) {
		this.stagingDetails = stagingDetails;
	}

}
