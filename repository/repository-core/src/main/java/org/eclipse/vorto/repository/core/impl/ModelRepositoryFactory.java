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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.IDiagnostics;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IModelRetrievalService;
import org.eclipse.vorto.repository.core.IRepositoryManager;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.TenantNotFoundException;
import org.eclipse.vorto.repository.core.UserLoginException;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.core.security.SpringSecurityCredentials;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.tenant.TenantService;
import org.infinispan.schematic.document.Changes;
import org.infinispan.schematic.document.EditableDocument;
import org.infinispan.schematic.document.Editor;
import org.modeshape.common.collection.Problems;
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
  private TenantService tenantService;
  
  private ApplicationEventPublisher eventPublisher = null;

  private Repository repository;

  private static final ModeShapeEngine ENGINE = new ModeShapeEngine();
  
  private Supplier<Collection<String>> tenantsSupplier = () -> {
    return tenantService.getTenants().stream().map(Tenant::getTenantId)
        .collect(Collectors.toList());
  };

  public ModelRepositoryFactory() {}
  
  public ModelRepositoryFactory(IUserAccountService userAccountService,
      ModelSearchUtil modelSearchUtil,
      AttachmentValidator attachmentValidator,
      ModelParserFactory modelParserFactory,
      RepositoryDiagnostics repoDiagnostics,
      RepositoryConfiguration repoConfig, 
      TenantService tenantService) {
    this.userAccountService = userAccountService;
    this.modelSearchUtil = modelSearchUtil;
    this.attachmentValidator = attachmentValidator;
    this.modelParserFactory = modelParserFactory;
    this.repoDiagnostics = repoDiagnostics;
    this.repositoryConfiguration = repoConfig;
    this.tenantService = tenantService;
  }

  @PostConstruct
  public void start() throws Exception {
    LOGGER.info("Starting Vorto Modeshape Repository -start-");
    ENGINE.start();
    repository = ENGINE.deploy(modifyWorkspaces(repositoryConfiguration));
    ENGINE.startRepository(repositoryConfiguration.getName()).get();
    LOGGER.info("Starting Vorto Modeshape Repository -finished-");
  }

  @PreDestroy
  public void stop() throws Exception {
    try {
      ENGINE.shutdown().get(10, TimeUnit.SECONDS);
    } catch (Exception e) {
      LOGGER.error("Error while waiting for the ModeShape engine to shutdown", e);
    }
  }
  
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }
  
  @Override
  public IModelRetrievalService getModelRetrievalService(Authentication user) {
    return new ModelRetrievalService(tenantsSupplier, (tenant) -> {
      return getRepository(tenant, user);
    });
  }
  
  @Override
  public IModelRetrievalService getModelRetrievalService(IUserContext userContext) {
    return new ModelRetrievalService(tenantsSupplier, (tenant) -> {
      return getRepository(tenant, userContext.getAuthentication());
    });
  }
  
  @Override
  public IModelRetrievalService getModelRetrievalService() {
    return new ModelRetrievalService(tenantsSupplier, (tenant) -> {
      return getRepository(tenant, SecurityContextHolder.getContext().getAuthentication());
    });
  }
  
  @Override
  public IDiagnostics getDiagnosticsService(String tenant, Authentication user) {
    Diagnostician diagnostics = new Diagnostician(repoDiagnostics);
    diagnostics.setSessionSupplier(repositorySessionSupplier(tenant, user));
    return diagnostics;
  }

  @Override
  public IRepositoryManager getRepositoryManager(String tenant, Authentication user) {
    RepositoryManager repoManager = new RepositoryManager();
    repoManager.setSessionSupplier(repositorySessionSupplier(tenant, user));
    return repoManager;
  }
  
  @Override
  public IModelPolicyManager getPolicyManager(String tenant, Authentication user) {
    ModelPolicyManager policyManager = new ModelPolicyManager(userAccountService);
    policyManager.setSessionSupplier(repositorySessionSupplier(tenant, user));
    return policyManager;
  }

  @Override
  public IModelPolicyManager getPolicyManager(IUserContext userContext) {
    return getPolicyManager(userContext.getTenant(), userContext.getAuthentication());
  }

  @Override
  public IModelRepository getRepository(String tenant, Authentication user) {
    ModelRepository modelRepository = new ModelRepository(this.modelSearchUtil,
        this.attachmentValidator, this.modelParserFactory, getModelRetrievalService(user),this);

    modelRepository.setSessionSupplier(repositorySessionSupplier(tenant, user));
    modelRepository.setApplicationEventPublisher(eventPublisher);
    
    return modelRepository;
  }

  @Override
  public IModelRepository getRepository(IUserContext userContext) {
    return getRepository(userContext.getTenant(), userContext.getAuthentication());
  }
  
  @Override
  public IModelRepository getRepository(String tenantId) {
    return getRepository(tenantId, SecurityContextHolder.getContext().getAuthentication());
  }
  
  private Supplier<Session> repositorySessionSupplier(String tenant, Authentication user) {
    return () -> {
      try {
        return repository.login(
            new SpringSecurityCredentials(user, getUserRolesInTenant(tenant, user.getName())),
            tenant);
      } catch (LoginException e) {
        throw new UserLoginException(user.getName(), e);
      } catch (NoSuchWorkspaceException e) {
        throw new TenantNotFoundException(tenant, e);
      } catch (RepositoryException e) {
        throw new FatalModelRepositoryException("Error while getting repository given tenant ["
            + tenant + "] and user [" + user.getName() + "]", e);
      }
    };
  }

  private Set<Role> getUserRolesInTenant(String tenantId, String username) {
    Tenant tenant = tenantService.getTenant(tenantId).orElseThrow(
        () -> new IllegalArgumentException("tenantId '" + tenantId + "' doesn't exist!"));
    
    return tenant
        .getUser(username).map(
            tUser -> tUser.getRoles().stream().map(
                userRole -> userRole.getRole()).collect(Collectors.toSet()))
        .orElse(Collections.emptySet());
  }

  private RepositoryConfiguration modifyWorkspaces(RepositoryConfiguration repoConfig) {
    Editor editor = repoConfig.edit();
    EditableDocument workspaces =
        editor.getOrCreateDocument(RepositoryConfiguration.FieldName.WORKSPACES);

    Collection<String> tenants = new ArrayList<>();
    tenants.add("default");
    
    try {
      Collection<String> currentTenants = getTenants();
      tenants.addAll(currentTenants);
    } catch (Exception e) {
      LOGGER.error(
          "Error while retrieving tenants during modeshape initialization. Using 'default' as workspace.",
          e);
    }
    
    workspaces.setArray(RepositoryConfiguration.FieldName.PREDEFINED, tenants.toArray());
    
    return new RepositoryConfiguration(editor, repoConfig.getName());
  }

  public void updateWorkspaces() {
    try {
      LOGGER.info("Updating the repo workspaces with current tenants! Engine State: "
          + ENGINE.getState().name());
      RepositoryConfiguration deployedConfig =
          ENGINE.getRepositoryConfiguration(repositoryConfiguration.getName());

      Editor editor = deployedConfig.edit();
      EditableDocument workspaces =
          editor.getOrCreateDocument(RepositoryConfiguration.FieldName.WORKSPACES);
      workspaces.setArray(RepositoryConfiguration.FieldName.PREDEFINED, getTenants().toArray());

      Changes changes = editor.getChanges();
      Problems problems = deployedConfig.validate(changes);
      if (!problems.hasErrors()) {
        ENGINE.update(repositoryConfiguration.getName(), changes);
      } else {
        LOGGER.error(problems.toString());
      }
    } catch (RepositoryException e) {
      LOGGER.error("Error while updating workspace", e);
    }
  }

  private Collection<String> getTenants() {
    Collection<String> tenantWorkspaces = tenantService.getTenants().stream()
        .map(tenant -> tenant.getTenantId()).collect(Collectors.toList());
    return tenantWorkspaces;
  }

  @Override
  public IModelRepository getRepositoryByNamespace(String namespace) {
    Optional<Tenant> tenant = this.tenantService.getTenantFromNamespace(namespace);
    if (tenant.isPresent()) {
      return this.getRepository(tenant.get().getTenantId());
    }
    return null;
  }

  @Override
  public IModelRepository getRepositoryByModel(ModelId modelId) {
    return getRepositoryByNamespace(modelId.getNamespace());
  }
}
