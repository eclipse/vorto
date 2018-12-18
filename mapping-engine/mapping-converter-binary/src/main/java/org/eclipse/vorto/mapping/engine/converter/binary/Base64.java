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
package org.eclipse.vorto.mapping.engine.converter.binary;

public class Base64 extends org.apache.commons.codec.binary.Base64 {

  public static byte[] decodeString(String value) {
    return org.apache.commons.codec.binary.Base64.decodeBase64(value);
  }

  public static byte[] decodeByteArray(byte[] value) {
    return org.apache.commons.codec.binary.Base64.decodeBase64(value);
  }
}
