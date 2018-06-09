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
package org.eclipse.vorto.service.mapping.internal;

import java.util.Optional;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.EndianUtils;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.Functions;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.jxpath.util.BasicTypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.Conversion;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.vorto.service.mapping.internal.converter.Arrays;
import org.eclipse.vorto.service.mapping.internal.converter.Base64;
import org.eclipse.vorto.service.mapping.internal.converter.ConvertUtils;
import org.eclipse.vorto.service.mapping.internal.converter.DateUtils;
import org.eclipse.vorto.service.mapping.internal.converter.String2Utils;

public class JxPathFactory {

	private FunctionLibrary converterLibrary;
	
	private boolean lenient = false;
	
	public JxPathFactory(Optional<Functions> functionsFromMappings) {
		this.converterLibrary = new FunctionLibrary();

		this.converterLibrary.addFunctions(new ClassFunctions(Conversion.class, "conversion"));
		this.converterLibrary.addFunctions(new ClassFunctions(StringUtils.class, "string"));
		this.converterLibrary.addFunctions(new ClassFunctions(String2Utils.class, "string2"));
		this.converterLibrary.addFunctions(new ClassFunctions(NumberUtils.class, "number"));
		this.converterLibrary.addFunctions(new ClassFunctions(DateUtils.class, "date"));
		this.converterLibrary.addFunctions(new ClassFunctions(ConvertUtils.class, "type"));
		this.converterLibrary.addFunctions(new ClassFunctions(BooleanUtils.class, "boolean"));
		this.converterLibrary.addFunctions(new ClassFunctions(Base64.class, "base64"));
		this.converterLibrary.addFunctions(new ClassFunctions(DatatypeConverter.class, "binaryString"));
		this.converterLibrary.addFunctions(new ClassFunctions(EndianUtils.class, "endian"));
		this.converterLibrary.addFunctions(new ClassFunctions(Arrays.class, "array"));
		
		if (functionsFromMappings.isPresent()) {
			converterLibrary.addFunctions(functionsFromMappings.get());
		}
	}
	
	public JxPathFactory() {
		this(Optional.empty());
	}
	
	public JXPathContext newContext(Object ctxObject) {
		JXPathContext context = JXPathContext.newContext(ctxObject);
		TypeUtils.setTypeConverter(new MyTypeConverter());
		context.setFunctions(converterLibrary);
		context.setLenient(this.lenient);
		return context;
	}
	
	public void setLenient(boolean lenient) {
		this.lenient = lenient;
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
