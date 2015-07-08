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
package org.eclipse.vorto.codegen.api.filewrite;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.vorto.codegen.api.ICodeGenerator;
import org.eclipse.vorto.codegen.api.tasks.Generated;

/**
 * Writes the {@link Generated} produced by the {@link ICodeGenerator} in a
 * custom way
 * 
 * Available strategies:
 * <ul>
 * <li>{@link FileWritingStrategyFactory#getGenFileStrategy()}</li>
 * <li>{@link FileWritingStrategyFactory#getOverwriteStrategy()}</li>
 * <li>
 * {@link FileWritingStrategyFactory#getPlaceHolderMergeReplacementStrategy()}
 * <li>{@link FileWritingStrategyFactory#getPlaceHolderMergeAppendingStrategy()}
 * </li>
 * <li>
 * {@link FileWritingStrategyFactory#getPlaceHolderMergeReplacementStrategy()}</li>
 * </ul>
 * 
 */
public interface IFileWritingStrategy {

	/**
	 * Decides over how to write the specified generated content.
	 * 
	 * @param context
	 *            context for file writing
	 * @param existingFile
	 *            generated file and content from a previous generation
	 * @throws CoreException
	 * @throws IOException
	 */
	void writeFile(FileWriteContext context, IFile existingFile)
			throws CoreException, IOException;
}
