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
package org.eclipse.vorto.repository.web.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Helper class for file operations tasks for upload, extract etc.
 * @author Nagavijay Sivakumar - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class FileHelper {

	public static void copyFileToTempLocation(MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File(getDefaultExtractDirectory() + "/" + file.getOriginalFilename())));
			FileCopyUtils.copy(file.getInputStream(), stream);
			stream.close();
		}
	}

	public static String getDefaultExtractDirectory() {
		String defaultDirectory = FilenameUtils
				.normalize(FileUtils.getTempDirectory().getPath() + "/" + "vorto", true);
		
		if (!new File(defaultDirectory).exists()) {
			new File(defaultDirectory).mkdirs();
		}
		return defaultDirectory;
	}

	public static void deleteUploadedFile(MultipartFile file) {
		FileUtils.deleteQuietly(new File(getDefaultExtractDirectory() + "/" + file.getOriginalFilename()));
	}

	public static void deleteTempExtractFolder() {
		FileUtils.deleteQuietly(new File(getDefaultExtractDirectory()));
	}

	public static void deleteUploadedFile(String handleId) {
		FileUtils.deleteQuietly(new File(handleId));
	}
}
