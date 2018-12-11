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
package org.eclipse.vorto.codegen.api;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 * 
 *         Outputter for generated content produced by the {@link ICodeGenerator}
 */
public interface IGeneratedWriter {

  /**
   * Outputs the specified generated artifact
   */
  void write(Generated generated);
}
