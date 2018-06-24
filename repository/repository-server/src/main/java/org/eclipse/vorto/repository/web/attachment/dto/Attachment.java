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
package org.eclipse.vorto.repository.web.attachment.dto;

import org.eclipse.vorto.repository.api.ModelId;

public class Attachment {

	private ModelId modelId;
	private String filename;
	private String downloadLink;

	public static Attachment newInstance(ModelId modelId, String filename) {
		return new Attachment(modelId, filename, getLink(modelId, filename));
	}
	
	public static String getLink(ModelId modelId, String filename) {
		StringBuffer link = new StringBuffer("/rest/model");

		link.append("/");
		link.append(modelId.getNamespace());
		link.append("/");
		link.append(modelId.getName());
		link.append("/");
		link.append(modelId.getVersion());
		link.append("/attachment/");
		link.append(filename);

		return link.toString();
	}
	
	private Attachment(ModelId modelId, String filename, String downloadLink) {
		this.modelId = modelId;
		this.filename = filename;
		this.downloadLink = downloadLink;
	}

	public ModelId getModelId() {
		return modelId;
	}

	public void setModelId(ModelId modelId) {
		this.modelId = modelId;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

}
