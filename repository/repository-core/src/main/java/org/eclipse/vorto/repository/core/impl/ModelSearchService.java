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
