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
import org.eclipse.vorto.mapping.engine.functions.IScriptEvaluator;
import org.eclipse.vorto.mapping.engine.functions.ScriptClassFunction;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.IMappedElement;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.Stereotype;

public class MappingSpecification implements IMappingSpecification {

  private static final String STEREOTYPE_NAMESPACE = "_namespace";

  private static final String STEREOTYPE_FUNCTIONS = "functions";

  private Infomodel infoModel;
  
  public MappingSpecification(Infomodel infoModel) {
    this();
    this.infoModel = infoModel;
  }

  public MappingSpecification() {}

  public void setInfoModel(Infomodel infoModel) {
    this.infoModel = infoModel;
  }

  @Override
  public Infomodel getInfoModel() {
    return infoModel;
  }

  @Override
  public FunctionblockModel getFunctionBlock(String name) {
    return (FunctionblockModel)this.infoModel.getFunctionblocks().stream().filter(p -> p.getName().equals(name)).findAny().get().getType();
  }
  
  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider factory) {
    FunctionLibrary library = new FunctionLibrary();
    this.infoModel.getFunctionblocks().stream().forEach(property -> {
      IMappedElement mappedElement = (IMappedElement)property.getType();
      if (mappedElement.getStereotype(STEREOTYPE_FUNCTIONS).isPresent()) {
        Stereotype functionsStereoType = mappedElement.getStereotype(STEREOTYPE_FUNCTIONS).get();
        IScriptEvaluator evaluator = factory.createEvaluator(property.getName().toLowerCase());
        functionsStereoType.getAttributes().keySet().stream()
            .filter(functionName -> !functionName.equalsIgnoreCase(STEREOTYPE_NAMESPACE)).forEach(
                functionName -> evaluator.addScriptFunction(new ScriptClassFunction(functionName,
                    functionsStereoType.getAttributes().get(functionName))));
        library.addFunctions(evaluator.getFunctions());
      }
    });
    return library;
  }

  @Override
  public String toString() {
    return "MappingSpecification [infoModel=" + infoModel + "]";
  }

}
