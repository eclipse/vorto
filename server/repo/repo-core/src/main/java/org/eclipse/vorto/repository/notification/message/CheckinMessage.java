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
package org.eclipse.vorto.repository.notification.message;

import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.model.User;

import java.util.HashMap;
import java.util.Map;

public class CheckinMessage extends AbstractMessage {

	private ModelResource modelResource;
	
	private TemplateRenderer renderer;
	
	public CheckinMessage(User recipient, ModelResource modelResource) {
		super(recipient);
		this.modelResource = modelResource;
		this.renderer = new TemplateRenderer("checkin_watch.ftl");
	}

	@Override
	public String getSubject() {
		return modelResource.getModelType().name()+" was added to the Vorto Repository";
	}

	@Override
	public String getContent() {
		Map<String,Object> ctx = new HashMap<>(1);
		ctx.put("user", recipient);
		ctx.put("model", modelResource);
		try {
			return renderer.render(ctx);
		} catch (Exception e) {
			throw new RuntimeException("Problem rendering registration email content",e);
			
		}
	}

}
