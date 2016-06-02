/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.api;

import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public interface IVortoCodeGenerator {
	
	static final String GENERATOR_ID = "org.eclipse.vorto.codegen.org_eclipse_vorto_codegen_Generators";
	
	/**
	 * Converts the given information models into platform specific code
	 * @param model information model containing functionblocks and datatype metadata of a specific device
	 * @param context generation invocation context, e.g. mapping rules or invocation parameters
	 * @return
	 */
	IGenerationResult generate(InformationModel model, InvocationContext context);
	
	/**
	 * 
	 * @return
	 */
	String getServiceKey();
}
