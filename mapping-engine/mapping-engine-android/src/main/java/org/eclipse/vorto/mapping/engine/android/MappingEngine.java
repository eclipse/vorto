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
package org.eclipse.vorto.mapping.engine.android;

import java.io.InputStream;

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
		mapper = new DataMapperBuilder().registerScriptEvalProvider(new JavascriptEvalProvider()).withSpecification(specification).build();
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
	public Object mapTarget(PropertyValue newValue, PropertyValue oldValue, String infomodelProperty) {
		return mapper.mapTarget(newValue,oldValue,infomodelProperty);
	}
}
