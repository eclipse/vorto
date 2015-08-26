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
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.model.IMapping;
import org.eclipse.vorto.core.model.IModelElement;
import org.eclipse.vorto.core.model.MappingAttribute;
import org.eclipse.vorto.core.model.ModelId;

/**
 * @author sgp0247
 *
 */
public abstract class AbstractMappingResource implements IMapping {
	protected MappingModel mappingModel;

	public AbstractMappingResource(MappingModel mappingModel) {
		this.mappingModel = mappingModel;
	}

	@Override
	public List<MappingRule> getRulesByStereoType(String stereoTypeName) {
		List<MappingRule> mappingRules = new ArrayList<>();
		for (MappingRule rule : mappingModel.getRules()) {
			addRuleIfContainsStereoTypeName(stereoTypeName, mappingRules, rule);
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
		for (MappingRule rule : this.mappingModel.getRules()) {
			for (Source source : rule.getSources()) {
				addRuleIfContainsModelObject(modelObjecct, mappingRules, rule, source);
			}
		}

		return mappingRules;
	}

	abstract protected void addRuleIfContainsModelObject(EObject modelObject, List<MappingRule> mappingRules,
			MappingRule rule, Source source);

	@Override
	public List<MappingRule> getRulesByModelAttribute(MappingAttribute mappingAttribute) {
		List<MappingRule> mappingRules = new ArrayList<>();
		for (MappingRule rule : this.mappingModel.getRules()) {
			for (Source source : rule.getSources()) {
				addRuleIfContainsAttribute(mappingAttribute, mappingRules, rule, source);
			}
		}
		return mappingRules;
	}

	abstract protected void addRuleIfContainsAttribute(MappingAttribute mappingAttribute, List<MappingRule> mappingRules,
			MappingRule rule, Source source);
	

	/* (non-Javadoc)
	 * @see org.eclipse.vorto.core.model.IModelElement#getId()
	 */
	@Override
	public ModelId getId() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.vorto.core.model.IModelElement#getModelFile()
	 */
	@Override
	public IFile getModelFile() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.vorto.core.model.IModelElement#getModel()
	 */
	@Override
	public Model getModel() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.vorto.core.model.IModelElement#getDescription()
	 */
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.vorto.core.model.IModelElement#getImage()
	 */
	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.vorto.core.model.IModelElement#getReferences()
	 */
	@Override
	public Set<IModelElement> getReferences() {
		// TODO Auto-generated method stub
		return null;
	}	
}
