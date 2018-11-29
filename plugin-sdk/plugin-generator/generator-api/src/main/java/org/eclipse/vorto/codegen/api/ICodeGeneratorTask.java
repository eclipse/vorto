/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 *
 *******************************************************************************/

package org.eclipse.vorto.codegen.api;

import org.eclipse.vorto.codegen.api.InvocationContext;

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
