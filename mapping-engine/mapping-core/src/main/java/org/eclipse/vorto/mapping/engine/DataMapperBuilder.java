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
package org.eclipse.vorto.mapping.engine;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.EndianUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.Conversion;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.vorto.mapping.engine.functions.ClassFunction;
import org.eclipse.vorto.mapping.engine.functions.IFunction;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.internal.DataMapperJxpath;
import org.eclipse.vorto.mapping.engine.internal.functions.Arrays;
import org.eclipse.vorto.mapping.engine.internal.functions.Base64;
import org.eclipse.vorto.mapping.engine.internal.functions.ConvertUtils;
import org.eclipse.vorto.mapping.engine.internal.functions.CustomFunctionsLibrary;
import org.eclipse.vorto.mapping.engine.internal.functions.DateUtils;
import org.eclipse.vorto.mapping.engine.internal.functions.Jxpath;
import org.eclipse.vorto.mapping.engine.internal.functions.String2Utils;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;

public class DataMapperBuilder {
		
	private IMappingSpecification specification;
	
	private CustomFunctionsLibrary functionLibrary = CustomFunctionsLibrary.createDefault();
	
	private static final IFunction FUNC_CONVERSION = new ClassFunction("conversion", Conversion.class);
	private static final IFunction FUNC_STRING = new ClassFunction("string", StringUtils.class);
	private static final IFunction FUNC_STRING2 = new ClassFunction("string2", String2Utils.class);
	private static final IFunction FUNC_NUMBER = new ClassFunction("number", NumberUtils.class);
	private static final IFunction FUNC_DATE = new ClassFunction("date", DateUtils.class);
	private static final IFunction FUNC_TYPE = new ClassFunction("type", ConvertUtils.class);
	private static final IFunction FUNC_BOOL = new ClassFunction("boolean", BooleanUtils.class);
	private static final IFunction FUNC_BASE64 = new ClassFunction("base64", Base64.class);
	private static final IFunction FUNC_BINARY = new ClassFunction("binaryString", DatatypeConverter.class);
	private static final IFunction FUNC_ENDIAN = new ClassFunction("endian", EndianUtils.class);
	private static final IFunction FUNC_ARRAY = new ClassFunction("array", Arrays.class);
	
	private static final IFunction FUNC_XPATH = new ClassFunction("xpath",Jxpath.class);
	
	private IScriptEvalProvider provider = null;

	protected DataMapperBuilder() {
		registerConverterFunction(FUNC_CONVERSION);
		registerConverterFunction(FUNC_STRING);
		registerConverterFunction(FUNC_STRING2);
		registerConverterFunction(FUNC_NUMBER);
		registerConverterFunction(FUNC_DATE);
		registerConverterFunction(FUNC_TYPE);
		registerConverterFunction(FUNC_BOOL);
		registerConverterFunction(FUNC_BASE64);
		registerConverterFunction(FUNC_BINARY);
		registerConverterFunction(FUNC_ENDIAN);
		registerConverterFunction(FUNC_ARRAY);
		
		registerConditionFunction(FUNC_CONVERSION);
		registerConditionFunction(FUNC_STRING);
		registerConditionFunction(FUNC_STRING2);
		registerConditionFunction(FUNC_NUMBER);
		registerConditionFunction(FUNC_DATE);
		registerConditionFunction(FUNC_TYPE);
		registerConditionFunction(FUNC_BOOL);
		registerConditionFunction(FUNC_BASE64);
		registerConditionFunction(FUNC_BINARY);
		registerConditionFunction(FUNC_ENDIAN);
		registerConditionFunction(FUNC_ARRAY);
		registerConditionFunction(FUNC_XPATH);
	}
	
	public DataMapperBuilder registerConverterFunction(IFunction converter) {
		functionLibrary.addConverterFunction(converter);
		return this;
	}
	
	public DataMapperBuilder registerConditionFunction(IFunction condition) {
		functionLibrary.addConditionFunction(condition);
		return this;
	}
	
	public DataMapperBuilder registerScriptEvalProvider(IScriptEvalProvider provider) {
		this.provider = provider;
		return this;
	}

	public IDataMapper build() {
		this.functionLibrary.addConverterFunctions(specification.getScriptFunctions(this.provider));
		return new DataMapperJxpath(specification,functionLibrary);
	}

	public DataMapperBuilder withSpecification(IMappingSpecification specification) {
		this.specification = specification;
		return this;
	}
	
	
	
}
