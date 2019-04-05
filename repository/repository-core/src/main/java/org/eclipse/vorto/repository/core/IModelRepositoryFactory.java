package org.eclipse.vorto.repository.core;

import org.springframework.security.core.Authentication;

public interface IModelRepositoryFactory {
  
  IModelPolicyManager getPolicyManager(String tenant, Authentication user);
  
  IModelPolicyManager getPolicyManager(IUserContext userContext);
  
  IModelRepository getRepository(String tenant, Authentication user);
  
  IModelRepository getRepository(IUserContext userContext);
  
  IModelRepository getRepository(String tenant);

  IModelRetrievalService getModelRetrievalService(Authentication user);
  
  IModelRetrievalService getModelRetrievalService(IUserContext userContext);
  
  IModelRetrievalService getModelRetrievalService();

  IModelSearchService getModelSearchService(Authentication user);
  
  IModelSearchService getModelSearchService(IUserContext userContext);
  
  IModelSearchService getModelSearchService();
}
