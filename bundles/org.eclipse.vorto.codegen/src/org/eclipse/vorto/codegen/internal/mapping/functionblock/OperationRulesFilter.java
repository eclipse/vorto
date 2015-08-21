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
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement;
import org.eclipse.vorto.core.api.model.mapping.OperationElement;

/**
 * @author sgp0247
 *
 */
public class OperationRulesFilter extends AbstractFunctionBlockChildRulesFilter {
	public boolean isRuleContainsContainsModelElement(FunctionBlockMappingRule rule,
			FunctionBlockSourceElement sourceElement, EObject modelElement) {
		Operation operationElement = ((OperationElement) sourceElement.getFunctionBlockElement()).getOperation();
		return this.matchesFunctionBlockOperation(sourceElement.getFunctionblock(), operationElement, modelElement);
	}

	protected boolean matchesFunctionBlockOperation(FunctionblockModel functionBlockModel, Operation operationElement,
			EObject modelElement) {

		Operation modelElementOperation = ((Operation) modelElement);
		FunctionblockModel elementFunctionBlock = (FunctionblockModel) modelElementOperation.eContainer().eContainer();
		return StringUtils.equals(modelElementOperation.getName(), operationElement.getName())
				&& StringUtils.equals(elementFunctionBlock.getNamespace(), functionBlockModel.getNamespace())
				&& StringUtils.equals(elementFunctionBlock.getVersion(), functionBlockModel.getVersion());
	}
}
