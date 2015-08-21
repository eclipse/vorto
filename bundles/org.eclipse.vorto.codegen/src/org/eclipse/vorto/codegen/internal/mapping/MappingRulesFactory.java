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
package org.eclipse.vorto.codegen.internal.mapping;

import org.eclipse.vorto.codegen.api.mapping.IMappingRules;
import org.eclipse.vorto.core.api.model.mapping.EntityMapping;
import org.eclipse.vorto.core.api.model.mapping.EnumMapping;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMapping;
import org.eclipse.vorto.core.api.model.mapping.Mapping;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;

public class MappingRulesFactory {

	public static IMappingRules createMappingRules(MappingModel mappingModel) {
		Mapping mapping = mappingModel.getMapping();
		if (mapping instanceof InfoModelMapping) {
			return new InfoModelMappingRules(mappingModel);
		} else if (mapping instanceof FunctionBlockMapping) {
			return new FunctionBlockMappingRules(mappingModel);
		} else if (mapping instanceof EntityMapping) {
			return new EntityMappingRules(mappingModel);
		} else if (mapping instanceof EnumMapping) {
			return new EnumMappingRules(mappingModel);
		}
		throw new UnsupportedOperationException(
				"Mapping rules not registered for type : " + mapping.getClass().getName());
	}
}
