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
package org.eclipse.vorto.mapping.engine.model.spec;

import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.Infomodel;

/**
 * Mapping Specification that combines the Information Model properties merged with the target
 * platform attributes defined in the mapping models.
 *
 */
public interface IMappingSpecification {

  /**
   * Information Model of the device
   * 
   * @return
   */
  Infomodel getInfoModel();

  /**
   * Resolves the referenced function block by the given fb property name
   * 
   * @param modelId
   * @return
   */
  FunctionblockModel getFunctionBlock(String propertyName);

  /**
   * Gets all custom script functions, used in the mapping specification
   * 
   * @return
   */
  FunctionLibrary getScriptFunctions(IScriptEvalProvider evalProvider);

  public static MappingSpecBuilder newBuilder() {
    return new MappingSpecBuilder();
  }
}
