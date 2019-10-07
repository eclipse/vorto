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
package org.eclipse.vorto.repository.core.impl.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import com.google.inject.Injector;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class MappingModelParser extends AbstractModelParser {

  private IModelRepositoryFactory modelRepoFactory;
  
  public MappingModelParser(String fileName,IModelRepositoryFactory modelRepoFactory) {
    super(fileName,modelRepoFactory);
    this.modelRepoFactory = modelRepoFactory;
  }

  @Override
  protected Injector getInjector() {
    return new MappingStandaloneSetup().createInjectorAndDoEMFRegistration();
  }
  
  /**
   * Adds inherited types of references
   * Why? Because mapping models can also define mapping rules of Function Blocks / or Entities for inherited properties
   */
  @Override
  protected Collection<ModelId> getReferences(Model model) { 
    List<ModelId> result = new ArrayList<>();
    model.getReferences().stream().map(
        modelRef -> ModelId.fromReference(modelRef.getImportedNamespace(), modelRef.getVersion()))
        .collect(Collectors.toList()).stream().forEach(modelId -> {
          result.add(modelId);
          result.addAll(this.modelRepoFactory.getRepositoryByModel(modelId).getBasicInfo(modelId).getReferences());
        });
    
    return result;
  }
}
