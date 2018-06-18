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
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class UploadModelResult {
	private String handleId = null;
	private ValidationReport report;

	public UploadModelResult(String handleId,ValidationReport report) {
		super();
		this.handleId = handleId;
		this.report = report;
	}
	
	protected UploadModelResult() {
		
	}

	public String getHandleId() {
		return handleId;
	}

	public ValidationReport getReport() {
		return report;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handleId == null) ? 0 : handleId.hashCode());
		result = prime * result + ((report == null) ? 0 : report.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UploadModelResult other = (UploadModelResult) obj;
		if (handleId == null) {
			if (other.handleId != null)
				return false;
		} else if (!handleId.equals(other.handleId))
			return false;
		if (report == null) {
			if (other.report != null)
				return false;
		} else if (!report.equals(other.report))
			return false;
		return true;
	}
	
	
	

	
	
}
