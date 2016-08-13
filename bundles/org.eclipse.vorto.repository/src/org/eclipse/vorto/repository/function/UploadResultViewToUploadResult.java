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
package org.eclipse.vorto.repository.function;

import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.api.repository.UploadResult;
import org.eclipse.vorto.repository.model.ModelView;
import org.eclipse.vorto.repository.model.UploadResultView;

import com.google.common.base.Function;

public class UploadResultViewToUploadResult implements Function<UploadResultView, UploadResult> {

	private Function<ModelView, ModelResource> converter;
	
	public UploadResultViewToUploadResult(Function<ModelView, ModelResource> converter) {
		this.converter = converter;
	}

	@Override
	public UploadResult apply(UploadResultView input) {
		UploadResult result = new UploadResult();
		result.setHandleId(input.getHandleId());
		result.setErrorMessage(input.getErrorMessage());
		result.setValid(input.isValid());
		result.setModelResource(converter.apply(input.getModelResource()));
		result.setUnresolvedReferences(input.getUnresolvedReferences());
		return result;
	}

}
