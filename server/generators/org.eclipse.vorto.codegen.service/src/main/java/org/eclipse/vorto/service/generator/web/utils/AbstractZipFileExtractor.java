/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.service.generator.web.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public abstract class AbstractZipFileExtractor {

	protected byte[] zipFile;
	
	public AbstractZipFileExtractor(byte[] zipFile) {
		this.zipFile = zipFile;
	}
	
	protected static byte[] copyStream(ZipInputStream in, ZipEntry entry) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int size;
		byte[] buffer = new byte[2048];

		BufferedOutputStream bos = new BufferedOutputStream(out);

		while ((size = in.read(buffer, 0, buffer.length)) != -1) {
			bos.write(buffer, 0, size);
		}
		bos.flush();
		bos.close();
		return out.toByteArray();
	}
}
