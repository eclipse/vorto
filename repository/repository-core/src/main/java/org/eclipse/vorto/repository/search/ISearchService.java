package org.eclipse.vorto.repository.search;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.repository.core.ModelInfo;

public interface ISearchService {

  /**
   * Searches the index given the owning tenants, and a search expression
   * 
   * @param tenantIds the tenants whose models we should search. If tenantIds is empty, it searches
   *        all tenants. If tenantIds is not empty but the collection inside is empty, it searches
   *        the public models of all tenants. If tenantIds is not empty and the collection inside is
   *        non-empty, it searches all models inside all tenants in tenantIds plus all the public
   *        models of all tenants.
   * @param expression The search expression
   * @return a list of models in the index
   */
  List<ModelInfo> search(Optional<Collection<String>> tenantIds, String expression);
  
  /**
   *  Searches the repository with the given search expression
   * @param expression
   * @return
   */
  List<ModelInfo> search(String expression);
}
