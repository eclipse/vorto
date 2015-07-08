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


/**
 * Appends delta content to already pre-generated code of the region
 * 
 */
public class FileRegionAppender implements IFileRegion {

	@Override
	public String merge(String delta, String fileContent, IRegionMarker location) {
		if (fileContent.contains(delta) || fileContent.length() == 0) {
			return fileContent; // do not merge if delta content already exists
		}

		String[] splitContent = fileContent.split(location.getRegionBegin());

		StringBuilder contentBuilder = new StringBuilder(splitContent[0]);
		contentBuilder.append(location.getRegionBegin());
		contentBuilder.append("\r\t");
		contentBuilder.append(delta);
		contentBuilder.append(splitContent[1]);

		return contentBuilder.toString();
	}

}
