/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.devtool.projectrepository.model;

/**
 * The class FileUploadHandle represents a file to be uploaded.
 * 
 */
public class FileUploadHandle extends UploadHandle {

	private byte[] content;

	private String encoding;

	public FileUploadHandle(FolderResource folder, String fileName, byte[] content, String encoding) {
		super(folder.getPath() + "/" + fileName);
		this.content = content;
		this.encoding = encoding;
	}
	
	public FileUploadHandle(FolderResource folder, String fileName, byte[] content) {
		super(folder.getPath() + "/" + fileName);
		this.content = content;
		this.encoding = "utf-8";
	}

	public byte[] getContent() {
		return content;
	}

	/**
	 * @return the encoding name for this file upload handle.
	 */
	public String getEncoding() {
		return encoding;
	}

	@Override
	public String toString() {
		return "FileUploadHandle [encoding=" + encoding + ", toString()=" + super.toString() + "]";
	}
	
	
}

/* EOF */
