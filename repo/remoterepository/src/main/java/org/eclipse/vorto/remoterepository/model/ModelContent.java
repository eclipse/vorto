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
package org.eclipse.vorto.remoterepository.model;

import java.util.Objects;

/**
 * Describes a model as a byte array. Used mainly in transferring actual files
 * instead of metadata. 
 * 
 * @author 
 */
public class ModelContent {

	ModelType modelType;
	
	private byte[] content;

	private ModelId modelId;
	
	public ModelContent(ModelType modelType, byte[] content) {
		Objects.requireNonNull(modelType, "modelType must not be null for contructing ModelContent");
		this.modelType = modelType;
		this.content = content;
	}
	
	public ModelContent(ModelId modelId, ModelType modelType, byte[] content) {
		Objects.requireNonNull(modelType, "modelType must not be null for contructing ModelContent");
		this.modelId = modelId;
		this.modelType = modelType;
		this.content = content;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
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
