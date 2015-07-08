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

package org.eclipse.vorto.editor;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.vorto.codegen.api.filewrite.FileWriteContext;
import org.eclipse.vorto.codegen.api.filewrite.FileWritingStrategyFactory;
import org.eclipse.vorto.codegen.api.filewrite.IFileWritingStrategy;
import org.eclipse.vorto.codegen.api.tasks.Generated;
import org.eclipse.vorto.codegen.api.tasks.IOutputter;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2;

public class EclipseFileSystemAccessOutputter extends
		EclipseResourceFileSystemAccess2 implements IOutputter {

	private static final String FILE_SEPARATOR = "/";
	private IFileWritingStrategy strategy;

	public IProject getIProject() {
		return this.getProject();
	}

	@Override
	public void output(Generated generated) {
		String path = "";
		if (Generated.ROOT_FOLDER_PATH != generated.getFolderPath()) {
			path = generated.getFolderPath() + FILE_SEPARATOR;
		}

		if (StringUtils.isBlank(generated.getFileName())) {
			throw new IllegalArgumentException("No file name available");
		} else {
			String fileFullPath = path + generated.getFileName();

			try {
				this.getFileWritingStrategy().writeFile(
						new FileWriteContext(generated.getContent()),
						this.getFile(fileFullPath, DEFAULT_OUTPUT));
			} catch (CoreException | IOException e) {
				throw new RuntimeException("Specified File cannot be written",
						e);
			}
		}

	}

	@Override
	public void setFileWritingStrategy(IFileWritingStrategy fileWritingHandler) {
		this.strategy = fileWritingHandler;
	}

	public IFileWritingStrategy getFileWritingStrategy() {
		if (this.strategy == null) {
			this.strategy = FileWritingStrategyFactory.getInstance()
					.getOverwriteStrategy();
		}
		return strategy;
	}

}
