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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.vorto.repository.api.AbstractModel;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;

public class FunctionblockModel extends AbstractModel {

	private List<ModelProperty> configurationProperties = new ArrayList<>();
	private List<ModelProperty> statusProperties = new ArrayList<>();
	private List<ModelProperty> faultProperties = new ArrayList<>();
	private List<ModelEvent> events = new ArrayList<>(); 
	
	private List<Operation> operations = new ArrayList<>();
	
	public FunctionblockModel(ModelId modelId, ModelType modelType) {
		super(modelId, modelType);
		
	}	

	public List<ModelProperty> getConfigurationProperties() {
		return configurationProperties;
	}



	public void setConfigurationProperties(List<ModelProperty> configurationProperties) {
		this.configurationProperties = configurationProperties;
	}



	public List<ModelProperty> getStatusProperties() {
		return statusProperties;
	}



	public void setStatusProperties(List<ModelProperty> statusProperties) {
		this.statusProperties = statusProperties;
	}



	public List<ModelProperty> getFaultProperties() {
		return faultProperties;
	}



	public void setFaultProperties(List<ModelProperty> faultProperties) {
		this.faultProperties = faultProperties;
	}



	public List<ModelEvent> getEvents() {
		return events;
	}



	public void setEvents(List<ModelEvent> events) {
		this.events = events;
	}



	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	@Override
	public String toString() {
		return "FunctionblockModelDto [configurationProperties=" + configurationProperties + ", statusProperties="
				+ statusProperties + ", faultProperties=" + faultProperties + ", events=" + events + ", operations="
				+ operations + "]";
	}

}
