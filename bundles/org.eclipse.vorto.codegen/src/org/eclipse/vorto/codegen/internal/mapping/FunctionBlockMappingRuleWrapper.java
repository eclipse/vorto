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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.codegen.api.mapping.IMappingRule;
import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockTargetElement;
import org.eclipse.vorto.core.api.model.mapping.StereoType;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeElement;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeReference;

/**
 * @author sgp0247
 *
 */
public class FunctionBlockMappingRuleWrapper implements IMappingRule {
	private FunctionBlockMappingRule rule;

	public FunctionBlockMappingRuleWrapper(FunctionBlockMappingRule rule) {
		this.rule = rule;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.vorto.codegen.api.mapping.IMappingRule#getRule()
	 */
	@Override
	public EObject getRule() {
		return this.rule;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.vorto.codegen.api.mapping.IMappingRule#getStereoType(java.lang.String)
	 */
	@Override
	public StereoType getStereoType(String stereoTypeName) {
		FunctionBlockTargetElement targetElement = this.rule.getTarget();
		if(!(targetElement instanceof StereoTypeReference)){
			return null;
		}
		
		StereoTypeElement stereoTypeElement = ((StereoTypeReference)targetElement).getTargetElement();
		return StereoTypeHelper.getStereoType(stereoTypeElement.getStereoTypes(), stereoTypeName);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.vorto.codegen.api.mapping.IMappingRule#getStereoTypeAttribute(java.lang.String, java.lang.String)
	 */
	@Override
	public Attribute getStereoTypeAttribute(String stereoTypeName, String attributeName) {
		StereoType stereoType = this.getStereoType(stereoTypeName);

		return StereoTypeHelper.getAttribute(stereoType, attributeName);
	}


}
