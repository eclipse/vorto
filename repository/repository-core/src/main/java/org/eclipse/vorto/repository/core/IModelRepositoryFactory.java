/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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

  IDiagnostics getDiagnosticsService(String workspaceID);

  IDiagnostics getDiagnosticsService(String workspaceID, Authentication auth);

  IModelPolicyManager getPolicyManager(String workspaceID);

  IModelPolicyManager getPolicyManager(String workspaceID, Authentication authentication);

  @Deprecated
  IModelPolicyManager getPolicyManager(IUserContext context);

  IModelRepository getRepository(String workspaceID);

  IModelRepository getRepositoryByNamespace(String namespace);

  IModelRepository getRepositoryByNamespaceEscalated(String namespace);

  IModelRepository getRepositoryByModel(ModelId modelId);

  IModelRepository getRepositoryByModelEscalated(ModelId modelId);

  IModelRepository getRepositoryByModelWithoutSessionHelper(ModelId modelId);

  IModelRepository getRepository(String workspaceId, Authentication authentication);

  IModelRepository getRepository(String workspaceId, IUserContext context);

  @Deprecated
  IModelRepository getRepository(IUserContext userContext);

  IModelRetrievalService getModelRetrievalService();

  IModelRetrievalService getModelRetrievalService(Authentication authentication);

  @Deprecated
  IModelRetrievalService getModelRetrievalService(IUserContext context);

  /**
   * The SessionHelper is scoped to the request and to the instance of the IModelRepositoryFactory.
   * In a multi-threaded context this will not work and cause unpredictable and failing behavior,
   * therefore this method uses individual SessionHelpers instead. This method should only be used
   * in a multi-threaded context of a single request.
   */
  IModelRetrievalService getModelRetrievalServiceWithoutSessionHelper();

  IRepositoryManager getRepositoryManager(String workspaceID);

  IModeshapeDoctor getModeshapeDoctor(String workspaceID);

}
