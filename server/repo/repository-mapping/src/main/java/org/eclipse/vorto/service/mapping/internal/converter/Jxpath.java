package org.eclipse.vorto.service.mapping.internal.converter;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.jxpath.util.BasicTypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.Conversion;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class Jxpath {

	private static FunctionLibrary converterLibrary;
	
	static {
		converterLibrary = new FunctionLibrary();

		converterLibrary.addFunctions(new ClassFunctions(Conversion.class, "conversion"));
		converterLibrary.addFunctions(new ClassFunctions(StringUtils.class, "string"));
		converterLibrary.addFunctions(new ClassFunctions(NumberUtils.class, "number"));
		converterLibrary.addFunctions(new ClassFunctions(DateUtils.class, "date"));
		converterLibrary.addFunctions(new ClassFunctions(ConvertUtils.class, "type"));
		converterLibrary.addFunctions(new ClassFunctions(BooleanUtils.class, "boolean"));
		converterLibrary.addFunctions(new ClassFunctions(Base64.class, "base64"));
		converterLibrary.addFunctions(new ClassFunctions(DatatypeConverter.class, "binaryString"));
	}
	
	public static Object eval(String exp, Object value) {
		JXPathContext context = newContext(value);
		context.setFunctions(converterLibrary);
		return context.getValue(exp.replaceAll("\\.", "/"));
	}
	
	private static JXPathContext newContext(Object ctxObject) {
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
