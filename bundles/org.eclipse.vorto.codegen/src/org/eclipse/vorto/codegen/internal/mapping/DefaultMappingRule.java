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

import org.eclipse.vorto.codegen.api.mapping.IMappingRule;
import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.Rule;
import org.eclipse.vorto.core.api.model.mapping.StereoType;

public class DefaultMappingRule implements IMappingRule {

	private Rule rule;

	public DefaultMappingRule(Rule rule) {
		this.rule = rule;
	}

	@Override
	public Rule getRule() {
		return this.rule;
	}

	@Override
	public StereoType getStereoType(String stereoTypeName) {
		for (StereoType stereoType : this.rule.getTargetElement()
				.getStereoTypes()) {
			if (stereoType.getName().equals(stereoTypeName)) {
				return stereoType;
			}
		}
		return null;
	}

	@Override
	public Attribute getStereoTypeAttribute(String stereoTypeName,
			String attributeName) {
		StereoType stereoType = this.getStereoType(stereoTypeName);

		if (stereoType == null) {
			return NullAttribute.INSTANCE;
		}

		for (Attribute attribute : stereoType.getAttributes()) {
			if (attribute.getName().equals(attributeName)) {
				return attribute;
			}
		}
		return NullAttribute.INSTANCE;
	}
}
