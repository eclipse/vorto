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
package org.eclipse.vorto.service.mapping;

import org.eclipse.vorto.service.mapping.json.JsonData;

/**
 * Data Mapper that maps specific device payload to IoT platform specific data or vica versa.
 *
 * @param <MappedData>
 */
public interface IDataMapper<MappedData extends JsonData> {
	
	/**
	 * Performs the actual platform specific mapping for the given input.
	 * @param input source input data that is supposed to get mapped.
	 * @param context providing more meta - data for the mapper
	 * @return mapped data that complies to the target platform.
	 */
	MappedData map(DataInput input, MappingContext context);
	
	static DataMapperBuilder newBuilder() {
		return new DataMapperBuilder();
	}
}
