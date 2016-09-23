/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.codegen.ui.filewrite;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;


public class WriteGenOverWriteStrategy extends AbstractFileWriteStrategy {

	private static final String FILENAME_SUFFIX = "_gen";

	@Override
	public void writeFile(FileWriteContext generated, IFile existingFile)
			throws CoreException, IOException {

		String fileName = existingFile.getName();
		IProject project = existingFile.getProject();

		if (!existingFile.exists()) {
			write(generated.getContent(), existingFile);

			/*
			 * Clearing the file history of the newly created file is because
			 * file history is not deleted when the file is being deleted. So if
			 * the file is not found whether deleted by the system or user , it
			 * will clear the history after creating the file to avoid false
			 * detection of modification.
			 */
			existingFile.clearHistory(null);
		} else {
			if (isModified(existingFile)) {
				IFile replacefile = getFile(fileName + FILENAME_SUFFIX,
						existingFile.getLocation(), project);

				if (replacefile.exists())
					replacefile.delete(true, null);

				write(generated.getContent(), replacefile);
			} else {
				existingFile.delete(true, null);
				write(generated.getContent(), existingFile);
			}

		}

	}

	private boolean isModified(IFile existingFile) {
		IFileState[] history = null;
		try {
			history = existingFile.getHistory(null);
		} catch (CoreException e) {
			return true;
		}
		return history.length != 0;
	}

	private IFile getFile(String fileName, IPath folderPath, IProject project) {
		return project.getFolder(folderPath).getFile(fileName);
	}

	private void write(String contentStr, IFile existingFile)
			throws CoreException, IOException {
		InputStream content = IOUtils.toInputStream(contentStr);
		existingFile.create(content, false, null);
		content.close();
	}

}
