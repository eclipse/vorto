/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
