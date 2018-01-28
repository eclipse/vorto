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
package org.eclipse.vorto.service.mapping.internal.ditto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.Functions;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.jxpath.util.BasicTypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.Conversion;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StrSubstitutor;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.service.mapping.DataInput;
import org.eclipse.vorto.service.mapping.IDataMapper;
import org.eclipse.vorto.service.mapping.MappingContext;
import org.eclipse.vorto.service.mapping.ditto.DittoData;
import org.eclipse.vorto.service.mapping.ditto.Feature;
import org.eclipse.vorto.service.mapping.internal.converter.ConvertUtils;
import org.eclipse.vorto.service.mapping.internal.converter.DateUtils;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;

/**
 * 
 * Maps data input to Eclipse Ditto / Vorto compliant data format
 *
 */
public class DittoMapper implements IDataMapper<DittoData> {

	private IMappingSpecification specification;

	private FunctionLibrary converterLibrary;

	private static final String STEREOTYPE = "source";
	private static final String ATTRIBUTE_XPATH = "xpath";

	private static final Object ATTRIBUTE_CONDITION = "condition";
	
	private static final JexlEngine JEXL = createJexlEngine();

	public DittoMapper(IMappingSpecification mappingSpecification) {
		this.specification = mappingSpecification;
		this.converterLibrary = new FunctionLibrary();

		this.converterLibrary.addFunctions(new ClassFunctions(Conversion.class, "conversion"));
		this.converterLibrary.addFunctions(new ClassFunctions(StringUtils.class, "string"));
		this.converterLibrary.addFunctions(new ClassFunctions(NumberUtils.class, "number"));
		this.converterLibrary.addFunctions(new ClassFunctions(DateUtils.class, "date"));
		this.converterLibrary.addFunctions(new ClassFunctions(ConvertUtils.class, "type"));
		this.converterLibrary.addFunctions(new ClassFunctions(BooleanUtils.class, "boolean"));

		Optional<Functions> functionsFromMappings = mappingSpecification.getCustomFunctions();
		if (functionsFromMappings.isPresent()) {
			converterLibrary.addFunctions(functionsFromMappings.get());
		}
	}
	
	private static JexlEngine createJexlEngine() {
		JexlEngine jexl = new JexlEngine();
		Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("conversion", Conversion.class);
        funcs.put("string", StringUtils.class);
        funcs.put("number", NumberUtils.class);
        funcs.put("date", DateUtils.class);
        funcs.put("type", ConvertUtils.class);
        funcs.put("boolean", BooleanUtils.class);
        jexl.setFunctions(funcs);
		return jexl;
	}

	public DittoData map(DataInput input, MappingContext mappingContext) {

		JXPathContext context = newContext(input.getValue());
		context.setFunctions(converterLibrary);

		DittoData output = new DittoData();

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

		FunctionblockModel fbModel = specification.getFunctionBlock(fbProperty.getName());

		for (ModelProperty statusProperty : fbModel.getStatusProperties()) {
			Optional<Stereotype> sourceStereotype = statusProperty.getStereotype(STEREOTYPE);
			if (sourceStereotype.isPresent() && hasXpath(sourceStereotype.get().getAttributes())) {
				String expression = replacePlaceHolders(sourceStereotype.get().getAttributes().get(ATTRIBUTE_XPATH),
						sourceStereotype.get().getAttributes());
				try {
					if (matchesCondition(sourceStereotype.get().getAttributes(),context)) {
						featureBuilder.withStatusProperty(statusProperty.getName(), context.getValue(expression));
					}
				} catch (JXPathNotFoundException | JXPathInvalidAccessException ex) {
					if (statusProperty.isMandatory()) {
						return new EmptyFeature(fbProperty.getName());
					}
				}
			}
		}

		for (ModelProperty configProperty : fbModel.getConfigurationProperties()) {
			Optional<Stereotype> sourceStereotype = configProperty.getStereotype(STEREOTYPE);
			if (sourceStereotype.isPresent() && hasXpath(sourceStereotype.get().getAttributes())) {
				if (sourceStereotype.get().getAttributes().containsKey(ATTRIBUTE_XPATH)) {
					String expression = replacePlaceHolders(sourceStereotype.get().getAttributes().get(ATTRIBUTE_XPATH),
							sourceStereotype.get().getAttributes());

					try {
						featureBuilder.withConfigurationProperty(configProperty.getName(),
								context.getValue(expression));
					} catch (JXPathNotFoundException | JXPathInvalidAccessException ex) {
						if (configProperty.isMandatory()) {
							return new EmptyFeature(fbProperty.getName());
						}
					}
				}
			}
		}

		return featureBuilder.build();
	}

	private boolean matchesCondition(Map<String, String> attributes, JXPathContext context) {
		if (attributes.containsKey(ATTRIBUTE_CONDITION) && !attributes.get(ATTRIBUTE_CONDITION).equals("")) {
			Expression e = JEXL.createExpression( attributes.get(ATTRIBUTE_CONDITION) );
			JexlContext jc = new MapContext();
			jc.set("value", context.getContextBean());
			return (boolean)e.evaluate(jc);		
		} else {
			return true;
		}
	}

	private boolean hasXpath(Map<String, String> stereotypeAttributes) {
		return stereotypeAttributes.containsKey(ATTRIBUTE_XPATH)
				&& !stereotypeAttributes.get(ATTRIBUTE_XPATH).equals("");
	}

	private String replacePlaceHolders(String expression, Map<String, String> mappedAttributes) {
		StrSubstitutor sub = new StrSubstitutor(mappedAttributes);
		return sub.replace(expression);
	}

	private JXPathContext newContext(Object ctxObject) {
		JXPathContext context = JXPathContext.newContext(ctxObject);
		TypeUtils.setTypeConverter(new MyTypeConverter());
		context.setLenient(false);
		return context;
	}

	public static class MyTypeConverter extends BasicTypeConverter {

		@SuppressWarnings("rawtypes")
		@Override
		public Object convert(Object object, final Class toType) {
			if (object instanceof BasicNodeSet && ((BasicNodeSet) object).getValues().isEmpty()) {
				throw new JXPathNotFoundException("Could not find path in source");
			} else {
				return super.convert(object, toType);
			}
		}
	}

}
