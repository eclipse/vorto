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
package org.eclipse.vorto.mapping.engine;

import org.eclipse.vorto.mapping.engine.functions.ClassFunction;
import org.eclipse.vorto.mapping.engine.functions.IFunction;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.internal.DataMapperJxpath;
import org.eclipse.vorto.mapping.engine.internal.functions.CustomFunctionsLibrary;
import org.eclipse.vorto.mapping.engine.internal.functions.Jxpath;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;

public class DataMapperBuilder {

  private IMappingSpecification specification;

  private CustomFunctionsLibrary functionLibrary = CustomFunctionsLibrary.createDefault();



  private static final IFunction FUNC_XPATH = new ClassFunction("xpath", Jxpath.class);

  private IScriptEvalProvider provider = null;

  protected DataMapperBuilder() {
    registerConditionFunction(FUNC_XPATH);
  }

  public DataMapperBuilder registerConverterFunction(IFunction... converters) {
    for (IFunction function : converters) {
      functionLibrary.addConverterFunction(function);
    }
    return this;
  }

  public DataMapperBuilder registerConditionFunction(IFunction... conditions) {
    for (IFunction condition : conditions) {
      functionLibrary.addConditionFunction(condition);
    }
    return this;
  }

  public DataMapperBuilder registerScriptEvalProvider(IScriptEvalProvider provider) {
    this.provider = provider;
    return this;
  }

  public IDataMapper build() {
    this.functionLibrary.addConverterFunctions(specification.getScriptFunctions(this.provider));
    return new DataMapperJxpath(specification, functionLibrary);
  }

  public DataMapperBuilder withSpecification(IMappingSpecification specification) {
    this.specification = specification;
    return this;
  }



}
