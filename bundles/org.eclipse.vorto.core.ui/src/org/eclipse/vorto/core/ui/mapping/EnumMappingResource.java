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
package org.eclipse.vorto.core.ui.mapping;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteral;
import org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.EnumPropertySource;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.ui.model.IMapping;
import org.eclipse.vorto.core.ui.model.IModelProject;

public class EnumMappingResource extends AbstractMappingResource {

	public EnumMappingResource(IModelProject modelProject, MappingModel mappingModel, List<IMapping> referenceMappings) {
		super(modelProject, mappingModel, referenceMappings);
	}

	@Override
	protected void addRuleIfContainsModelObject(EObject modelObject, List<MappingRule> mappingRules, MappingRule rule,
			Source source) {
		
		if(modelObject instanceof Enum){
			addRuleIfMatchesEnum((Enum)modelObject, mappingRules, rule, source);
		}else if(modelObject instanceof EnumLiteral){					
			addRuleIfMatchesEnumLiteral(modelObject, mappingRules, rule, source);
		}
	}

	/**
	 * @param modelObject
	 * @param mappingRules
	 * @param rule
	 * @param source
	 */
	private void addRuleIfMatchesEnum(Enum modelObject, List<MappingRule> mappingRules, MappingRule rule,
			Source source) {
		if (source instanceof EnumPropertySource) {
			Enum enumType = (Enum) ((EnumPropertySource) source).getModel();
			if (matchesModel(enumType, modelObject)) {
				mappingRules.add(rule);
			}
		}
	}

	/**
	 * @param modelObject
	 * @param mappingRules
	 * @param rule
	 * @param source
	 */
	protected void addRuleIfMatchesEnumLiteral(EObject modelObject, List<MappingRule> mappingRules, MappingRule rule,
			Source source) {
		if (source instanceof EnumPropertySource) {
			EnumLiteral enumLiteral = ((EnumPropertySource) source).getProperty();
			Enum enumType = (Enum) ((EnumPropertySource) source).getModel();
			if (matchesEnumLiteral(enumType, enumLiteral, modelObject)) {
				mappingRules.add(rule);
			}
		}
	}

	private boolean matchesEnumLiteral(Enum enumType, EnumLiteral enumLiteral, EObject modelElement) {
		if ((enumLiteral == null) || !(modelElement instanceof EnumLiteral)) {
			return false;
		}

		EnumLiteral elementEnumLiteral = ((EnumLiteral) modelElement);
		Enum elementEnum = (Enum) elementEnumLiteral.eContainer();
		return StringUtils.equals(elementEnumLiteral.getName(), enumLiteral.getName())
				&& StringUtils.equals(elementEnum.getNamespace(), enumType.getNamespace())
				&& StringUtils.equals(elementEnum.getVersion(), enumType.getVersion());
	}

	@Override
	protected void addRuleIfContainsAttribute(ModelAttribute modelAttribute, List<MappingRule> mappingRules,
			MappingRule rule, Source source) {

		if (source instanceof EnumAttributeSource) {
			ModelAttribute attribute = ((EnumAttributeSource) source).getAttribute();
			if (modelAttribute ==attribute) {
				mappingRules.add(rule);
			}
		}
	}
}
