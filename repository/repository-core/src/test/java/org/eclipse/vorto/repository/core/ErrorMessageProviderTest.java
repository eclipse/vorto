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
package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import org.eclipse.vorto.repository.core.impl.parser.ErrorMessageProvider;
import org.junit.Test;

public class ErrorMessageProviderTest {

  @Test
  public void testErrorMessageProvider() {
    ErrorMessageProvider provider = new ErrorMessageProvider();
    assertEquals("", provider.convertError(""));
    assertEquals("test", provider.convertError("test"));
    assertEquals("Symbol '-' is not a valid character for property name.",
        provider.convertError("mismatched input '-' expecting 'as'"));
    assertEquals("Malformed property.",
        provider.convertError("extraneous input 'has' expecting 'as'"));
  }

}
