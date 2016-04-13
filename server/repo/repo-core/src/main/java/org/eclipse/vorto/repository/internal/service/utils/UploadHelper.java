/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.repository.internal.service.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.model.ModelType;
import org.eclipse.vorto.repository.model.ZipData;


/**
 * Helper class to related to file operations during model uploads.
 * @author Nagavijay Sivakumar - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class UploadHelper {
	
	public static Set<ZipData> extractZipFile(String zipFileName) {
		Set<ZipData> modelsList = new HashSet<ZipData>();
		try {
			ZipFile zipFile = new ZipFile(zipFileName);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				File destination = new File(getDefaultExtractDirectory(), entry.getName());
				if (entry.isDirectory()) {
					destination.mkdirs();
				} else {
					destination.getParentFile().mkdirs();
					InputStream in = zipFile.getInputStream(entry);
					OutputStream out = new FileOutputStream(destination);
					IOUtils.copy(in, out);
					IOUtils.closeQuietly(in);
					out.close();
					modelsList.add(buildZipData(entry));
				}
			}
			modelsList.removeIf(Objects::isNull);
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return modelsList;
	}

	private static ZipData buildZipData(ZipEntry entry) {
		ZipData zipData = null;
		String fileName = getDefaultExtractDirectory() + "/" + entry.getName();
		//Skip if the project contains shared_models
		if(!fileName.contains("shared_models")) {
			if (fileName.endsWith(ModelType.Datatype.getExtension())) {
				return new ZipData(fileName, ModelType.Datatype);
			} else if(fileName.endsWith(ModelType.Functionblock.getExtension())) {
				return new ZipData(fileName, ModelType.Functionblock);
			} else if(fileName.endsWith(ModelType.InformationModel.getExtension())) {
				return new ZipData(fileName, ModelType.InformationModel);
			} else if(fileName.endsWith(ModelType.Mapping.getExtension())) {
				return new ZipData(fileName, ModelType.Mapping);
			}			
		}
		return zipData;

	}
	
	public static String getDefaultExtractDirectory() {
		return FilenameUtils.normalize(FileUtils.getTempDirectory().getPath() + "/vorto", true);
	}
}
