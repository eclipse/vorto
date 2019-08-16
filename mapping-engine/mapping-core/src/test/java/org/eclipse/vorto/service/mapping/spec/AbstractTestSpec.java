/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.service.mapping.spec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.mapping.engine.model.spec.Reference;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.IModel;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelId;

public abstract class AbstractTestSpec implements IMappingSpecification {

  protected Infomodel infomodel =
      new Infomodel(ModelId.fromPrettyFormat("devices:AWSIoTButton:1.0.0"));
  
  private List<Reference> references = new ArrayList<>();

  public AbstractTestSpec() {
    createModel();
  }

  @Override
  public Infomodel getInfoModel() {
    return infomodel;
  }

  protected abstract void createModel();
  
  protected void addReference(Reference reference) {
    this.references.add(reference);
  }

  @Override
  public FunctionblockModel getFunctionBlock(String name) {
    return (FunctionblockModel) getReferencedModel(infomodel.getId(), name).get();
  }

  public Optional<IModel> getReferencedModel(ModelId parent, String propertyName) {
    Optional<Reference> reference = this.references.stream().filter(r -> r.getParent().equals(parent) && r.getPropertyName().equals(propertyName)).findFirst();
    if (reference.isPresent()) {
      return Optional.of(reference.get().getType());
    } else {
      return Optional.empty();
    }
  }

  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider provider) {
    return new FunctionLibrary();
  }
}
