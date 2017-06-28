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
package org.eclipse.vorto.server.devtool.models;

public class LinkReferenceResponse {

	private String content;
	private String targetResourceId;
	private String referenceResourceId;

	public LinkReferenceResponse(String content, String targetResourceId, String referenceResourceId) {
		this.content = content;
		this.targetResourceId = targetResourceId;
		this.referenceResourceId = referenceResourceId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

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
