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
package org.eclipse.vorto.mapping.engine;

import java.util.Optional;

import org.eclipse.vorto.mapping.engine.model.FunctionblockProperty;
import org.eclipse.vorto.mapping.engine.model.InfomodelData;

/**
 * Data Mapper that maps specific device payload to Vorto compliant data and vica versa.
 *
 * @param <Result>
 */
public interface IDataMapper {
	
	
	@Deprecated
	/**
	 * Use {@link#mapSource} instead
	 * @param input
	 * @param context
	 * @return
	 */
	InfomodelData map(Object input, MappingContext context);
	
	/**
	 * Maps the given source object to Vorto compliant data structure
	 * @param input source input data that is supposed to get mapped.
	 * @return mapped payload that complies to Vorto Information Model
	 */
	InfomodelData mapSource(Object input);

	/**
	 * Maps the given new property 
	 * @param newValue
	 * @param oldValue
	 * @param context
	 * @return
	 */
	Object mapTarget(FunctionblockProperty newValue, Optional<FunctionblockProperty> oldValue);
	
	static DataMapperBuilder newBuilder() {
		return new DataMapperBuilder();
	}
}
