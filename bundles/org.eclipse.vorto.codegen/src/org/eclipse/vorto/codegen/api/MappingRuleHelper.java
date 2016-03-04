/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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

import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class MappingRuleHelper {

	private MappingRule mappingRule;
	
	public MappingRuleHelper(MappingRule mappingRule) {
		this.mappingRule = mappingRule;
	}
	
	public Attribute getStereoTypeAttribute(final String stereoTypeName, String attributeName) {
		if (mappingRule.getTarget() instanceof StereoTypeTarget) {
			StereoTypeTarget stereoType = (StereoTypeTarget)mappingRule.getTarget();
			for (Attribute attribute : stereoType.getAttributes()) {
				if (attribute.getName().equalsIgnoreCase(attributeName)) {
					return attribute;
				}
			}
		}
		
		return null;
	}
}
