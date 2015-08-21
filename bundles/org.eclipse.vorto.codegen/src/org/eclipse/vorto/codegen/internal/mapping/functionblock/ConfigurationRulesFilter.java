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
package org.eclipse.vorto.codegen.internal.mapping.functionblock;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.mapping.ConfigurationElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement;

/**
 * @author sgp0247
 *
 */
public class ConfigurationRulesFilter extends AbstractFunctionBlockChildRulesFilter {
	public boolean isRuleContainsContainsModelElement(FunctionBlockMappingRule rule,
			FunctionBlockSourceElement sourceElement, EObject modelElement) {
		Property functionBlockProperty = ((ConfigurationElement)sourceElement.getFunctionBlockElement()).getTypeRef()
				.getProperty();
		return this.matchesFunctionBlockProperty(sourceElement.getFunctionblock(), functionBlockProperty, modelElement);
	}
}
