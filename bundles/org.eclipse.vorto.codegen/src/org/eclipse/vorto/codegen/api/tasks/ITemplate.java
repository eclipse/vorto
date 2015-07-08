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
 * A {@link ICodeGeneratorTask} use generation templates which contain the
 * context specific outcome logic
 * 
 */
public interface ITemplate<Context> {

	/**
	 * gets the generation template for the specified context
	 * 
	 * @param context
	 * @return generated content for the specified context
	 */
	String getContent(Context context);

}
