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
package org.eclipse.vorto.codegen.internal.filewrite;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.vorto.codegen.api.filewrite.FileWriteContext;

public class RegionMergeStrategy extends AbstractFileWriteStrategy {

	private IFileRegion fileRegion = null;

	public RegionMergeStrategy(IFileRegion fileRegion) {
		this.fileRegion = fileRegion;
	}

	@Override
	public void writeFile(FileWriteContext context, IFile existingFile)
			throws CoreException, IOException {

		if (!existingFile.exists()) {
			throw new IllegalStateException("No existing file to merge with.");
		}

		String existingContent = toString(existingFile.getContents());

		IRegionMarker location = RegionMarkerFactory.getRegionMarker(
				context.getGenerationKey(), existingContent);

		String mergedContent = fileRegion.merge(context.getContent(),
				existingContent, location);

		if (!mergedContent.equals(existingContent)) {
			existingFile.setContents(toInputStream(mergedContent), true, true,
					new NullProgressMonitor());
		}

	}

	private InputStream toInputStream(String s) {
		return IOUtils.toInputStream(s);
	}

	private String toString(InputStream s) throws IOException {
		return IOUtils.toString(s);
	}

}
