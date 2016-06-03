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
package org.eclipse.vorto.codegen.api;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.core.api.model.model.ModelId;

/**
 * 
 * @author Alexander Edelmann
 *
 */
public class ZipContentExtractCodeGeneratorTask implements ICodeGeneratorTask<ModelId> {

	private byte[] zipContent;
	
	private boolean isMavenContent = false;

	public ZipContentExtractCodeGeneratorTask(byte[] zipContent) {
		this.zipContent = zipContent;
	}

	@Override
	public void generate(ModelId infoModelId,InvocationContext context, IGeneratedWriter outputter) {
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipContent));
		
		try {
		
			ZipEntry ze = null;
			while ((ze = zis.getNextEntry()) != null) {
				if (ze.isDirectory()) {
					outputter.write(new Generated(null, ze.getName(), new byte[0]));
				} else {
					int indexOfLastSlash = ze.getName().lastIndexOf("/");
					String fileName = null;
					String folderName = null;
					if (indexOfLastSlash > -1) {
						fileName = ze.getName().substring(indexOfLastSlash+1);
						folderName = ze.getName().substring(0,indexOfLastSlash);
					} else {
						fileName = ze.getName();
					}
					
					outputter.write(new Generated(fileName,folderName,new String(copyStream(zis),"utf-8")));
					
					if (fileName.equalsIgnoreCase("pom.xml")) {
						this.isMavenContent = true;
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (zis != null) {
				try {
					zis.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	private byte[] copyStream(ZipInputStream in) throws Exception {
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
	
	public boolean isMavenContent() {
		return this.isMavenContent;
	}
}
