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

import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.*;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.core.PolicyEntry.PrincipalType;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.services.RoleService;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.eclipse.vorto.repository.workflow.ModelState;
import org.eclipse.vorto.repository.workflow.impl.functions.GrantCollaboratorAccessPolicy;
import org.eclipse.vorto.repository.workflow.impl.functions.GrantReviewerModelPolicy;
import org.eclipse.vorto.repository.workflow.impl.functions.GrantRoleAccessPolicy;
import org.eclipse.vorto.repository.workflow.impl.functions.RemoveRoleAccessPolicy;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.modeshape.jcr.security.SimplePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.jcr.AccessDeniedException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.*;
import java.util.*;
import java.util.function.Predicate;

public class ModelPolicyManager extends AbstractRepositoryOperation implements IModelPolicyManager {

  private static final Logger LOGGER = Logger.getLogger(ModelPolicyManager.class);

  private IUserAccountService userAccountService;

  private IModelRepositoryFactory modelRepositoryFactory;

  private RoleService roleService;

  public ModelPolicyManager(
      @Autowired IUserAccountService userAccountService,
      @Autowired IModelRepositoryFactory iModelRepositoryFactory,
      @Autowired RoleService roleService) {
    this.userAccountService = userAccountService;
    this.modelRepositoryFactory = iModelRepositoryFactory;
    this.roleService = roleService;
  }

