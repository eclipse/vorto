package org.eclipse.vorto.mapping.engine.normalized;

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
