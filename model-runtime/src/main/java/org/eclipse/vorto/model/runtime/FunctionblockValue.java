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
package org.eclipse.vorto.model.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.PrimitiveType;

public class FunctionblockValue implements IValidatable {
	
	private FunctionblockModel meta;
	
	private List<PropertyValue> status = new ArrayList<PropertyValue>();
	private List<PropertyValue> configuration = new ArrayList<PropertyValue>();
	private List<FBEventValue> events = new ArrayList<FBEventValue>();
	
	public FunctionblockValue(FunctionblockModel meta) {
		this.meta = meta;
	}


	public List<PropertyValue> getStatus() {
		return Collections.unmodifiableList(this.status);
	}
	
	public Optional<PropertyValue> getStatusProperty(String propertyName) {
		return this.status.stream().filter(p -> p.getMeta().getName().equals(propertyName)).findAny();
	}

	public List<PropertyValue> getConfiguration() {
		return Collections.unmodifiableList(this.status);
	}
	
	public Optional<PropertyValue> getConfigurationProperty(String propertyName) {
		return this.configuration.stream().filter(p -> p.getMeta().getName().equals(propertyName)).findAny();
	}

    public void withStatusProperty(String name, Object value) {
    	Optional<ModelProperty> mp = meta.getStatusProperty(name);
    	if (!mp.isPresent()) {
    		throw new IllegalArgumentException("Status property with given name is not defined in Function Block");
    	}
		this.status.add(new PropertyValue(mp.get(), value));
	}
	
    public void withConfigurationProperty(String name, Object value) {
    	Optional<ModelProperty> mp = meta.getConfigurationProperty(name);
    	if (!mp.isPresent()) {
    		throw new IllegalArgumentException("Configuration property with given name is not defined in Function Block");
    	}
		this.configuration.add(new PropertyValue(mp.get(), value));
	}
    
    public void withEvent(FBEventValue event) {
    	this.events.add(event);
    }

	@Override
	public String toString() {
		return "FunctionblockData [status=" + status + ", configuration=" + configuration + "]";
	}

	@Override
	public ValidationReport validate() {
		ValidationReport report = new ValidationReport();
		
		for (ModelProperty statusProperty : meta.getStatusProperties()) {
			checkProperty(getStatus(), statusProperty, meta.getId().getName().toLowerCase() + "/status",report);
		}

		for (ModelProperty configProperty : meta.getConfigurationProperties()) {
			checkProperty(getConfiguration(), configProperty,
					meta.getId().getName().toLowerCase() + "/configuration",report);
		}
		return report;
	}
	
	private void checkProperty(List<PropertyValue> properties, ModelProperty property, String path, ValidationReport report) {
		Optional<PropertyValue> mpd = properties.stream().filter(p -> p.getMeta().equals(property)).findAny();
		if (property.isMandatory()
				&& !mpd.isPresent()) {
			report.addItem(property, "Mandatory field " + path + "/" + property.getName() + " is missing");
		} else {
			if (mpd.isPresent() && property.getType() instanceof PrimitiveType) {
				checkPrimitiveTypeValue(path, mpd.get().getValue(), property,report);
			}
		}
	}

	private static void checkPrimitiveTypeValue(String path, Object propertyValue, ModelProperty property,ValidationReport report) {
		PrimitiveType type = (PrimitiveType) property.getType();
		if (type == PrimitiveType.STRING && !(propertyValue instanceof String)) {
			report.addItem(property,"Field " + path + "/" + property.getName() + " must be of type 'String'");
		} else if (type == PrimitiveType.BOOLEAN && !(propertyValue instanceof Boolean)) {
			report.addItem(property,"Field " + path + "/" + property.getName() + " must be of type 'Boolean'");
		} else if (type == PrimitiveType.DOUBLE && !(propertyValue instanceof Double)) {
			report.addItem(property,"Field " + path + "/" + property.getName() + " must be of type 'Double'");
		} else if (type == PrimitiveType.FLOAT && !isFloat(propertyValue)) {
			report.addItem(property,"Field " + path + "/" + property.getName() + " must be of type 'Float'");
		} else if (type == PrimitiveType.INT && !isInteger(propertyValue)) {
			report.addItem(property,"Field " + path + "/" + property.getName() + " must be of type 'Integer'");
		} else if (type == PrimitiveType.LONG && !isLong(propertyValue)) {
			report.addItem(property,"Field " + path + "/" + property.getName() + " must be of type 'Long'");
		} else if (type == PrimitiveType.BASE64_BINARY && !(propertyValue instanceof String)) {
			report.addItem(property,
					"Field " + path + "/" + property.getName() + " must be a Base64-encoded 'String'");
		}
	}

	private static boolean isInteger(Object value) {
		try {
			Integer.parseInt(value.toString());
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	private static boolean isFloat(Object value) {
		try {
			Float.parseFloat(value.toString());
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	private static boolean isLong(Object value) {
		try {
			Long.parseLong(value.toString());
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	
	public Map<String, Object> serialize() {
		Map<String, Object> result = new HashMap<String, Object>();
		
		Map<String,Object> statusProperties = new HashMap<String, Object>();
		for (PropertyValue statusProperty : status) {
			statusProperties.put(statusProperty.getMeta().getName(), statusProperty.getValue());
		}
		result.put("status", statusProperties);
		
		Map<String,Object> configuration = new HashMap<String, Object>();
		for (PropertyValue configProperty : this.configuration) {
			configuration.put(configProperty.getMeta().getName(), configProperty.getValue());
		}
		if (!configuration.isEmpty()) {
			result.put("configuration", configuration);
		}
		
		return result;
	}
}
