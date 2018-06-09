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
package org.eclipse.vorto.service.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.EndianUtils;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.ObjectContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.Conversion;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StrSubstitutor;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.service.mapping.internal.JxPathFactory;
import org.eclipse.vorto.service.mapping.internal.converter.Arrays;
import org.eclipse.vorto.service.mapping.internal.converter.Base64;
import org.eclipse.vorto.service.mapping.internal.converter.ConvertUtils;
import org.eclipse.vorto.service.mapping.internal.converter.DateUtils;
import org.eclipse.vorto.service.mapping.internal.converter.Jxpath;
import org.eclipse.vorto.service.mapping.normalized.FunctionblockData;
import org.eclipse.vorto.service.mapping.normalized.InfomodelData;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;

/**
 * 
 * Extend this class in order to implement a platform mapper that maps normlized vorto model 
 * to the target platform specific data model
 *
 * @param <MappingResult>
 */
public abstract class AbstractDataMapper<Result> implements IDataMapper<Result> {

	private IMappingSpecification specification;

	private JxPathFactory jxpathHelper = null;
	
	private static final JexlEngine JEXL = createJexlEngine();
	
	private static final String STEREOTYPE = "source";
	private static final String ATTRIBUTE_XPATH = "xpath";
	private static final Object ATTRIBUTE_CONDITION = "condition";

	public AbstractDataMapper(IMappingSpecification mappingSpecification) {
		this.specification = mappingSpecification;
		this.jxpathHelper = new JxPathFactory(mappingSpecification.getCustomFunctions());
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
        funcs.put("base64", Base64.class);
        funcs.put("binaryString", DatatypeConverter.class);
        funcs.put("xpath", Jxpath.class);
        funcs.put("endian", EndianUtils.class);
        funcs.put("array", Arrays.class);
        jexl.setFunctions(funcs);
		return jexl;
	}

	public Result map(DataInput input, MappingContext mappingContext) {

		JXPathContext context = jxpathHelper.newContext(input.getValue());
		
		InfomodelData normalized = new InfomodelData();
		
		final Infomodel deviceInfoModel = specification.getInfoModel();

		for (ModelProperty fbProperty : deviceInfoModel.getFunctionblocks()) {
			if (mappingContext.isIncluded(fbProperty.getName())) {
				FunctionblockData mappedFb = mapFunctionBlock(fbProperty, context);
				if (mappedFb != null) {
					normalized.withFunctionblock(mappedFb);
				}
			}
		}
		
		return this.doMap(normalized, mappingContext);
	}
	
	private FunctionblockData mapFunctionBlock(ModelProperty fbProperty, JXPathContext context) {
		
		FunctionblockModel fbModel = specification.getFunctionBlock(fbProperty.getName());
		
		FunctionblockData fbData = new FunctionblockData(fbProperty.getName());

		for (ModelProperty statusProperty : fbModel.getStatusProperties()) {

			try {
				Object mapped = this.mapProperty(statusProperty, context);
				if (mapped != null) {
					fbData.withStatusProperty(statusProperty.getName(), mapped);
				}
			} catch (JXPathNotFoundException  ex) {
				if (statusProperty.isMandatory()) {
					return null;
				}
			} catch(JXPathInvalidAccessException ex) {
				if (ex.getCause() instanceof JXPathNotFoundException) {
					if (statusProperty.isMandatory()) {
						return null;
					}
				}
				throw new MappingException("A problem occured during mapping",ex);
			}

		}

		for (ModelProperty configProperty : fbModel.getConfigurationProperties()) {
			
			try {
				Object mapped = this.mapProperty(configProperty, context);
				if (mapped != null) {
					fbData.withConfigurationProperty(configProperty.getName(), mapped);
				}
			} catch (JXPathNotFoundException  ex) {
				if (configProperty.isMandatory()) {
					return null;
				}
			} catch(JXPathInvalidAccessException ex) {
				if (ex.getCause() instanceof JXPathNotFoundException) {
					if (configProperty.isMandatory()) {
						return null;
					}
				}
				throw new MappingException("A problem occured during mapping",ex);
			}
		}

		return onlyReturnIfPopulated(fbData);
	}

	
	private FunctionblockData onlyReturnIfPopulated(FunctionblockData fbData) {
		if (!fbData.getConfiguration().isEmpty() || !fbData.getStatus().isEmpty()) {
			return fbData;
		} else {
			return null;
		}
	}
	
	protected abstract Result doMap(InfomodelData normalized, MappingContext mappingContext);

	private Object mapProperty(ModelProperty property, JXPathContext input) {
		Optional<Stereotype> sourceStereotype = property.getStereotype(STEREOTYPE);
		if (sourceStereotype.isPresent() && hasXpath(sourceStereotype.get().getAttributes())) {
			String expression = replacePlaceHolders(sourceStereotype.get().getAttributes().get(ATTRIBUTE_XPATH),
					sourceStereotype.get().getAttributes());
			
			if (matchesCondition(sourceStereotype.get().getAttributes(), input)) {
				return input.getValue(expression);
			}
		}
		
		return null;

	}
	
	private boolean matchesCondition(Map<String, String> attributes, JXPathContext context) {
		if (attributes.containsKey(ATTRIBUTE_CONDITION) && !attributes.get(ATTRIBUTE_CONDITION).equals("")) {
			Expression e = JEXL.createExpression( normalizeCondition(attributes.get(ATTRIBUTE_CONDITION) ) );
			JexlContext jc = new ObjectContext<Object>(JEXL, context.getContextBean());
			jc.set("this", context.getContextBean());
			return (boolean)e.evaluate(jc);		
		} else {
			return true;
		}
	}

	private String normalizeCondition(final String expression) {
		return expression.replaceAll("/", "\\.");
	}

	private boolean hasXpath(Map<String, String> stereotypeAttributes) {
		return stereotypeAttributes.containsKey(ATTRIBUTE_XPATH)
				&& !stereotypeAttributes.get(ATTRIBUTE_XPATH).equals("");
	}

	private String replacePlaceHolders(String expression, Map<String, String> mappedAttributes) {
		StrSubstitutor sub = new StrSubstitutor(mappedAttributes);
		return sub.replace(expression);
	}
	
}
