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
package org.eclipse.vorto.repository.core.impl;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.*;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component("modelRepositoryFactory")
public class ModelRepositoryFactory implements IModelRepositoryFactory, ApplicationEventPublisherAware {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModelRepositoryFactory.class);

  @Autowired
  private IUserAccountService userAccountService;

  @Autowired
  private ModelSearchUtil modelSearchUtil;

  @Autowired
  private AttachmentValidator attachmentValidator;

  @Autowired
  private ModelParserFactory modelParserFactory;

  @Autowired
  private RepositoryDiagnostics repoDiagnostics;

  @Autowired
  private RepositoryConfiguration repositoryConfiguration;

  @Autowired
  private RequestRepositorySessionHelper sessionHelper;

  @Autowired
  private NamespaceService namespaceService;

  @Autowired
  private UserNamespaceRoleService userNamespaceRoleService;

  private ApplicationEventPublisher eventPublisher = null;

  private Repository repository;

  private static final ModeShapeEngine ENGINE = new ModeShapeEngine();
  
  private final Supplier<Collection<String>> workspaceIdSupplier = () -> namespaceService.findAllWorkspaceIds();

  public ModelRepositoryFactory() {}

  public ModelRepositoryFactory(IUserAccountService userAccountService,
      ModelSearchUtil modelSearchUtil,
      AttachmentValidator attachmentValidator,
      ModelParserFactory modelParserFactory,
      RepositoryDiagnostics repoDiagnostics,
      RepositoryConfiguration repoConfig,
      RequestRepositorySessionHelper sessionHelper,
      NamespaceService namespaceService,
      UserNamespaceRoleService userNamespaceRoleService) {
    this.userAccountService = userAccountService;
    this.modelSearchUtil = modelSearchUtil;
    this.attachmentValidator = attachmentValidator;
    this.modelParserFactory = modelParserFactory;
    this.repoDiagnostics = repoDiagnostics;
    this.repositoryConfiguration = repoConfig;
    this.namespaceService = namespaceService;
    this.sessionHelper = sessionHelper;
    this.userNamespaceRoleService = userNamespaceRoleService;
  }

  @PostConstruct
  public void start() throws Exception {
    LOGGER.info("Starting Vorto Modeshape Repository -start-");
    ENGINE.start();
    repository = ENGINE.deploy(repositoryConfiguration);
    ENGINE.startRepository(repositoryConfiguration.getName()).get();
    LOGGER.info("Starting Vorto Modeshape Repository -finished-");
  }

  @PreDestroy
  public void stop() {
    try {
      ENGINE.shutdown().get(10, TimeUnit.SECONDS);
    } catch (Exception e) {
      LOGGER.error("Error while waiting for the ModeShape engine to shutdown", e);
    }
  }
  
  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }
  
  @Override
  public IModelRetrievalService getModelRetrievalService(Authentication user) {
    return new ModelRetrievalService(workspaceIdSupplier, workspaceId -> getRepository(workspaceId, user));
  }
  
  @Override
  public IModelRetrievalService getModelRetrievalService(IUserContext userContext) {
    return new ModelRetrievalService(workspaceIdSupplier, workspaceId -> getRepository(workspaceId, userContext.getAuthentication()));
  }
  
  @Override
  public IModelRetrievalService getModelRetrievalService() {
    return new ModelRetrievalService(workspaceIdSupplier,
            workspaceId -> getRepository(workspaceId, SecurityContextHolder.getContext().getAuthentication()));
  }
  
  @Override
  public IDiagnostics getDiagnosticsService(String workspaceId, Authentication user) {
    Diagnostician diagnostics = new Diagnostician(repoDiagnostics);
    diagnostics.setRepositorySessionHelperSupplier(namedWorkspaceSessionSupplier(workspaceId, user));
    return diagnostics;
  }

  @Override
  public IRepositoryManager getRepositoryManager(String workspaceId, Authentication user) {
    RepositoryManager repoManager = new RepositoryManager();
    repoManager.setRepositorySessionHelperSupplier(namedWorkspaceSessionSupplier(workspaceId, user));
    repoManager.setDefaultSessionSupplier(defaultWorkspaceSessionSupplier(user));
    return repoManager;
  }

  @Override
  public IModelPolicyManager getPolicyManager(String workspaceId, Authentication user) {
    ModelPolicyManager policyManager = new ModelPolicyManager(userAccountService, this);
    policyManager.setRepositorySessionHelperSupplier(namedWorkspaceSessionSupplier(workspaceId, user));
    return policyManager;
  }

  @Override
  public IModelPolicyManager getPolicyManager(IUserContext userContext) {
    return getPolicyManager(userContext.getTenant(), userContext.getAuthentication());
  }

  @Override
  public IModelRepository getRepository(String workspaceId, Authentication user) {
    ModelRepository modelRepository = new ModelRepository(this.modelSearchUtil,
        this.attachmentValidator,
        this.modelParserFactory,
        getModelRetrievalService(user),
        this,
        getPolicyManager(workspaceId, user),
        namespaceService);

    modelRepository.setRepositorySessionHelperSupplier(namedWorkspaceSessionSupplier(workspaceId, user));
    modelRepository.setApplicationEventPublisher(eventPublisher);
    return modelRepository;
  }

  @Override
  public IModelRepository getRepository(IUserContext userContext) {
    return getRepository(userContext.getTenant(), userContext.getAuthentication());
  }
  
  @Override
  public IModelRepository getRepository(String workspaceId) {
    return getRepository(workspaceId, SecurityContextHolder.getContext().getAuthentication());
  }
  
  @Override
  public IModelRepository getRepositoryByNamespace(String namespace) {
    return getRepositoryByNamespace(namespace, SecurityContextHolder.getContext().getAuthentication());
  }
  
  @Override
  public IModelRepository getRepositoryByModel(ModelId modelId) {
    return getRepositoryByModel(modelId, SecurityContextHolder.getContext().getAuthentication());
  }
  
  @Override
  public IModelRepository getRepositoryByModel(ModelId modelId, IUserContext userContext) {
    return getRepositoryByModel(modelId, userContext.getAuthentication());
  }

  private Supplier<RequestRepositorySessionHelper> namedWorkspaceSessionSupplier(String workspaceId, Authentication user) {
    return () -> {
      if(sessionHelper == null)
        sessionHelper = new RequestRepositorySessionHelper(false);
      sessionHelper.setRepository(repository);
      sessionHelper.setWorkspaceId(workspaceId);
      sessionHelper.setRolesInNamespace(getUserRolesInNamespace(workspaceId, user.getName()));
      sessionHelper.setUser(user);
      return sessionHelper;
    };
  }

  private Supplier<Session> defaultWorkspaceSessionSupplier(Authentication user) {
    return () -> {
      try {
        return repository.login();
      } catch (LoginException e) {
        throw new UserLoginException(user.getName(), e);
      } catch (RepositoryException e) {
        throw new FatalModelRepositoryException("Error while getting default repository for user [" + user.getName() + "]", e);
      }
    };
  }

  private Set<IRole> getUserRolesInNamespace(String workspaceId, String username) {
    // TODO improve caching for non anon users
    if(UserContext.isAnonymous(username))
      return new HashSet<>();

    String namespace = namespaceService.findNamespaceByWorkspaceId(workspaceId).getName();
    try {
      return new HashSet<>(userNamespaceRoleService.getRoles(username, namespace));
    } catch (DoesNotExistException e) {
      LOGGER.debug("User or namespace not found. ", e);
      return Collections.emptySet();
    }
  }

  private IModelRepository getRepositoryByNamespace(String namespace, Authentication auth) {
    return namespaceService.resolveWorkspaceIdForNamespace(namespace)
        .map(workspaceId -> getRepository(workspaceId, auth))
        .orElse(null);
  }

  private IModelRepository getRepositoryByModel(ModelId modelId, Authentication auth) {
    IModelRepository foundRepository =  getRepositoryByNamespace(modelId.getNamespace(), auth);
    if (foundRepository == null) {
      throw new ModelNotFoundException("Namespace " + modelId.getNamespace() + " does not exist in the system.");
    } else {
      return foundRepository;
    }
  }
}
