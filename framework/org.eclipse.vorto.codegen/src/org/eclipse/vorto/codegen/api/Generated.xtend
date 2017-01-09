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
package org.eclipse.vorto.codegen.api

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class Generated {
	public static final String ROOT_FOLDER_PATH = null;
	
	String fileName;
	String folderPath;
	byte[] content;
	
	new(String fileName, String folderPath, byte[] content) {
		this.fileName = fileName;
		this.folderPath = folderPath;
		this.content = content;
	}
	
	new(String fileName, String folderPath, String content) {
		this(fileName,folderPath,content.bytes)
	}
	
	def String getFileName() {
		return fileName;
	}
	
	def String getFolderPath() {
		return folderPath;
	}
	
	def byte[] getContent() {
		return content;
	}
	
	def boolean isDirectory() {
		return fileName == null;
	}
}
