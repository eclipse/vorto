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
package org.eclipse.vorto.perspective.view;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.vorto.codegen.api.tasks.Generated;
import org.eclipse.vorto.codegen.api.tasks.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.IOutputter;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.repository.Attachment;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelRepositoryFactory;

/**
 * Code Generation Task that executes the code generation remotely on the repository server
 * 
 * @author Alexander Edelmann
 *
 */
public class RemoteCodeGeneratorTask implements ICodeGeneratorTask<ModelId> {

	private String serviceKey;

	public RemoteCodeGeneratorTask(String serviceKey) {
		this.serviceKey = serviceKey;
	}

	@Override
	public void generate(ModelId infoModelId, IOutputter outputter) {
		IModelRepository modelRepo = ModelRepositoryFactory.getModelRepository();
		Attachment attachment = modelRepo.generateCode(infoModelId, serviceKey);

		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(attachment.getContent()));
		try {
			

			ZipEntry ze = null;
			while ((ze = zis.getNextEntry()) != null) {
				if (ze.isDirectory()) {
					outputter.output(new Generated(null, ze.getName(), new byte[0]));
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
					
					outputter.output(new Generated(fileName,folderName,new String(copyStream(zis),"utf-8")));
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
}
