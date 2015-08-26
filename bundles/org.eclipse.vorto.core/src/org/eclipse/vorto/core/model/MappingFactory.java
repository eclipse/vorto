/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.core.model;

import org.eclipse.vorto.core.api.model.mapping.EntityMapping;
import org.eclipse.vorto.core.api.model.mapping.EnumMapping;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMapping;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.internal.model.mapping.EntityMappingResource;
import org.eclipse.vorto.core.internal.model.mapping.EnumMappingResource;
import org.eclipse.vorto.core.internal.model.mapping.FunctionBlockMappingResource;
import org.eclipse.vorto.core.internal.model.mapping.InfoModelMappingResource;

public class MappingFactory {

	public static IMapping createMapping(MappingModel mappingModel) {
		if (mappingModel instanceof InfoModelMapping) {
			return new InfoModelMappingResource(mappingModel);
		} else if (mappingModel instanceof FunctionBlockMapping) {
			return new FunctionBlockMappingResource(mappingModel);
		} else if (mappingModel instanceof EntityMapping) {
			return new EntityMappingResource(mappingModel);
		} else if (mappingModel instanceof EnumMapping) {
			return new EnumMappingResource(mappingModel);
		}
		throw new UnsupportedOperationException(
				"Mapping rules not registered for type : " + mappingModel.getClass().getName());
	}
}
