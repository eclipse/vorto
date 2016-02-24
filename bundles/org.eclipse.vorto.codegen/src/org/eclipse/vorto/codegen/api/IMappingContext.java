/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.codegen.api;

import java.util.List;

import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;

/**
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public interface IMappingContext {

	List<MappingRule> getAllRules();
	
	List<MappingRule> getMappingRulesByOperation(Operation operation);
	
	List<MappingRule> getMappingRulesByProperty(Property property);
	
	/**
	 * gets all mapping rules for the specified stereotype name. A stereotype is
	 * platform specific and specified in the DSL as e.g. "channel-type"
	 * 
	 * @param stereoType
	 * @return list of mapping rule objects
	 */
	List<MappingRule> getRulesByStereoType(String stereoTypeName);
}
