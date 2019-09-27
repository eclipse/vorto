package org.eclipse.vorto.repository.search;

import java.util.List;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;

public interface ISearchService {

  /**
   * Searches all public models or models for which the calling user is member/collaborator of.
   * 
   * @param fulltextexpression The search expression
   * @return models matching the search expression
   */
  List<ModelInfo> search(String expression);
  
  /**
   * Searches all public models or models for which the calling user is member/collaborator of, but uses the
   * user context of @userContext
   * @param searchExpression The search expression
   * @param userContext The user context with which to execute this query
   * @return
   */
  List<ModelInfo> search(String searchExpression, IUserContext userContext);
}
