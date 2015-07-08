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

package org.eclipse.vorto.codegen.api.tasks.eclipse;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.vorto.codegen.api.tasks.Generated;
import org.eclipse.vorto.codegen.api.tasks.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.IOutputter;

/**
 * This generator task copies generator plugin resources to the target generated
 * project.
 */
public class CopyResourceTask<Context> implements ICodeGeneratorTask<Context> {

	private URL resourceURL;
	private String targetFolder;
	private String suffix;

	public CopyResourceTask(URL resourceURL, String targetFolder, String suffix) {
		this.resourceURL = resourceURL;
		this.targetFolder = targetFolder;
		this.suffix = suffix;
	}

	public CopyResourceTask(URL resourceURL, String targetFolder) {
		this(resourceURL, targetFolder, null);
	}

	public void generate(Context metaData, IOutputter outputter) {
		try {

			// Had to use toFileURL instead of Resolve for URL to work from
			// plugin jar file
			URL resolvedFileURL = FileLocator.toFileURL(resourceURL);

			// Use URI constructor to properly escape file system chars
			java.net.URI resolvedURI = new java.net.URI(
					resolvedFileURL.getProtocol(), resolvedFileURL.getPath(),
					null);

			File root = new File(resolvedURI);

			copyFileRecursive(root, metaData, outputter, root.getPath()
					.indexOf(root.getName()));

		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void copyFileRecursive(File file, Context metaData,
			IOutputter outputter, int directoryOffset) {
		if (file.isDirectory()) {
			new FolderModule<>(targetFolder + "/"
					+ file.getPath().substring(directoryOffset)).generate(
					metaData, outputter);
			File[] filesOfDirectory = file.listFiles();
			for (File directoryFile : filesOfDirectory) {
				copyFileRecursive(directoryFile, metaData, outputter,
						directoryOffset);
			}
		} else {
			try {
				Generated generated = new Generated(
						getFileName(file),
						targetFolder
								+ "/"
								+ file.getPath().substring(directoryOffset,
										file.getPath().indexOf(file.getName())),
						FileUtils.readFileToString(file));
				outputter.output(generated);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getFileName(File file) {
		if (suffix != null) {
			return file.getName().substring(0, file.getName().indexOf(suffix));
		} else {
			return file.getName();
		}
	}
}
