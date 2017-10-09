package org.eclipse.vorto.service.mapping.converters;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

public class DateUtils {
	
	public static String format(final long time) {
		return DateFormatUtils.format(time, "yyyy-MM-dd HH:mm:ssZ");
	}
	
	public static String format(final long time, String pattern) {
		return DateFormatUtils.format(new Date(time), pattern);
	}
}
