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

package org.eclipse.vorto.codegen.api.tasks;

/**
 * Adds file specific meta information for the template generation.
 * 
 */
public interface IFileTemplate<Context> extends ITemplate<Context> {

	/**
	 * file name of the generated file for the specified context
	 * 
	 * @param context
	 * @return file name of the target file
	 */
	String getFileName(final Context context);

	/**
	 * file path of the generated file for the specified context
	 * 
	 * @param context
	 * @return file path of the target file
	 */
	String getPath(final Context context);

}
