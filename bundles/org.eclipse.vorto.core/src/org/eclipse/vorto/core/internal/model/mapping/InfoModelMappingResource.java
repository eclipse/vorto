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
package org.eclipse.vorto.core.internal.model.mapping;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.model.IModelElement;
import org.eclipse.vorto.core.model.MappingAttribute;
import org.eclipse.vorto.core.model.ModelId;

public class InfoModelMappingResource extends AbstractMappingResource {

	public InfoModelMappingResource(MappingModel mappingModel) {
		super(mappingModel);
	}

	
	@Override
	protected void addRuleIfContainsModelObject(EObject modelObjecct, List<MappingRule> mappingRules,
			MappingRule rule, Source source) {
		if(source instanceof InfoModelPropertySource){
			FunctionblockProperty functionblockProperty = ((InfoModelPropertySource)source).getProperty();
			FunctionblockModel functionblockModel = functionblockProperty.getType();
			if (matchesFunctionBlockModel(functionblockModel, modelObjecct)) {
				mappingRules.add(rule);
			}
		}
	}

	private boolean matchesFunctionBlockModel(FunctionblockModel functionblockModel, EObject modelElement) {
		if (!(modelElement instanceof FunctionblockProperty)) {
			return false;
		}

		FunctionblockModel elementModel = ((FunctionblockProperty) modelElement).getType();
		return StringUtils.equals(elementModel.getName(), functionblockModel.getName())
				&& StringUtils.equals(elementModel.getNamespace(), functionblockModel.getNamespace())
				&& StringUtils.equals(elementModel.getVersion(), functionblockModel.getVersion());

	}

	@Override
	protected void addRuleIfContainsAttribute(MappingAttribute mappingAttribute, List<MappingRule> mappingRules,
			MappingRule rule, Source source) {
		
		if(source instanceof InfoModelAttributeSource){
			InfoModelAttribute attribute = ((InfoModelAttributeSource)source).getAttribute();
			if (StringUtils.equals(attribute.toString(), mappingAttribute.name())) {
				mappingRules.add(rule);
			}
		}
	}
}
