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
package org.eclipse.vorto.service.mapping.ditto;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.Functions;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang3.Conversion;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StrSubstitutor;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.IReferenceType;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.PrimitiveType;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.service.mapping.DataInput;
import org.eclipse.vorto.service.mapping.IDataMapper;
import org.eclipse.vorto.service.mapping.IMappingSpecification;
import org.eclipse.vorto.service.mapping.MappingContext;

/**
 * 
 * Maps data input to Eclipse Ditto / Vorto compliant data format
 *
 */
public class DittoMapper implements IDataMapper<DittoOutput> {

	private IMappingSpecification specification;
	
	private FunctionLibrary converterLibrary;
	
	private static final String STEREOTYPE = "source";
	private static final String ATTRIBUTE_VALUE = "value";
	private static final String ATTRIBUTE_XPATH = "xpath";
	
	public DittoMapper(IMappingSpecification loader,ClassFunctions customFunctions) {
		this.specification = loader;
		this.converterLibrary = new FunctionLibrary();
		if (customFunctions != null) {
			this.converterLibrary.addFunctions(customFunctions);
		}
		this.converterLibrary.addFunctions(new ClassFunctions(Conversion.class, "conversion"));
		this.converterLibrary.addFunctions(new ClassFunctions(StringUtils.class, "string"));
		this.converterLibrary.addFunctions(new ClassFunctions(NumberUtils.class, "number"));
		
		Optional<Functions> functionsFromMappings = loader.getCustomFunctions();
		if (functionsFromMappings.isPresent()) {
			converterLibrary.addFunctions(functionsFromMappings.get());
		}
	}
	
	public DittoOutput map(DataInput input, MappingContext mappingContext) {
	
		JXPathContext context = newContext(input.getValue());
		context.setFunctions(converterLibrary);

		DittoOutput output = new DittoOutput();
		
		final Infomodel deviceInfoModel = specification.getInfoModel();
		
		for (ModelProperty fbProperty : deviceInfoModel.getFunctionblocks()) {
			if (mappingContext.isIncluded(fbProperty.getName())) {
				output.withFeature(mapFunctionBlock(fbProperty, context));
			}
		}
	
		return output;
	}
	
	private Feature mapFunctionBlock(ModelProperty fbProperty, JXPathContext context) {		
		FeatureBuilder featureBuilder = Feature.newBuilder(fbProperty.getName());
		
		FunctionblockModel fbModel = specification.getFunctionBlock((ModelId)fbProperty.getType());
		
		for (ModelProperty statusProperty : fbModel.getStatusProperties()) {
			Optional<Stereotype> sourceStereotype = statusProperty.getStereotype(STEREOTYPE);
			if (sourceStereotype.isPresent()) {
				if (sourceStereotype.get().getAttributes().containsKey(ATTRIBUTE_VALUE)) {
					String value = sourceStereotype.get().getAttributes().get(ATTRIBUTE_VALUE);
					featureBuilder.withStatusProperty(statusProperty.getName(), toType(value,statusProperty.getType()));
				} else if (sourceStereotype.get().getAttributes().containsKey(ATTRIBUTE_XPATH)) {
					String expression = replacePlaceHolders(sourceStereotype.get().getAttributes().get(ATTRIBUTE_XPATH),sourceStereotype.get().getAttributes());
					featureBuilder.withStatusProperty(statusProperty.getName(), context.getValue(expression));
				}
			}
		}
		
		for (ModelProperty configProperty : fbModel.getConfigurationProperties()) {
			Optional<Stereotype> sourceStereotype = configProperty.getStereotype(STEREOTYPE);
			if (sourceStereotype.isPresent()) {
				if (sourceStereotype.get().getAttributes().containsKey(ATTRIBUTE_VALUE)) {
					String value = sourceStereotype.get().getAttributes().get(ATTRIBUTE_VALUE);
					featureBuilder.withStatusProperty(configProperty.getName(), toType(value,configProperty.getType()));
				} else if (sourceStereotype.get().getAttributes().containsKey(ATTRIBUTE_XPATH)) {
					String expression = replacePlaceHolders(sourceStereotype.get().getAttributes().get(ATTRIBUTE_XPATH),sourceStereotype.get().getAttributes());
					
					featureBuilder.withConfigurationProperty(configProperty.getName(), context.getValue(expression));
				}
			}
		}
		
		return featureBuilder.build();
	}
	
	private String replacePlaceHolders(String expression, Map<String, String> mappedAttributes) {
		StrSubstitutor sub = new StrSubstitutor(mappedAttributes);
		return sub.replace(expression);
	}

	private Object toType(String value, IReferenceType type) {
		if (type == PrimitiveType.BOOLEAN) {
			return Boolean.parseBoolean(value);
		} else if (type == PrimitiveType.FLOAT) {
			return Float.parseFloat(value);
		} else if (type == PrimitiveType.INT) {
			return Integer.parseInt(value);
		} else if (type == PrimitiveType.DOUBLE) {
			return Double.parseDouble(value);
		} else if (type == PrimitiveType.STRING) {
			return value;
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
    private JXPathContext newContext(Object ctxObject) {
    	JXPathContext context = JXPathContext.newContext(ctxObject);
    	context.setLenient(true);	
    	return context;
    }

}
