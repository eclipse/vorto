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
package org.eclipse.vorto.service.mapping.internal.cc;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.jxpath.JXPathContext;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.Operation;
import org.eclipse.vorto.repository.api.content.Param;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.service.mapping.DataInput;
import org.eclipse.vorto.service.mapping.IDataMapper;
import org.eclipse.vorto.service.mapping.MappingResult;
import org.eclipse.vorto.service.mapping.MappingContext;
import org.eclipse.vorto.service.mapping.internal.DynamicBean;
import org.eclipse.vorto.service.mapping.internal.JxPathFactory;
import org.eclipse.vorto.service.mapping.json.JsonData;
import org.eclipse.vorto.service.mapping.normalized.Command;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandMapper implements IDataMapper<JsonData> {

	private IMappingSpecification mappingSpecification;
	
	private static final String STEREOTYPE = "target";
	private static final String ATTRIBUTE_XPATH = "key";
	private static final String ATTRIBUTE_VALUE = "value";
	
	private JxPathFactory jxpathFactory = null;
	
	public CommandMapper(IMappingSpecification specification) {
		this.mappingSpecification = specification;
		this.jxpathFactory = new JxPathFactory(specification.getCustomFunctions());
		this.jxpathFactory.setLenient(true);
	}
	
	@Override
	public JsonData map(DataInput input, MappingContext context) {
		Command cmd = (Command)input.getValue();
		DynamicBean mappedPayload = new DynamicBean(jxpathFactory);
		
		final Infomodel deviceInfoModel = mappingSpecification.getInfoModel();

		for (ModelProperty fbProperty : deviceInfoModel.getFunctionblocks()) {
			FunctionblockModel fbModel = mappingSpecification.getFunctionBlock(fbProperty.getName());
			for (Operation operation : fbModel.getOperations()) {
				Optional<Stereotype> operationStereotype = operation.getStereotype(STEREOTYPE);
				if (operationStereotype.isPresent() && hasXpath(operationStereotype.get().getAttributes())) {
					final String expression = operationStereotype.get().getAttributes().get(ATTRIBUTE_XPATH);
					final String value = operationStereotype.get().getAttributes().get(ATTRIBUTE_VALUE);
					mappedPayload.setProperty(expression, value);
				}
				operation.getParams().stream().forEach(param -> {
					param.getStereotypes().stream().filter(s -> s.getName().equals(STEREOTYPE)).forEach(paramStereotype -> {
						if (hasXpath(paramStereotype.getAttributes())) {
							final String expression = paramStereotype.getAttributes().get(ATTRIBUTE_XPATH);
							final Object value = replacePlaceHolders(paramStereotype.getAttributes().get(ATTRIBUTE_VALUE),param,cmd.getParams());
							mappedPayload.setProperty(expression, value);
						}
					});					
				});
				
			}
		}
		
		ObjectMapper mapper = new ObjectMapper(); 
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mapper.writeValue(baos, mappedPayload.asMap());
			
			return new JsonData() {
				
				@Override
				public String toJson() {
					return new String(baos.toByteArray());
				}
			};
		} catch (Exception e) {
			throw new IllegalArgumentException("Provided json not valid");
		}
	}
	
	private Object replacePlaceHolders(String expression, Param param, Map<String,Object> values) {
		JXPathContext jxpathCtx = jxpathFactory.newContext(values);
		return jxpathCtx.getValue(expression);
	}
	
	private boolean hasXpath(Map<String, String> stereotypeAttributes) {
		return stereotypeAttributes.containsKey(ATTRIBUTE_XPATH)
				&& !stereotypeAttributes.get(ATTRIBUTE_XPATH).equals("");
	}

}
