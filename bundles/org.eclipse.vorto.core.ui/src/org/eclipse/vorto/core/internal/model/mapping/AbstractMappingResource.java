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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.model.AbstractModelElement;
import org.eclipse.vorto.core.model.IMapping;
import org.eclipse.vorto.core.service.IModelElementResolver;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;

public abstract class AbstractMappingResource extends AbstractModelElement implements IMapping {
	private List<IMapping> referenceMappings = new ArrayList<IMapping>();
	protected MappingModel mappingModel = null;

	public AbstractMappingResource(MappingModel mappingModel, List<IMapping> referenceMappings) {
		this.mappingModel = mappingModel;
		this.referenceMappings = referenceMappings;
	}

	@Override
	public List<MappingRule> getAllRules() {
		List<MappingRule> mappingRules = new ArrayList<>();
		mappingRules.addAll(this.getAllLocalRules());
		mappingRules.addAll(this.getAllReferenceRules());
		return mappingRules;
	}

	private Collection<? extends MappingRule> getAllReferenceRules() {
		List<MappingRule> mappingRules = new ArrayList<>();
		for (IMapping mapping : this.getMappings()) {
			mappingRules.addAll(mapping.getAllRules());
		}
		return mappingRules;
	}

	private Collection<? extends MappingRule> getAllLocalRules() {
		if (isEmptyMappingModel()) {
			return Collections.emptyList();
		} else {
			return this.mappingModel.getRules();
		}
	}

	@Override
	public List<MappingRule> getRulesByStereoType(String stereoTypeName) {
		List<MappingRule> mappingRules = new ArrayList<>();
		mappingRules.addAll(this.getLocalRulesByStereoType(stereoTypeName));
		mappingRules.addAll(this.getReferenceRulesByStereoType(stereoTypeName));
		return mappingRules;
	}

	private List<MappingRule> getLocalRulesByStereoType(String stereoTypeName) {
		if (isEmptyMappingModel())
			return Collections.emptyList();

		List<MappingRule> mappingRules = new ArrayList<>();

		for (MappingRule rule : mappingModel.getRules()) {
			addRuleIfContainsStereoTypeName(stereoTypeName, mappingRules, rule);
		}
		return mappingRules;
	}

	/**
	 * @return
	 */
	private boolean isEmptyMappingModel() {
		return (mappingModel == null) || (mappingModel.getRules()==null) || (mappingModel.getRules().isEmpty());
	}

	private List<MappingRule> getReferenceRulesByStereoType(String stereoTypeName) {
		List<MappingRule> mappingRules = new ArrayList<>();
		for (IMapping mapping : this.getMappings()) {
			mappingRules.addAll(mapping.getRulesByStereoType(stereoTypeName));
		}
		return mappingRules;
	}

	private void addRuleIfContainsStereoTypeName(String stereoTypeName, List<MappingRule> mappingRules,
			MappingRule rule) {
		if (rule.getTarget() instanceof StereoTypeTarget) {
			String targetStereoTypeName = ((StereoTypeTarget) rule.getTarget()).getName();
			if (StringUtils.equals(stereoTypeName, targetStereoTypeName)) {
				mappingRules.add(rule);
			}
		}
	}

	@Override
	public List<MappingRule> getRulesByModelObject(EObject modelObjecct) {

		List<MappingRule> mappingRules = new ArrayList<>();

		mappingRules.addAll(this.getLocalRulesByModelObject(modelObjecct));
		mappingRules.addAll(this.getReferenceRulesByModelObject(modelObjecct));
		return mappingRules;
	}

	private List<MappingRule> getLocalRulesByModelObject(EObject modelObjecct) {

		if (isEmptyMappingModel() || (modelObjecct==null)){
			return Collections.emptyList();
		}
		
		List<MappingRule> mappingRules = new ArrayList<>();
		for (MappingRule rule : this.mappingModel.getRules()) {
			for (Source source : rule.getSources()) {
				addRuleIfContainsModelObject(modelObjecct, mappingRules, rule, source);
			}
		}

		return mappingRules;
	}

	private List<MappingRule> getReferenceRulesByModelObject(EObject modelObjecct) {
		List<MappingRule> mappingRules = new ArrayList<>();
		for (IMapping mapping : this.getMappings()) {
			mappingRules.addAll(mapping.getRulesByModelObject(modelObjecct));
		}

		return mappingRules;
	}

	abstract protected void addRuleIfContainsModelObject(EObject modelObject, List<MappingRule> mappingRules,
			MappingRule rule, Source source);

	@Override
	public List<MappingRule> getRulesByModelAttribute(ModelAttribute modelAttribute) {
		List<MappingRule> mappingRules = new ArrayList<>();
		mappingRules.addAll(this.getLocalRulesByModelAttribute(modelAttribute));
		mappingRules.addAll(this.getReferenceRulesByModelAttribute(modelAttribute));
		return mappingRules;
	}

	private List<MappingRule> getLocalRulesByModelAttribute(ModelAttribute modelAttribute) {
		if (isEmptyMappingModel())
			return Collections.emptyList();

		List<MappingRule> mappingRules = new ArrayList<>();
		for (MappingRule rule : this.mappingModel.getRules()) {
			for (Source source : rule.getSources()) {
				addRuleIfContainsAttribute(modelAttribute, mappingRules, rule, source);
			}
		}
		return mappingRules;
	}

	private List<MappingRule> getReferenceRulesByModelAttribute(ModelAttribute modelAttribute) {
		List<MappingRule> mappingRules = new ArrayList<>();
		for (IMapping mapping : this.getMappings()) {
			mappingRules.addAll(mapping.getRulesByModelAttribute(modelAttribute));
		}
		return mappingRules;
	}

	abstract protected void addRuleIfContainsAttribute(ModelAttribute modelAttribute,
			List<MappingRule> mappingRules, MappingRule rule, Source source);

	protected List<IMapping> getMappings() {
		return this.referenceMappings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.vorto.core.model.IModelElement#getModel()
	 */
	@Override
	public Model getModel() {
		return this.mappingModel;
	}

	protected boolean matchesModel(Model model1, Model model2) {
		return StringUtils.equals(model1.getName(), model2.getName())
				&& StringUtils.equals(model1.getNamespace(),
						model2.getNamespace())
				&& StringUtils.equals(model1.getVersion(), model2.getVersion());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.vorto.core.model.IModelElement#getModelFile()
	 */
	@Override
	public IFile getModelFile() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.vorto.core.model.AbstractModelElement#getImageURLAsString()
	 */
	@Override
	protected String getImageURLAsString() {
		return "platform:/plugin/org.eclipse.vorto.core.ui/icons/mapping.png";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.vorto.core.model.AbstractModelElement#
	 * getPossibleReferenceType()
	 */
	@Override
	protected ModelType getPossibleReferenceType() {
		return ModelType.Mapping;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.vorto.core.model.AbstractModelElement#getResolvers()
	 */

	protected IModelElementResolver[] getResolvers() {
		return new IModelElementResolver[] { ModelProjectServiceFactory.getDefault().getWorkspaceProjectResolver() };
	}

}
