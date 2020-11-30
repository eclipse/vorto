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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.IDiagnostics;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IModelRetrievalService;
import org.eclipse.vorto.repository.core.IModeshapeDoctor;
import org.eclipse.vorto.repository.core.IRepositoryManager;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.UserLoginException;
import org.eclipse.vorto.repository.core.impl.cache.UserRolesRequestCache;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.diagnostics.ModeshapeDoctor;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.PrivilegeService;
import org.eclipse.vorto.repository.services.RoleService;
import org.eclipse.vorto.repository.services.RoleUtil;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.UserRepositoryRoleService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
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
public class ModelRepositoryFactory implements IModelRepositoryFactory,
    ApplicationEventPublisherAware {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModelRepositoryFactory.class);

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

  @Autowired
  private UserRepositoryRoleService userRepositoryRoleService;

  @Autowired
  private PrivilegeService privilegeService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private RoleUtil roleUtil;

  @Autowired
  private IOAuthProviderRegistry registry;

  private ApplicationEventPublisher eventPublisher = null;

  private Repository repository;

  private static final ModeShapeEngine ENGINE = new ModeShapeEngine();

  private final Supplier<Collection<String>> allWorkspaceIdSupplier = () -> namespaceService
      .findAllWorkspaceIds();

  private final Supplier<Collection<String>> visibleWorkspaceIdSupplier =
      () -> {
        try {
          return namespaceService
              .findWorkspaceIdsOfPossibleReferences();
        } catch (DoesNotExistException dnee) {
          LOGGER.warn("No workspaces found", dnee);
          return Collections.emptyList();
        }
      };

  public ModelRepositoryFactory() {
  }

  public ModelRepositoryFactory(
      ModelSearchUtil modelSearchUtil,
      AttachmentValidator attachmentValidator,
      ModelParserFactory modelParserFactory,
      RepositoryDiagnostics repoDiagnostics,
      RepositoryConfiguration repositoryConfiguration,
      RequestRepositorySessionHelper sessionHelper,
      NamespaceService namespaceService,
      UserNamespaceRoleService userNamespaceRoleService,
      UserRepositoryRoleService userRepositoryRoleService,
      PrivilegeService privilegeService,
      RoleService roleService,
      RoleUtil roleUtil,
      IOAuthProviderRegistry registry
  ) {
    this.modelSearchUtil = modelSearchUtil;
    this.attachmentValidator = attachmentValidator;
    this.modelParserFactory = modelParserFactory;
    this.repoDiagnostics = repoDiagnostics;
    this.repositoryConfiguration = repositoryConfiguration;
    this.sessionHelper = sessionHelper;
    this.namespaceService = namespaceService;
    this.userNamespaceRoleService = userNamespaceRoleService;
    this.userRepositoryRoleService = userRepositoryRoleService;
    this.privilegeService = privilegeService;
    this.roleService = roleService;
    this.roleUtil = roleUtil;
    this.registry = registry;
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
  public IModelRetrievalService getModelRetrievalService() {
    return getModelRetrievalService((Authentication)null);
  }

  @Deprecated
  @Override
  public IModelRetrievalService getModelRetrievalService(IUserContext context) {
    return getModelRetrievalService(context.getAuthentication());
  }

  @Override
  public IModelRetrievalService getModelRetrievalService(Authentication auth) {
    final Authentication authentication;
    if (Objects.isNull(auth)) {
      authentication = SecurityContextHolder.getContext().getAuthentication();
    }
    else {
      authentication = auth;
    }
    UserDto user = UserDto.of(
        authentication.getName(),
        registry.getByAuthentication(authentication).getId()
    );
    return new ModelRetrievalService(getMatchingWorkspaceIdSupplier(user),
        workspaceId -> getRepository(workspaceId, authentication));
  }

  @Override
  public IModelRetrievalService getModelRetrievalServiceWithoutSessionHelper() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDto user = UserDto.of(
        authentication.getName(),
        registry.getByAuthentication(authentication).getId()
    );
    return new ModelRetrievalService(getMatchingWorkspaceIdSupplier(user),
        workspaceId -> getRepositoryWithoutSessionHelper(workspaceId));
  }

  @Override
  public IDiagnostics getDiagnosticsService(String workspaceID, Authentication auth) {
    Diagnostician diagnostics = new Diagnostician(repoDiagnostics);

    diagnostics
        .setRepositorySessionHelperSupplier(namedWorkspaceSessionSupplier(workspaceID, auth));
    return diagnostics;
  }

  @Override
  public IDiagnostics getDiagnosticsService(String workspaceID) {
    return getDiagnosticsService(workspaceID, null);
  }

  @Override
  public IRepositoryManager getRepositoryManager(String workspaceId) {
    RepositoryManager repoManager = new RepositoryManager();
    repoManager
        .setRepositorySessionHelperSupplier(namedWorkspaceSessionSupplier(workspaceId, null));
    repoManager.setDefaultSessionSupplier(defaultWorkspaceSessionSupplier());
    return repoManager;
  }

  @Override
  public IModeshapeDoctor getModeshapeDoctor(String workspaceId) {
    ModeshapeDoctor doctor = new ModeshapeDoctor();
    doctor.setRepositorySessionHelperSupplier(namedWorkspaceSessionSupplier(workspaceId, null));
    return doctor;
  }

  @Override
  public IModelPolicyManager getPolicyManager(String workspaceId) {
    return getPolicyManager(workspaceId, null);
  }

  @Deprecated
  @Override
  public IModelPolicyManager getPolicyManager(IUserContext context) {
    return getPolicyManager(context.getWorkspaceId(), context.getAuthentication());
  }

  @Override
  public IModelPolicyManager getPolicyManager(String workspaceId, Authentication auth) {
    ModelPolicyManager policyManager = new ModelPolicyManager(
        userNamespaceRoleService, roleUtil, this, roleService, namespaceService,
        registry
    );
    policyManager
        .setRepositorySessionHelperSupplier(namedWorkspaceSessionSupplier(workspaceId, auth));
    return policyManager;
  }

  @Override
  public IModelRepository getRepository(String workspaceId) {
    return getRepository(workspaceId, (Authentication)null);
  }

  @Override
  public IModelRepository getRepository(String workspaceID, IUserContext userContext) {
    return getRepository(workspaceID, userContext.getAuthentication());
  }

  @Deprecated
  @Override
  public IModelRepository getRepository(IUserContext userContext) {
    return getRepository(userContext.getWorkspaceId(), userContext.getAuthentication());
  }

  @Override
  public IModelRepository getRepository(String workspaceId, Authentication auth) {
    final Authentication authentication;
    if (Objects.isNull(auth)) {
      authentication = SecurityContextHolder.getContext().getAuthentication();
    }
    else {
      authentication = auth;
    }
    ModelRepository modelRepository = new ModelRepository(
        this.modelSearchUtil,
        this.attachmentValidator,
        this.modelParserFactory,
        getModelRetrievalService(authentication),
        this,
        getPolicyManager(workspaceId, authentication),
        namespaceService,
        privilegeService);

    modelRepository
        .setRepositorySessionHelperSupplier(namedWorkspaceSessionSupplier(workspaceId, authentication));
    modelRepository.setApplicationEventPublisher(eventPublisher);
    return modelRepository;
  }

  @Override
  public IModelRepository getRepositoryByNamespace(String namespace) {
    return getRepositoryByNamespace(namespace,
        SecurityContextHolder.getContext().getAuthentication());
  }

  @Override
  public IModelRepository getRepositoryByNamespaceEscalated(String namespace) {
    return getRepositoryByNamespace(namespace,
        PrivilegedUserContextProvider.systemAdminContext().getAuthentication()
    );
  }

  @Override
  public IModelRepository getRepositoryByModel(ModelId modelId) {
    IModelRepository foundRepository = getRepositoryByNamespace(modelId.getNamespace());
    if (foundRepository == null) {
      throw new ModelNotFoundException(
          String.format("Namespace [%s] does not exist.", modelId.getNamespace())
      );
    } else {
      return foundRepository;
    }
  }

  @Override
  public IModelRepository getRepositoryByModelEscalated(ModelId modelId) {
    IModelRepository foundRepository = getRepositoryByNamespaceEscalated(modelId.getNamespace());
    if (foundRepository == null) {
      throw new ModelNotFoundException(
          String.format("Namespace [%s] does not exist.", modelId.getNamespace())
      );
    } else {
      return foundRepository;
    }
  }

  @Override
  public IModelRepository getRepositoryByModelWithoutSessionHelper(ModelId modelId) {
    return getRepositoryByModelWithoutSessionHelper(modelId,
        SecurityContextHolder.getContext().getAuthentication());
  }

  /**
   *
   * @param workspaceId
   * @param auth if {@code null}, will be inferred by querying the {@link SecurityContextHolder}
   * @return
   */
  private Supplier<RequestRepositorySessionHelper> namedWorkspaceSessionSupplier(String workspaceId,
      Authentication auth) {
    final Authentication authentication;
    if (Objects.isNull(auth)) {
      authentication = SecurityContextHolder.getContext().getAuthentication();
    }
    else {
      authentication = auth;
    }
    return () -> {

      if (sessionHelper == null) {
        sessionHelper = new RequestRepositorySessionHelper(false, privilegeService);
      }
      sessionHelper.setRepository(repository);
      sessionHelper.setWorkspaceId(workspaceId);
      UserDto user = UserDto.of(
          authentication.getName(),
          registry.getByAuthentication(authentication).getId()
      );
      sessionHelper.setUserRoles(
          getUserRoles(workspaceId, user)
      );
      sessionHelper.setAuthentication(authentication);
      return sessionHelper;
    };
  }

  private Supplier<Session> defaultWorkspaceSessionSupplier() {

    return () -> {
      try {
        return repository.login();
      } catch (LoginException e) {
        throw new UserLoginException("[redacted]", e);
      } catch (RepositoryException e) {
        throw new FatalModelRepositoryException(
            "Error while getting default repository for user", e);
      }
    };
  }

  /**
   * This method accesses a request-scoped {@link UserRolesRequestCache} bean that maps composite
   * workspace+user IDs to roles. <br/>
   * This implies values for multiple calls with same workspace ID and username are cached within
   * the current request, and expire in the next one. <br/>
   * Not a perfect system as there are also numerous individual ID calls per request at times,
   * but better than nothing (i.e. at least caches the large amount of repeated calls within e.g. a
   * request to load a model).
   *
   * @param workspaceId
   * @param targetUser
   * @return
   */
  private Collection<IRole> getUserRoles(String workspaceId, UserDto targetUser) {

    if (UserContext.isAnonymous(targetUser.getUsername())) {
      return new HashSet<>();
    }
    try {
      return userNamespaceRoleService.getRolesByUserAndWorkspaceId(targetUser, workspaceId);
    } catch (DoesNotExistException dnee) {
      LOGGER.debug("User or namespace not found. ", dnee);
      return Collections.emptySet();
    }
  }

  private IModelRepository getRepositoryByNamespace(String namespace, Authentication auth) {
    return namespaceService.resolveWorkspaceIdForNamespace(namespace)
        .map(workspaceId -> getRepository(workspaceId, auth))
        .orElse(null);
  }

  private IModelRepository getRepositoryByNamespaceWithoutSessionHelper(String namespace,
      Authentication auth) {
    return namespaceService.resolveWorkspaceIdForNamespace(namespace)
        .map(workspaceId -> getRepositoryWithoutSessionHelper(workspaceId))
        .orElse(null);
  }

  private IModelRepository getRepositoryByModelWithoutSessionHelper(ModelId modelId,
      Authentication auth) {
    IModelRepository foundRepository =
        getRepositoryByNamespaceWithoutSessionHelper(modelId.getNamespace(), auth);
    if (foundRepository == null) {
      throw new ModelNotFoundException(
          "Namespace " + modelId.getNamespace() + " does not exist in the system.");
    } else {
      return foundRepository;
    }
  }

  private Supplier<Collection<String>> getMatchingWorkspaceIdSupplier(UserDto user) {
    if (!UserContext.isAnonymous(user.getUsername()) && userRepositoryRoleService.isSysadmin(user)) {
      return allWorkspaceIdSupplier;
    }
    return visibleWorkspaceIdSupplier;
  }

  protected IModelRepository getRepositoryWithoutSessionHelper(String workspaceId) {
    ModelRepository modelRepository = new ModelRepository(
        this.modelSearchUtil,
        this.attachmentValidator,
        this.modelParserFactory,
        getModelRetrievalServiceWithoutSessionHelper(),
        this,
        getPolicyManager(workspaceId),
        namespaceService,
        privilegeService);

    modelRepository.setRepositorySessionHelperSupplier(() -> {
      RequestRepositorySessionHelper s =
          new RequestRepositorySessionHelper(false, privilegeService);
      s.setRepository(repository);
      s.setWorkspaceId(workspaceId);
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserDto user = UserDto.of(
        authentication.getName(),
        registry.getByAuthentication(authentication).getId()
      );
      s.setUserRoles(
          getUserRoles(workspaceId, user)
      );
      s.setAuthentication(authentication);
      return s;
    });
    modelRepository.setApplicationEventPublisher(eventPublisher);
    return modelRepository;
  }
}
