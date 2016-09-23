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
package org.eclipse.vorto.repository.internal.service.notification.message;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.vorto.repository.model.User;

public class RegistrationMessage extends AbstractMessage {
	
	private TemplateRenderer renderer;
	
	public RegistrationMessage(User registeredPerson) {
		super(registeredPerson);
		this.renderer = new TemplateRenderer("registration.ftl");
	}

	@Override
	public String getSubject() {
		return "Vorto Repository Registration";
	}

	@Override
	public String getContent() {
		Map<String,Object> ctx = new HashMap<>(1);
		ctx.put("user", recipient);
		try {
			return renderer.render(ctx);
		} catch (Exception e) {
			throw new RuntimeException("Problem rendering registration email content",e);
			
		}
	}
}
