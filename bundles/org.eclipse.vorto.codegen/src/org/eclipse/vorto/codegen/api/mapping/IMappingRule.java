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
package org.eclipse.vorto.codegen.api.mapping;

import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.StereoType;

/**
 * A mapping rule as defined in the target platform mapping file.
 * 
 */
public interface IMappingRule {

	/**
	 * gets the mapping rule
	 * 
	 * @return
	 */
	InfoModelMappingRule getRule();

	/**
	 * finds a stereotype by its name
	 * 
	 * @param stereoTypeName
	 * @return stereotype
	 */
	StereoType getStereoType(String stereoTypeName);

	/**
	 * Get attribute based on given stereoTypeName and attributeName
	 * 
	 * @param stereoTypeName
	 * @param attributeName
	 * @return attribute instance, or NullAttribute of no attribute is found
	 *         based on input
	 */
	Attribute getStereoTypeAttribute(String stereoTypeName, String attributeName);
}