  @Override
  public Collection<PolicyEntry> getPolicyEntries(ModelId modelId) {
    return doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
        Node nodeToGetPolicies = session.getNode(modelIdHelper.getFullPath());
        AccessControlManager acm = session.getAccessControlManager();
        AccessControlList acl = getAccessControlList(nodeToGetPolicies, acm);
        return convertAccessControlEntriesToPolicyEntries(acl);
      } catch (AccessDeniedException ex) {
        throw new NotAuthorizedException(modelId);
      }
    });
  }

  private List<PolicyEntry> convertAccessControlEntriesToPolicyEntries(AccessControlList acl) throws RepositoryException {
    List<PolicyEntry> policyEntries = new ArrayList<>();
    for (AccessControlEntry entry : acl.getAccessControlEntries()) {
      PolicyEntry policy = PolicyEntry.of(entry);
      if (!policy.isAdminPolicy()) {
        policyEntries.add(policy);
      }
    }
    return policyEntries;
  }

  @Override
  public void addPolicyEntry(ModelId modelId, PolicyEntry... newEntries) {
    doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
        Node nodeToAddPolicy = session.getNode(modelIdHelper.getFullPath());
        AccessControlManager acm = session.getAccessControlManager();
        AccessControlList acl = getAccessControlList(nodeToAddPolicy, acm);

        final AccessControlList _acl = acl;

        // put all existing ACE that are in newEntries to existingEntries
        List<AccessControlEntry> existingEntries = putAllExistingACEFromNewEntriesToExistingEntries(acl, newEntries);

        // remove all existingEntries, entries that are in newEntries
        removeAllExistingEntries(_acl, existingEntries);

        // create ACE for every entry in newEntries
        createAceForEveryEntryInNewEntries(acm, _acl, newEntries);

        acm.setPolicy(nodeToAddPolicy.getPath(), _acl);
        session.save();
        return null;
      } catch (AccessDeniedException ex) {
        throw new NotAuthorizedException(modelId);
      }
    });
  }

  @Override
  public void removePolicyEntry(ModelId modelId, PolicyEntry entryToRemove) {
    entryToRemove.setPermission(null);
    this.addPolicyEntry(modelId, entryToRemove);

    if (this.getPolicyEntries(modelId).isEmpty()) {
      doInSession(session -> {
        try {
          ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
          Node nodeToRemovePolicy = session.getNode(modelIdHelper.getFullPath());
          AccessControlManager acm = session.getAccessControlManager();
          AccessControlList acl = getAccessControlList(nodeToRemovePolicy, acm);

          acm.removePolicy(nodeToRemovePolicy.getPath(), acl);
          session.save();

          return null;
        } catch (AccessDeniedException ex) {
          throw new NotAuthorizedException(modelId);
        }
      });
    }
  }

  @Override
  public boolean hasPermission(final ModelId modelId, final Permission permission) {
    return doInSession(session -> this.getPolicyEntries(modelId).stream()
          .anyMatch(userFilter(session).and(p -> hasPermission(p.getPermission(), permission))));
  }

  @Override
  public void copyPolicyEntries(ModelId sourceModelId, ModelId targetModelId) {
   Collection<PolicyEntry> entries = getPolicyEntries(sourceModelId);
   addPolicyEntry(targetModelId,entries.toArray(new PolicyEntry[entries.size()]));
  }

  @Override
  public void restorePolicyEntries() {
    doInSession(session -> {
      restorePolicyEntriesInternal(session);
      return null;
    });
  }

  private Predicate<PolicyEntry> userFilter(Session session) {
    return p -> {
      if (p.getPrincipalType() == PrincipalType.User) {
        return p.getPrincipalId().equalsIgnoreCase(session.getUserID());
      } else {
        return userAccountService.hasRole(session.getWorkspace().getName(), session.getUserID(),
            p.getPrincipalId());
      }
    };
  }

  private boolean hasPermission(Permission userPermission, Permission permission) {
    return userPermission.includes(permission);
  }

  private void createAceForEveryEntryInNewEntries(AccessControlManager acm,
      AccessControlList _acl, PolicyEntry[] newEntries) throws RepositoryException {
    for (PolicyEntry newEntry : newEntries) {
      String[] privileges = createPrivileges(newEntry);
      Privilege[] permissions = new Privilege[privileges.length];
      for (int i = 0; i < privileges.length; i++) {
        permissions[i] = acm.privilegeFromName(privileges[i]);
      }
      if (privileges.length > 0) {
        _acl.addAccessControlEntry(SimplePrincipal.newInstance(newEntry.toACEPrincipal()),
            permissions);
      }
    }
  }

  private List<AccessControlEntry> putAllExistingACEFromNewEntriesToExistingEntries(
      AccessControlList acl, PolicyEntry[] newEntries) throws RepositoryException {
    List<AccessControlEntry> existingEntries = new ArrayList<>();
    for (AccessControlEntry ace : acl.getAccessControlEntries()) {
      Arrays.asList(newEntries).forEach(entry -> {
        if (entry.isSame(ace)) {
          existingEntries.add(ace);
        }
      });
    }
    return existingEntries;
  }

  private void removeAllExistingEntries(AccessControlList _acl,
      List<AccessControlEntry> existingEntries) {
    if (!existingEntries.isEmpty()) {
      existingEntries.forEach(ace -> {
        try {
          _acl.removeAccessControlEntry(ace);
        } catch (Exception e) {
          LOGGER.error("Could not grant user readd permissions for model", e);
        }
      });
    }
  }

  private AccessControlList getAccessControlList(Node nodeToAddPolicy, AccessControlManager acm)
      throws RepositoryException {

    AccessControlPolicyIterator it = acm.getApplicablePolicies(nodeToAddPolicy.getPath());
    if (it.hasNext()) {
      return (AccessControlList) it.nextAccessControlPolicy();
    } else {
      return (AccessControlList) acm.getPolicies(nodeToAddPolicy.getPath())[0];
    }
  }

  private String[] createPrivileges(PolicyEntry newEntry) {
    Set<String> result = new HashSet<>();
    if (newEntry.getPermission() == Permission.READ) {
      result.add(Privilege.JCR_READ);
      result.add(Privilege.JCR_READ_ACCESS_CONTROL);
    } else if (newEntry.getPermission() == Permission.MODIFY) {
      result.add(Privilege.JCR_READ);
      result.add(Privilege.JCR_READ_ACCESS_CONTROL);
      result.add(Privilege.JCR_WRITE);
    } else if (newEntry.getPermission() == Permission.FULL_ACCESS) {
      result.add(Privilege.JCR_ALL);
    }

    return result.toArray(new String[result.size()]);
  }

  /**
   * Restores the access policies. (https://github.com/eclipse/vorto/issues/2350)
   *
   * @param session
   * @throws RepositoryException
   */
  private void restorePolicyEntriesInternal(Session session) {
    LOGGER.info("restorePolicyEntries");
    if (this.modelRepositoryFactory == null)
      return;
    IModelRepository repo = this.modelRepositoryFactory.getRepository(session.getWorkspace().getName());
    List<ModelInfo> modelList = repo.search("");

    Map<String, IWorkflowFunction[]> stateToFunctionsMap = createStateToFunctionsMap();

    for (ModelInfo model : modelList) {
      applyWorkflowFunctions(model, session, stateToFunctionsMap);
      setVisibility(model);
    }
  }

  private Map<String, IWorkflowFunction[]> createStateToFunctionsMap() {
    LOGGER.debug("createStateToFunctionsMap");
    Map<String, IWorkflowFunction[]> stateToFunctionsMap = new HashMap<>();

    final IWorkflowFunction grantReviewerModelAccess = new GrantReviewerModelPolicy(this.modelRepositoryFactory);
    final IWorkflowFunction removeModelPromoterPolicy = new RemoveRoleAccessPolicy(this.modelRepositoryFactory, () -> getRole("model_promoter"));
    final GrantCollaboratorAccessPolicy grantCollaboratorAccessPolicy = new GrantCollaboratorAccessPolicy(this.modelRepositoryFactory);
    final IWorkflowFunction grantPublisherModelAccess = new GrantRoleAccessPolicy(this.modelRepositoryFactory, () -> getRole("model_publisher"));

    stateToFunctionsMap.put(ModelState.Draft.getName(), new IWorkflowFunction[]{grantCollaboratorAccessPolicy});
    stateToFunctionsMap.put(ModelState.InReview.getName(), new IWorkflowFunction[]{grantCollaboratorAccessPolicy, grantReviewerModelAccess, removeModelPromoterPolicy});
    stateToFunctionsMap.put(ModelState.Released.getName(), new IWorkflowFunction[]{grantCollaboratorAccessPolicy, removeModelPromoterPolicy, grantPublisherModelAccess});
    stateToFunctionsMap.put(ModelState.Deprecated.getName(), new IWorkflowFunction[]{grantCollaboratorAccessPolicy, removeModelPromoterPolicy, grantPublisherModelAccess});
    return stateToFunctionsMap;
  }

  private IRole getRole(String roleName) {
    return roleService.findAnyByName(roleName).orElseThrow(() -> new IllegalStateException("The role '" + roleName + "' was not found."));
  }

  private void applyWorkflowFunctions(ModelInfo model, Session session, Map<String, IWorkflowFunction[]> stateToFunctionsMap) {
    LOGGER.debug("applyWorkflowFunctions on Model: " + model.getFileName());
    IWorkflowFunction[] functions = stateToFunctionsMap.get(model.getState());
    if (functions == null) {
      LOGGER.warn("applyWorkflowFunctions: no functions defined for state: " + model.getState() + " of model: " + model.getFileName());
      return;
    }
    Arrays.stream(stateToFunctionsMap.get(model.getState())).forEach(func -> func.execute(model, UserContext.user(SecurityContextHolder.getContext().getAuthentication(), session.getWorkspace().getName()), null));
  }

  private void setVisibility(ModelInfo model) {
    if (IModelRepository.VISIBILITY_PUBLIC.equals(model.getVisibility())) {
      addPolicyEntry(model.getId(), PolicyEntry.of(IModelPolicyManager.ANONYMOUS_ACCESS_POLICY,
              PolicyEntry.PrincipalType.User, PolicyEntry.Permission.READ));
    }
  }


}
