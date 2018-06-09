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
package org.eclipse.vorto.service.mapping.mapper;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.vorto.service.mapping.AbstractDataMapper;
import org.eclipse.vorto.service.mapping.MappingContext;
import org.eclipse.vorto.service.mapping.normalized.FunctionblockData;
import org.eclipse.vorto.service.mapping.normalized.InfomodelData;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;

public class SomePlatformMapper extends AbstractDataMapper<PlatformData> {
	
	public SomePlatformMapper(IMappingSpecification spec) {
		super(spec);
	}

	@Override
	protected PlatformData doMap(InfomodelData normalized, MappingContext mappingContext) {
		PlatformData data = new PlatformData();
		
		State state = new State();
		Map<String,Object> status = new HashMap<>();
		
		for (FunctionblockData fbData : normalized.getFunctionblockData()) {
			status.put(fbData.getId(), fbData.getStatus());
		}
		state.setReported(status);
		data.setState(state);
		
		return data;
	}
	

}
