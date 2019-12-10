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
package org.eclipse.vorto.repository.oauth.internal;

public class MalformedElement extends RuntimeException {

  private static final long serialVersionUID = -479693127250022807L;

  public MalformedElement(String entireString, String malformedElement) {
    super("The string '" + entireString + "' is malformed. Check '" + malformedElement + "'");
  }
  
  public MalformedElement(String malformedString) {
    super("String is malformed:" + malformedString);
  }
}
