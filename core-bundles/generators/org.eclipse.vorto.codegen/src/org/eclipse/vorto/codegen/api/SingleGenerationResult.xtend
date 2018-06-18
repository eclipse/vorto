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
class SingleGenerationResult implements IGeneratedWriter, IGenerationResult {
	
	String fileName;
	String mediatype;
	byte[] content;
	
	new(String mediatype) {
		this.mediatype = mediatype;
	}
		
	def override String getFileName() {
		return fileName;
	}
	
	def override String getMediatype() {
		return mediatype;
	}
	
	def override byte[] getContent() {
		return content;
	}
	
	override write(Generated generated) {
		this.content = generated.content
		this.fileName = generated.fileName;
	}	
}
