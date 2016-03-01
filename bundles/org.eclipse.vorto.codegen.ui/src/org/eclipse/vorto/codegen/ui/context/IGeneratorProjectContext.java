/*******************************************************************************
 *  Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.ui.context;

/**
 * Context object for the generation of a code generator project
 * 
 */
public interface IGeneratorProjectContext extends IProjectContext {

	/**
	 * 
	 * @return the name of the generator
	 */
	String getGeneratorName();

	/**
	 * 
	 * @return the name of the target package
	 */
	String getPackageName();

	/**
	 * 
	 * @return package folders
	 */
	String getPackageFolders();

	/**
	 * 
	 * @return flag if the generator should support server-side deployment.
	 */
	boolean isMicroServiceSupport();

}
