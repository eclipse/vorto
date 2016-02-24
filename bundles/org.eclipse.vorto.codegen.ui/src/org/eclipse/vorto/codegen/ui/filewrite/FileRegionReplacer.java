/*******************************************************************************
 * Copyright (c) 2014, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.ui.filewrite;

/**
 * Replaces delta content for a specified region
 * 
 */
public class FileRegionReplacer implements IFileRegion {

	@Override
	public String merge(String delta, String fileContent, IRegionMarker location) {
		if (fileContent.contains(delta) || fileContent.length() == 0) {
			return fileContent; // do not merge if delta content already exists
		}

		int beginIndex = fileContent.indexOf(location.getRegionBegin());
		int endIndex = fileContent.indexOf(location.getRegionEnd())
				+ location.getRegionEnd().length();

		final String textToBeReplaced = fileContent.substring(beginIndex,
				endIndex);

		StringBuilder replacedText = new StringBuilder(
				location.getRegionBegin());
		replacedText.append("\r\n");
		replacedText.append(delta);
		replacedText.append("\r\n");
		replacedText.append(location.getRegionEnd());

		return fileContent
				.replaceAll(textToBeReplaced, replacedText.toString());
	}

}
