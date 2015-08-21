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
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.Fault;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Status;
import org.eclipse.vorto.core.api.model.mapping.ConfigurationElement;
import org.eclipse.vorto.core.api.model.mapping.FaultElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement;
import org.eclipse.vorto.core.api.model.mapping.OperationElement;
import org.eclipse.vorto.core.api.model.mapping.StatusElement;

/**
 * @author sgp0247
 *
 */
public class FunctionBlockRuleFilterFactory {
	public static FunctionBlockRulesFilter getFunctionBlockRulesFilter(FunctionBlockElement functionBlockElement, EObject modelElement) {
		if (modelElement instanceof Property) {
			return getPropertyRulesFilter(functionBlockElement, (Property) modelElement);
		} else if (isOperationProperty(functionBlockElement, modelElement)) {
			return new OperationRulesFilter();
		}
		return null;
	}

	public static FunctionBlockRulesFilter getPropertyRulesFilter(FunctionBlockElement functionBlockElement, Property modelElement) {
		if (isConfigurationProperty(functionBlockElement, modelElement)) {
			return new ConfigurationRulesFilter();
		} else if (isStatusProperty(functionBlockElement, modelElement)) {
			return new StatusRulesFilter();
		} else if (isFaultProperty(functionBlockElement, modelElement)) {
			return new FaultRulesFilter();
		}
		return null;
	}

	private static boolean isOperationProperty(FunctionBlockElement functionBlockElement, EObject modelElement) {
		return (functionBlockElement instanceof OperationElement) && (modelElement instanceof Operation);
	}
	
	private static boolean isConfigurationProperty(FunctionBlockElement functionBlockElement, Property modelElement) {
		return (functionBlockElement instanceof ConfigurationElement ) && (modelElement.eContainer() instanceof Configuration);
	}

	private static boolean isStatusProperty(FunctionBlockElement functionBlockElement, Property modelElement) {
		return (functionBlockElement instanceof StatusElement ) && (modelElement.eContainer() instanceof Status);
	}

	private static boolean isFaultProperty(FunctionBlockElement functionBlockElement, Property modelElement) {
		return (functionBlockElement instanceof FaultElement ) && (modelElement.eContainer() instanceof Fault);
	}
}
