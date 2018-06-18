/*******************************************************************************
 * Copyright (c) 2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.json.templates;

import org.apache.log4j.Logger;
import org.eclipse.vorto.codegen.api.IFileTemplate;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonIMTemplate implements IFileTemplate<InformationModel> {

	private static final Logger LOG = Logger.getLogger(JsonIMTemplate.class);

	@Override
	public String getContent(InformationModel model, InvocationContext context) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		String result = "";
		try {
			result = objectMapper.writeValueAsString(model);
		} catch (JsonProcessingException e) {
			LOG.error("Could not serialize model.", e);
		}
		return result;
	}

	@Override
	public String getFileName(InformationModel model) {
		return model.getName().toLowerCase() + ".json";
	}

	@Override
	public String getPath(InformationModel model) {
		return null;
	}
}