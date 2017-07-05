/*******************************************************************************
 * Copyright (c) 2017 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.server.devtool.http.request;

public class LinkReferenceResourceRequest {

	private String targetResourceId;
	private String referenceResourceId;

	public String getTargetResourceId() {
		return targetResourceId;
	}
	public void setTargetResourceId(String targetResourceId) {
		this.targetResourceId = targetResourceId;
	}
	public String getReferenceResourceId() {
		return referenceResourceId;
	}
	public void setReferenceResourceId(String referenceResourceId) {
		this.referenceResourceId = referenceResourceId;
	}
		
}
