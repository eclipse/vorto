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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement;

public abstract class AbstractFunctionBlockChildRulesFilter implements FunctionBlockRulesFilter{

	public abstract boolean isRuleContainsContainsModelElement(FunctionBlockMappingRule rule,
			FunctionBlockSourceElement sourceElement, EObject modelElement);

	protected boolean matchesFunctionBlockProperty(FunctionblockModel functionBlockModel, Property functionBlockProperty,
			EObject modelElement) {
		if (!(modelElement instanceof Property)) {
			return false;
		}

		Property modelElementProperty = ((Property) modelElement);
		FunctionblockModel elementFunctionBlock = (FunctionblockModel) modelElementProperty.eContainer().eContainer()
				.eContainer();
		return StringUtils.equals(modelElementProperty.getName(), functionBlockProperty.getName())
				&& StringUtils.equals(elementFunctionBlock.getNamespace(), functionBlockModel.getNamespace())
				&& StringUtils.equals(elementFunctionBlock.getVersion(), functionBlockModel.getVersion());
	}
}
