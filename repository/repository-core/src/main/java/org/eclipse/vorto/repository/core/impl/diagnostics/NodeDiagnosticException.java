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
package org.eclipse.vorto.repository.core.impl.diagnostics;

public class NodeDiagnosticException extends RuntimeException {

  private static final long serialVersionUID = -2571242394325812217L;

  public NodeDiagnosticException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public NodeDiagnosticException(String arg0) {
    super(arg0);
  }
}
