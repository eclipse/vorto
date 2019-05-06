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
package org.eclipse.vorto.repository.core.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelSearchService;
import org.eclipse.vorto.repository.core.ModelInfo;

public class ModelSearchService implements IModelSearchService {

  private Supplier<Collection<String>> tenantsSupplier;
  private Function<String, IModelRepository> modelRepoSource;
  
  public ModelSearchService(Supplier<Collection<String>> tenantsSupplier,
      Function<String, IModelRepository> modelRepoSource) {
    this.tenantsSupplier = tenantsSupplier;
    this.modelRepoSource = modelRepoSource;
  }

  @Override
  public Map<String, List<ModelInfo>> search(String expression) {
    Map<String, List<ModelInfo>> modelResourcesMap = new HashMap<String, List<ModelInfo>>();
    for (String tenant : tenantsSupplier.get()) {
      IModelRepository modelRepo = modelRepoSource.apply(tenant);
      modelResourcesMap.put(tenant, modelRepo.search(expression));
    }

    return modelResourcesMap;
  }

}
