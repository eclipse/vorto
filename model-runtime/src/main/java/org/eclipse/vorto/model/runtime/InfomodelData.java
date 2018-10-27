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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelProperty;

public class InfomodelData implements IValidatable {

	private Infomodel meta = null;
	
	private Map<String, FunctionblockData> functionblocks = new HashMap<>();
	
	public InfomodelData(Infomodel meta) {
		this.meta = meta;
	}
	
	public void withFunctionblock(FunctionblockData data) {
		this.functionblocks.put(data.getId(),data); 
	}
	
	public Map<String,FunctionblockData> getProperties() {
		return functionblocks;
	}
	
	public FunctionblockData get(String fbProperty) {
		return functionblocks.get(fbProperty);
	}

	@Override
	public String toString() {
		return "InfomodelData [functionblocks=" + functionblocks + "]";
	}

	@Override
	public ValidationReport validate() {
		ValidationReport report = new ValidationReport();
		for (ModelProperty fbProperty : meta.getFunctionblocks()) {
			if (fbProperty.isMandatory() && !functionblocks.containsKey(fbProperty.getName())) {
				report.addItem(fbProperty, "Mandatory property is missing!");
			} else {
				FunctionblockData fbData = functionblocks.get(fbProperty.getName());
				if (fbData != null) {
					ValidationReport fbReport = fbData.validate();
					report.addReport(fbReport);
				}
				
			}
		}
		return report;
	}

	public Map<String,Object> serialize() {
		Map<String,Object> result = new HashMap<String, Object>();
		for (String fbProperty : functionblocks.keySet()) {
			result.put(fbProperty, functionblocks.get(fbProperty).serialize());
		}
		return result;
	}
	
}
