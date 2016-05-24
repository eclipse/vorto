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
package org.eclipse.vorto.core.ui.model;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;

/**
 * Allow users to search element from MappingModel without the need to parse or navigate it
 */
public interface IMapping extends IModelElement{
	/**
	 * Retrieve all rules 
	 * @return mapping rules
	 */
	List<MappingRule> getAllRules();
	
	/**
	 * gets the mapping rules for the specified Model Object
	 * 
	 * @param modelObject
	 *            modelObject whose meta data ought to be looked up
	 * @return mapping rules
	 */
	List<MappingRule> getRulesByModelObject(EObject modelObject);

	/**
	 * gets the mapping rules for the specified Model Attribute
	 * 
	 * @param modelObject
	 *            modelObject whose meta data ought to be looked up
	 * @return mapping rules
	 */
	List<MappingRule> getRulesByModelAttribute(ModelAttribute modelAttribute);
	
	/**
	 * gets all mapping rules for the specified stereotype name. A stereotype is
	 * platform specific and specified in the DSL as e.g. "channel-type"
	 * 
	 * @param stereoType
	 * @return list of mapping rule objects
	 */
	List<MappingRule> getRulesByStereoType(String stereoTypeName);

}
