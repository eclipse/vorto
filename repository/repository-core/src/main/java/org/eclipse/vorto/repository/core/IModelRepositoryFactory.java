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
package org.eclipse.vorto.repository.core;

import org.eclipse.vorto.model.ModelId;
import org.springframework.security.core.Authentication;

public interface IModelRepositoryFactory {
  
  IDiagnostics getDiagnosticsService(String tenant, Authentication user);
  
  IModelPolicyManager getPolicyManager(String tenant, Authentication user);
  
  IModelPolicyManager getPolicyManager(IUserContext userContext);
  
  IModelRepository getRepository(String tenant, Authentication user);
  
  IModelRepository getRepository(IUserContext userContext);
  
  IModelRepository getRepository(String tenant);
  
  IModelRepository getRepositoryByNamespace(String namespace);
  
  IModelRepository getRepositoryByModel(ModelId modelId);
  
  IModelRepository getRepositoryByModel(ModelId modelId, IUserContext userContext);

  IModelRetrievalService getModelRetrievalService(Authentication user);
  
  IModelRetrievalService getModelRetrievalService(IUserContext userContext);
  
  IModelRetrievalService getModelRetrievalService();
  
  IRepositoryManager getRepositoryManager(String tenant, Authentication user);
}
