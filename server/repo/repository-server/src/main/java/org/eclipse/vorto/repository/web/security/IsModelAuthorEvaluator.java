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
package org.eclipse.vorto.repository.web.security;

import java.io.Serializable;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class IsModelAuthorEvaluator implements PermissionEvaluator {

	private IModelRepository repository;

	public IsModelAuthorEvaluator(IModelRepository repository) {
		this.repository = repository;
	}

	public IsModelAuthorEvaluator() {
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		final String callerId = authentication.getName();

		if (targetDomainObject instanceof ModelId) {
			ModelInfo modelInfo = this.repository.getById((ModelId) targetDomainObject);
			if (modelInfo != null) {
				return modelInfo.getAuthor().equalsIgnoreCase(callerId);
			}
		} else if (targetDomainObject instanceof String) {
			return callerId.equalsIgnoreCase((String)targetDomainObject);
		}
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return false;
	}

}
