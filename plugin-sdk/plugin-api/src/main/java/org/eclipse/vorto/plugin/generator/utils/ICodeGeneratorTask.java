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
package org.eclipse.vorto.plugin.generator.utils;

import org.eclipse.vorto.plugin.generator.InvocationContext;

/*
 * A specific generation task during the entire Code generation process
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public interface ICodeGeneratorTask<InformationModelFragment> {

  /**
   * Generates code from the specified context and sends it to the specified outputter
   */
  void generate(InformationModelFragment element, InvocationContext context,
      IGeneratedWriter writer);
}
