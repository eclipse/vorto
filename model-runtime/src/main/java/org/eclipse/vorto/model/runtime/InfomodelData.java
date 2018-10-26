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

public class InfomodelData {

	private Map<String, FunctionblockData> functionblocks = new HashMap<>();
	
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
	
}
