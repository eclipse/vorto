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
<<<<<<< 8fce91bd96d10ca25d52d3920c7f9527391e78e2:server/repo/repo-core/src/main/java/org/eclipse/vorto/repository/model/ModelResource.java
 */
package org.eclipse.vorto.http.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelResource {
	
	protected ModelId id;
	
	protected ModelType modelType;
	protected String displayName;
	protected String description;
	protected String author;
	protected Date creationDate;
	protected boolean hasImage = false;
	
	protected List<ModelId> references = new ArrayList<>();
	
	protected List<ModelId> referencedBy = new ArrayList<ModelId>();
	
	protected List<String> supportedTargetPlatforms = new ArrayList<String>();
	
	public ModelResource(ModelId modelId,ModelType modelType) {
		this.id = modelId;
		this.modelType = modelType;
	}

	public ModelId getId() {
		return id;
	}

	public void setId(ModelId id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ModelId> getReferences() {
		return references;
	}

	public void setReferences(List<ModelId> references) {
		this.references = references;
	}
	
	public List<ModelId> getReferencedBy() {
		return referencedBy;
	}

	public void setReferencedBy(List<ModelId> referencedBy) {
		this.referencedBy = referencedBy;
	}

	public ModelType getModelType() {
		return modelType;
	}

	public void setModelType(ModelType modelType) {
		this.modelType = modelType;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public List<String> getSupportedTargetPlatforms() {
		return supportedTargetPlatforms;
	}

	public void setSupportedTargetPlatforms(List<String> supportedTargetPlatforms) {
		this.supportedTargetPlatforms = supportedTargetPlatforms;
	}
	
	public boolean isHasImage() {
		return hasImage;
	}

	public void setHasImage(boolean hasImage) {
		this.hasImage = hasImage;
	}	
	
	
	@Override
	public String toString() {
		return "ModelResource [id=" + id + ", modelType=" + modelType + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((modelType == null) ? 0 : modelType.hashCode());
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
		ModelResource other = (ModelResource) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (modelType != other.modelType)
			return false;
		return true;
	}

	public void addTargetPlatform(String targetPlatform) {
		if (targetPlatform != null && !targetPlatform.equals("")) {
			this.supportedTargetPlatforms.add(targetPlatform);
		}
	}	
	
}
