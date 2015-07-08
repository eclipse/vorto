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
package org.eclipse.vorto.codegen.api;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * This Code Generator generates platform specific code for the information
 * model which can be executed in a target environment.
 * 
 */
public interface ICodeGenerator<Context> {

	static final String GENERATOR_ID = "org.eclipse.vorto.codegen.org_eclipse_vorto_codegen_Generators";

	/**
	 * Generates environment specific code for a given model context
	 * 
	 * @param model
	 *            model instance which contains context information to generated
	 *            platform specific code
	 * @param monitor
	 *            callback to monitor steps during the generation process
	 */
	void generate(Context ctx, IProgressMonitor monitor);

	/**
	 * Descriptive name of the generator
	 * 
	 */
	String getName();
}
