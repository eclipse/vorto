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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.model.IMapping;

public class InfoModelMappingResource extends AbstractMappingResource {

	public InfoModelMappingResource(MappingModel mappingModel, List<IMapping> referenceMappings) {
		super(mappingModel, referenceMappings);
	}

	@Override
	protected void addRuleIfContainsModelObject(EObject modelElement, List<MappingRule> mappingRules, MappingRule rule,
			Source source) {
		if (source instanceof InfoModelPropertySource) {
			if(modelElement instanceof InformationModel){
				InformationModel sourceModel = ((InfoModelPropertySource) source).getModel();
				if(this.matchesInformationModel(sourceModel, modelElement)){
					mappingRules.add(rule);
				}
			}else if(modelElement instanceof FunctionblockProperty){
				FunctionblockProperty functionblockProperty = ((InfoModelPropertySource) source).getProperty();

				if (functionblockProperty == null) {
					return;
				}

				FunctionblockModel functionblockModel = functionblockProperty.getType();
				if (matchesFunctionBlockModel(functionblockModel, modelElement)) {
					mappingRules.add(rule);
				}
			}
		}
	}

	private boolean matchesInformationModel(InformationModel informationModel, EObject modelElement) {
		if (!(modelElement instanceof InformationModel)) {
			return false;
		}

		InformationModel elementModel = (InformationModel) modelElement;
		return this.matchesModel(informationModel, elementModel);

	}
	
	private boolean matchesFunctionBlockModel(FunctionblockModel functionblockModel, EObject modelElement) {
		if (!(modelElement instanceof FunctionblockProperty)) {
			return false;
		}

		FunctionblockModel elementModel = ((FunctionblockProperty) modelElement).getType();
		return this.matchesModel(functionblockModel, elementModel);

	}

	@Override
	protected void addRuleIfContainsAttribute(ModelAttribute modelAttribute, List<MappingRule> mappingRules,
			MappingRule rule, Source source) {

		if (source instanceof InfoModelAttributeSource) {
			ModelAttribute attribute = ((InfoModelAttributeSource) source).getAttribute();
			if (modelAttribute ==attribute) {
				mappingRules.add(rule);
			}
		}
	}
}
