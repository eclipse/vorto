/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.repository.model;

/**
 * Class to hold model data at the time of extraction.
 * @author Nagavijay Sivakumar - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class ZipData {
	
	private ModelId modelId;
	private String fileName;
	private ModelType modelType;
	
	public ZipData(String fileName, ModelType modelType) {
		this.fileName = fileName;
		this.modelType = modelType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public ModelType getModelType() {
		return modelType;
	}
	public void setModelType(ModelType modelType) {
		this.modelType = modelType;
	}
	public ModelId getModelId() {
		return modelId;
	}
	public void setModelId(ModelId modelId) {
		this.modelId = modelId;
	}
}
