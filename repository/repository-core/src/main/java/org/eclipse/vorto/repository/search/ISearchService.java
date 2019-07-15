package org.eclipse.vorto.repository.search;

import java.util.List;
import org.eclipse.vorto.repository.core.ModelInfo;

public interface ISearchService {

  /**
   * Searches all public models or models for which the calling user is member/collaborator of.
   * 
   * @param fulltextexpression The search expression
   * @return models matching the search expression
   */
  List<ModelInfo> search(String expression);
}
