/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.mapping.engine.android;

import java.io.InputStream;
import java.util.Optional;

import org.eclipse.vorto.mapping.engine.DataMapperBuilder;
import org.eclipse.vorto.mapping.engine.IDataMapper;
import org.eclipse.vorto.mapping.engine.converter.android.JavascriptEvalProvider;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.mapping.engine.model.spec.MappingSpecBuilder;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.model.runtime.PropertyValue;

public final class MappingEngine {

	private IDataMapper mapper;
	
	private MappingEngine(IMappingSpecification specification) {
		DataMapperBuilder builder =
			IDataMapper.newBuilder().registerScriptEvalProvider(new JavascriptEvalProvider())
				.withSpecification(specification);
		mapper = builder.build();
	}
	
	public static MappingEngine create(IMappingSpecification specification) {
		return new MappingEngine(specification);	
	}
	
	public static MappingEngine createFromInputStream(InputStream inputStream) {
		IMappingSpecification spec = new MappingSpecBuilder().fromInputStream(inputStream).build();
		return new MappingEngine(spec);
	}
	
	/**
	 * Maps the given device source object to Vorto compliant Information Model data.
	 * @param input source input data that is supposed to get mapped.
	 * @return mapped payload that complies to Vorto Information Model
	 */
	public InfomodelValue map(Object deviceData) {
		return mapper.mapSource(deviceData);
	}
	
	/**
	 * Maps the given Functionblock Property to device specific object.
	 * @param newValue the value to map
	 * @param oldValue the value that is currently set on the device
	 * @param infomodelProperty the name of the property defined in the information model
	 * @return the mapped device specific object
	 */
	public Object mapTarget(PropertyValue newValue, Optional<PropertyValue> oldValue,
		String infomodelProperty) {
		return mapper.mapTarget(newValue, oldValue, infomodelProperty);
	}
}
