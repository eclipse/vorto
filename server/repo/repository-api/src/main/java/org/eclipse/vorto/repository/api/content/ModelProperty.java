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
package org.eclipse.vorto.repository.api.content;

public class ModelProperty {

	private boolean isMandatory = true;
	private String name;
	
	private boolean isMultiple = false;
	private String description;
	
	private IReferenceType type = null;
			
	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public IReferenceType getType() {
		return type;
	}

	public void setType(IReferenceType type) {
		this.type = type;
	}

	public boolean isPrimitive() {
		return type instanceof PrimitiveType;
	}

	public boolean isMultiple() {
		return isMultiple;
	}

	public void setMultiple(boolean isMultiple) {
		this.isMultiple = isMultiple;
	}

	@Override
	public String toString() {
		return "ModelPropertyDto [isMandatory=" + isMandatory + ", name=" + name + ", isMultiple=" + isMultiple
				+ ", description=" + description + ", type=" + type + ", isPrimitive=" + isPrimitive() + "]";
	}
	
	
	
	
}
