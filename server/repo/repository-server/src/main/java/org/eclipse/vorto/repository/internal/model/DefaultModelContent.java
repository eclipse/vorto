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
package org.eclipse.vorto.repository.internal.model;

import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.repository.model.IModelContent;
import org.eclipse.vorto.repository.service.IModelRepository.ContentType;

public class DefaultModelContent implements IModelContent {

	private Model model;
	private ContentType type;
	private byte[] content;
	
	public DefaultModelContent(Model model, ContentType type, byte[] content) {
		this.model = model;
		this.type = type;
		this.content = content;
	}
	
	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public ContentType getType() {
		return type;
	}

	@Override
	public byte[] getContent() {
		return content;
	}

}
