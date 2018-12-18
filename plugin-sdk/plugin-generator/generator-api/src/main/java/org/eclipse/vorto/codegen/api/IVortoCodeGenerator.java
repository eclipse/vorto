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

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public interface IVortoCodeGenerator {

  /**
   * Converts the given information models into platform specific code
   * 
   * @param model information model containing functionblocks and datatype metadata of a specific
   *        device
   * @param context generation invocation context, e.g. mapping rules or invocation parameters
   * @return
   */
  IGenerationResult generate(InformationModel model, InvocationContext context,
      IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException;

  /**
   * Unique service key of the generator. This key correlates to the mapping model service key
   * identifier.
   * 
   * @return
   */
  String getServiceKey();

  /**
   * Gets more information about the generator.
   * 
   * @return
   */
  GeneratorInfo getInfo();



}
