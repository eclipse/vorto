/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.server.config.config;

import java.io.IOException;

import org.eclipse.vorto.model.BooleanAttributeProperty;
import org.eclipse.vorto.model.EnumAttributeProperty;
import org.eclipse.vorto.model.IPropertyAttribute;
import org.eclipse.vorto.model.IReferenceType;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.PrimitiveType;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class BaseConfiguration {

	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
	    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
	    builder.deserializerByType(IReferenceType.class, new JsonDeserializer<IReferenceType>() {

			@Override
			public IReferenceType deserialize(JsonParser parser, DeserializationContext context)
					throws IOException, JsonProcessingException {
				try {
					return parser.readValueAs(ModelId.class);
				} catch(IOException ioEx) {
					try {
						return parser.readValueAs(PrimitiveType.class);
					} catch(IOException ex) {
						ex.printStackTrace();
						return null;
					}
				}
			}
	    	
	    });
	    builder.deserializerByType(IPropertyAttribute.class, new JsonDeserializer<IPropertyAttribute>() {

			@Override
			public IPropertyAttribute deserialize(JsonParser parser, DeserializationContext context)
					throws IOException, JsonProcessingException {
				try {
					return parser.readValueAs(BooleanAttributeProperty.class);
				} catch(IOException ioEx) {
					try {
						return parser.readValueAs(EnumAttributeProperty.class);
					} catch(IOException ex) {
						ex.printStackTrace();
						return null;
					}
				}
			}
	    	
	    });

	    return builder;
	}
}
