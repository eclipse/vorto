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
package org.eclipse.vorto.http.model;

import java.util.ArrayList;
import java.util.List;

public class FunctionblockModelDto extends AbstractModelDto {

	private List<ModelPropertyDto> configurationProperties = new ArrayList<>();
	private List<ModelPropertyDto> statusProperties = new ArrayList<>();
	private List<ModelPropertyDto> faultProperties = new ArrayList<>();
	private List<ModelEventDto> events = new ArrayList<>(); 
	
	private List<OperationDto> operations = new ArrayList<>();
	
	public FunctionblockModelDto(ModelIdDto modelId, ModelTypeDto modelType) {
		super(modelId, modelType);
		
	}	

	public List<ModelPropertyDto> getConfigurationProperties() {
		return configurationProperties;
	}



	public void setConfigurationProperties(List<ModelPropertyDto> configurationProperties) {
		this.configurationProperties = configurationProperties;
	}



	public List<ModelPropertyDto> getStatusProperties() {
		return statusProperties;
	}



	public void setStatusProperties(List<ModelPropertyDto> statusProperties) {
		this.statusProperties = statusProperties;
	}



	public List<ModelPropertyDto> getFaultProperties() {
		return faultProperties;
	}



	public void setFaultProperties(List<ModelPropertyDto> faultProperties) {
		this.faultProperties = faultProperties;
	}



	public List<ModelEventDto> getEvents() {
		return events;
	}



	public void setEvents(List<ModelEventDto> events) {
		this.events = events;
	}



	public List<OperationDto> getOperations() {
		return operations;
	}

	public void setOperations(List<OperationDto> operations) {
		this.operations = operations;
	}

	@Override
	public String toString() {
		return "FunctionblockModelDto [configurationProperties=" + configurationProperties + ", statusProperties="
				+ statusProperties + ", faultProperties=" + faultProperties + ", events=" + events + ", operations="
				+ operations + "]";
	}
	
	

}
