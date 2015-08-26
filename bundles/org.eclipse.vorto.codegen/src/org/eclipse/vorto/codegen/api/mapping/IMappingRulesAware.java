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

import org.eclipse.vorto.core.api.model.mapping.InfoModelMapping;

/**
 * 
 * Code Generators which need to process mapping rules during generation, should
 * be implementing this interface
 * 
 */
public interface IMappingRulesAware {

	/**
	 * 
	 * @return the name of the mapping for the current code generator, e.g.
	 *         myplatform
	 */
	String getMappingRulesFileName();

	/**
	 * injects the mapping rules for the specific target platform
	 * 
	 * @param rules
	 */
	void setMappingRules(InfoModelMapping rules);
}
