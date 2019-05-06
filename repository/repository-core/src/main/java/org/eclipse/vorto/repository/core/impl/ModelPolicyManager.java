/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javax.jcr.AccessDeniedException;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.PolicyEntry;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.core.PolicyEntry.PrincipalType;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.modeshape.jcr.security.SimplePrincipal;
import org.springframework.beans.factory.annotation.Autowired;

public class ModelPolicyManager extends AbstractRepositoryOperation implements IModelPolicyManager {

  private static Logger logger = Logger.getLogger(ModelPolicyManager.class);

  private IUserAccountService userAccountService;

  public ModelPolicyManager(@Autowired IUserAccountService userAccountService) {
    this.userAccountService = userAccountService;
  }

  @Override
  public Collection<PolicyEntry> getPolicyEntries(ModelId modelId) {
    return doInSession(session -> {
      List<PolicyEntry> policyEntries = new ArrayList<PolicyEntry>();

      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

        final Node folderNode = session.getNode(modelIdHelper.getFullPath());

        if (!folderNode.getNodes(FILE_NODES).hasNext()) {
          throw new ModelNotFoundException("Could not find model with ID " + modelId);
        }
        Node fileNode = folderNode.getNodes(FILE_NODES).nextNode();

        AccessControlManager acm = session.getAccessControlManager();

        AccessControlList acl = null;
        AccessControlPolicyIterator it = acm.getApplicablePolicies(fileNode.getPath());
        if (it.hasNext()) {
          acl = (AccessControlList) it.nextAccessControlPolicy();
        } else {
          acl = (AccessControlList) acm.getPolicies(fileNode.getPath())[0];
        }

        for (AccessControlEntry entry : acl.getAccessControlEntries()) {
          PolicyEntry policy = PolicyEntry.of(entry);
          if (!policy.isAdminPolicy()) {
            policyEntries.add(policy);
          }
        }

        return policyEntries;
      } catch (AccessDeniedException ex) {
        throw new NotAuthorizedException(modelId);
      }
    });
  }

  @Override
  public void addPolicyEntry(ModelId modelId, PolicyEntry... newEntries) {
    doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

        final Node folderNode = session.getNode(modelIdHelper.getFullPath());
        if (!folderNode.getNodes(FILE_NODES).hasNext()) {
          logger.warn("Cannot add policy entry to model " + modelId);
          session.logout();
          return null;
        }
        Node fileNode = folderNode.getNodes(FILE_NODES).nextNode();

        AccessControlManager acm = session.getAccessControlManager();

        AccessControlList acl = null;
        AccessControlPolicyIterator it = acm.getApplicablePolicies(fileNode.getPath());
        if (it.hasNext()) {
          acl = (AccessControlList) it.nextAccessControlPolicy();
        } else {
          acl = (AccessControlList) acm.getPolicies(fileNode.getPath())[0];
        }

        final AccessControlList _acl = acl;

        // put all existing ACE that are in newEntries to existingEntries
        List<AccessControlEntry> existingEntries = new ArrayList<>();
        for (AccessControlEntry ace : acl.getAccessControlEntries()) {
          Arrays.asList(newEntries).stream().forEach(entry -> {
            if (entry.isSame(ace)) {
              existingEntries.add(ace);
            }
          });
        }

        // remove all existingEntries, entries that are in newEntries
        if (!existingEntries.isEmpty()) {
          existingEntries.stream().forEach(ace -> {
            try {
              _acl.removeAccessControlEntry(ace);
            } catch (Exception e) {
              logger.error("Could not grant user readd permissions for model", e);
            }
          });
        }

        // create ACE for every entry in newEntries
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

        acm.setPolicy(fileNode.getPath(), _acl);
        session.save();
        return null;
      } catch (AccessDeniedException ex) {
        throw new NotAuthorizedException(modelId);
      }
    });
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

  @Override
  public void removePolicyEntry(ModelId modelId, PolicyEntry entryToRemove) {
    entryToRemove.setPermission(null);
    this.addPolicyEntry(modelId, entryToRemove);

    if (this.getPolicyEntries(modelId).isEmpty()) {
      doInSession(session -> {
        try {
          ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

          final Node folderNode = session.getNode(modelIdHelper.getFullPath());
          Node fileNode = folderNode.getNodes(FILE_NODES).nextNode();

          AccessControlManager acm = session.getAccessControlManager();

          AccessControlList acl = null;
          AccessControlPolicyIterator it = acm.getApplicablePolicies(fileNode.getPath());
          if (it.hasNext()) {
            acl = (AccessControlList) it.nextAccessControlPolicy();
          } else {
            acl = (AccessControlList) acm.getPolicies(fileNode.getPath())[0];
          }

          acm.removePolicy(fileNode.getPath(), acl);
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
    return doInSession(session -> {
      try {
        ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);

        Node folderNode = session.getNode(modelIdHelper.getFullPath());

        if (permission == Permission.READ) {
          return folderNode.getNodes(FILE_NODES).hasNext();
        } else {
          return this.getPolicyEntries(modelId).stream().filter(userFilter(session))
              .filter(p -> hasPermission(p.getPermission(), permission)).findAny().isPresent();
        }
      } catch (AccessDeniedException e) {
        return false;
      }
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
}
