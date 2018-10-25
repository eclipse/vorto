/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.mapping.engine.model;

import java.util.HashMap;
import java.util.Map;

public class FunctionblockData {

	private String id;
	
	private Map<String, Object> status = new HashMap<String, Object>();
	private Map<String,Object> configuration = new HashMap<String, Object>();
	private Map<String,Object> fault = new HashMap<String, Object>();
		
	public FunctionblockData(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getStatus() {
		return status;
	}

	public void setStatus(Map<String, Object> status) {
		this.status = status;
	}

	public Map<String, Object> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<String, Object> configuration) {
		this.configuration = configuration;
	}

	public Map<String, Object> getFault() {
		return fault;
	}

	public void setFault(Map<String, Object> fault) {
		this.fault = fault;
	}

    public void withStatusProperty(String name, Object mapped) {
		this.status.put(name, mapped);
	}
	
    public void withConfigurationProperty(String name, Object mapped) {
		this.configuration.put(name, mapped);
	}
	
    public void withFaultProperty(String name, Object mapped) {
		this.fault.put(name, mapped);
	}

	@Override
	public String toString() {
		return "FunctionblockData [id=" + id + ", status=" + status + ", configuration=" + configuration + ", fault="
				+ fault + "]";
	}

}
