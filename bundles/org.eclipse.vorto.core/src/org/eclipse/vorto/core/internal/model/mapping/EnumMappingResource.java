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
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteral;
import org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.EnumPropertySource;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.model.IModelElement;
import org.eclipse.vorto.core.model.MappingAttribute;
import org.eclipse.vorto.core.model.ModelId;

public class EnumMappingResource extends AbstractMappingResource {

	public EnumMappingResource(MappingModel mappingModel) {
		super(mappingModel);
	}


	@Override
	protected void addRuleIfContainsModelObject(EObject modelObject, List<MappingRule> mappingRules,
			MappingRule rule, Source source) {
		if(source instanceof EnumPropertySource){
			EnumLiteral enumLiteral = ((EnumPropertySource)source).getProperty();
			Enum enumType = (Enum)((EnumPropertySource)source).getModel();
			if (matchesEnumLiteral(enumType, enumLiteral, modelObject)) {
				mappingRules.add(rule);
			}
		}
	}

	private boolean matchesEnumLiteral(Enum enumType, EnumLiteral enumLiteral, EObject modelElement) {
		if (!(modelElement instanceof EnumLiteral)) {
			return false;
		}

		EnumLiteral elementEnumLiteral = ((EnumLiteral) modelElement);
		Enum elementEnum = (Enum) elementEnumLiteral.eContainer();
		return StringUtils.equals(elementEnumLiteral.getName(), enumLiteral.getName())
				&& StringUtils.equals(elementEnum.getNamespace(), enumType.getNamespace())
				&& StringUtils.equals(elementEnum.getVersion(), enumType.getVersion());
	}

	@Override
	protected void addRuleIfContainsAttribute(MappingAttribute mappingAttribute, List<MappingRule> mappingRules,
			MappingRule rule, Source source) {
		
		if(source instanceof EnumAttributeSource){
			ModelAttribute attribute = ((EnumAttributeSource)source).getAttribute();
			if (StringUtils.equals(attribute.toString(), mappingAttribute.name())) {
				mappingRules.add(rule);
			}
		}
	}
}
