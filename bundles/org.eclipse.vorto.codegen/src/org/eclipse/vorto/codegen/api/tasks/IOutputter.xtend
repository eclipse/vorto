/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
 
 package org.eclipse.vorto.codegen.api.tasks;

import org.eclipse.vorto.codegen.api.filewrite.IFileWritingStrategy
import org.eclipse.vorto.codegen.api.ICodeGenerator

/**
 * Outputter for generated content produced by the {@link ICodeGenerator}
 */
interface IOutputter {
	
	/**
	 * Outputs the specified generated artifact
	 */
	def void output(org.eclipse.vorto.codegen.api.tasks.Generated generated);
	
	/**
	 * Sets the global strategy for file writing for generated artifact
	 */
	def void setFileWritingStrategy(IFileWritingStrategy fileWritingHandler);
	
}