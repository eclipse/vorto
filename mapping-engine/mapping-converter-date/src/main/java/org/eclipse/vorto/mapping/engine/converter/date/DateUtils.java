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
package org.eclipse.vorto.mapping.engine.converter.date;

import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

  public static String format(final long time) {
    return DateFormatUtils.format(time, "yyyy-MM-dd HH:mm:ssZ");
  }

  public static String format(final long time, String pattern) {
    return DateFormatUtils.format(new Date(time), pattern);
  }
}
