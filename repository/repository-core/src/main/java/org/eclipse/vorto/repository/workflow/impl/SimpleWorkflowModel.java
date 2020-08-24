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
package org.eclipse.vorto.repository.workflow.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.RoleService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.ModelState;
import org.eclipse.vorto.repository.workflow.impl.conditions.HasRoleCondition;
import org.eclipse.vorto.repository.workflow.impl.conditions.IsAdminCondition;
import org.eclipse.vorto.repository.workflow.impl.conditions.IsAnonymousModel;
import org.eclipse.vorto.repository.workflow.impl.conditions.IsLoggedIn;
import org.eclipse.vorto.repository.workflow.impl.conditions.IsReviewerCondition;
import org.eclipse.vorto.repository.workflow.impl.conditions.OrCondition;
import org.eclipse.vorto.repository.workflow.impl.functions.BulkApproveFunction;
import org.eclipse.vorto.repository.workflow.impl.functions.BulkReleaseFunction;
import org.eclipse.vorto.repository.workflow.impl.functions.ClaimOwnership;
import org.eclipse.vorto.repository.workflow.impl.functions.GrantCollaboratorAccessPolicy;
import org.eclipse.vorto.repository.workflow.impl.functions.GrantReviewerModelPolicy;
import org.eclipse.vorto.repository.workflow.impl.functions.GrantRoleAccessPolicy;
import org.eclipse.vorto.repository.workflow.impl.functions.PendingApprovalNotification;
import org.eclipse.vorto.repository.workflow.impl.functions.ReadOnlyRoleAccessPolicy;
import org.eclipse.vorto.repository.workflow.impl.validators.CheckStatesOfDependenciesValidator;
import org.eclipse.vorto.repository.workflow.model.IAction;
import org.eclipse.vorto.repository.workflow.model.IState;
import org.eclipse.vorto.repository.workflow.model.IWorkflowCondition;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.eclipse.vorto.repository.workflow.model.IWorkflowModel;

/**
 * Defines a simple workflow with the following states:
 * DRAFT -> IN_REVIEW -> RELEASED -> DEPRECATED
 */
public class SimpleWorkflowModel implements IWorkflowModel {

  private String name;
  private String description;


  private static final DefaultAction ACTION_INITAL = new DefaultAction("start");
  public static final DefaultAction ACTION_RELEASE = new DefaultAction("Release",
      "Releasing a model will trigger a review process done by the model reviewers of the repository. Once approved, your model will be released. This will trigger a review process for all references in 'draft' as well.");
  public static final DefaultAction ACTION_CLAIM = new DefaultAction("Claim",
      "Claiming the model makes you owner and responsible for the model having full access.");
  public static final DefaultAction ACTION_APPROVE = new DefaultAction("Approve",
      "You agree to the model and its content and confirm the model release and the release of all of its references which are in review.");
  public static final DefaultAction ACTION_REJECT = new DefaultAction("Reject",
      "You do not agree to the model and its content. Please use comments to give feedback to author.");
  public static final DefaultAction ACTION_WITHDRAW = new DefaultAction("Withdraw",
      "When you withdraw, the review process is stopped and your model returns to Draft state where you can make changes.");
  public static final DefaultAction ACTION_DEPRECATE = new DefaultAction("Deprecate",
      "Marks the model as deprecated.");

  public static final DefaultState STATE_DRAFT = new DefaultState(ModelState.Draft.getName(),
      "A draft model is only viewable and editable by collaborators.");
  public static final DefaultState STATE_IN_REVIEW = new DefaultState(ModelState.InReview.getName(),
      "Being reviewed by collaborators with 'Reviewer' role.");
  public static final DefaultState STATE_RELEASED = new DefaultState(ModelState.Released.getName(),
      "A released model cannot be changed or removed and can safely be consumed.");
  public static final DefaultState STATE_DEPRECATED = new DefaultState(
      ModelState.Deprecated.getName(),
      "A deprecated model indicates that the model is obsolete and shall not be used any more.");

  private static final IWorkflowCondition IS_ANONYMOUS_MODEL = new IsAnonymousModel();
  private static final IWorkflowCondition IS_LOGGED_IN = new IsLoggedIn();

  private static final List<IState> ALL_STATES = Arrays
      .asList(STATE_DRAFT, STATE_IN_REVIEW, STATE_RELEASED, STATE_DEPRECATED);

  public SimpleWorkflowModel(
      DefaultUserAccountService userRepository,
      IModelRepositoryFactory repositoryFactory,
      INotificationService notificationService,
      IWorkflowService workflowService,
      RoleService roleService,
      NamespaceService namespaceService,
      UserNamespaceRoleService userNamespaceRoleService) {

    final IWorkflowCondition isAdminCondition = new IsAdminCondition();
    final IWorkflowCondition isReviewerCondition = new IsReviewerCondition(userRepository,
        namespaceService, userNamespaceRoleService, roleService);
    final IWorkflowCondition isPromoterCondition = new HasRoleCondition(userRepository,
        () -> getRole("model_promoter", roleService), namespaceService, userNamespaceRoleService);
    final IWorkflowFunction pendingWorkItemNotification = new PendingApprovalNotification(
        notificationService, userRepository);
    final IWorkflowFunction bulkRelease = new BulkReleaseFunction(workflowService,
        repositoryFactory);
    final IWorkflowFunction bulkApprove = new BulkApproveFunction(workflowService,
        repositoryFactory);

    final IWorkflowFunction grantModelOwnerPolicy = new GrantCollaboratorAccessPolicy(
        repositoryFactory);
    final IWorkflowFunction grantReviewerModelAccess = new GrantReviewerModelPolicy(
        repositoryFactory);
    final IWorkflowFunction grantPublisherModelAccess = new GrantRoleAccessPolicy(repositoryFactory,
        () -> getRole("model_publisher", roleService));
    final IWorkflowFunction claimOwnership = new ClaimOwnership(repositoryFactory);
    final IWorkflowFunction removeModelReviewerPolicy = new ReadOnlyRoleAccessPolicy(
        repositoryFactory, () -> getRole("model_reviewer", roleService));
    final IWorkflowFunction removeModelPromoterPolicy = new ReadOnlyRoleAccessPolicy(
        repositoryFactory, () -> getRole("model_promoter", roleService));

    ACTION_INITAL.setTo(STATE_DRAFT);
    ACTION_INITAL.setFunctions(grantModelOwnerPolicy);

    ACTION_CLAIM.setTo(STATE_DRAFT);
    ACTION_CLAIM.setConditions(IS_LOGGED_IN, IS_ANONYMOUS_MODEL);
    ACTION_CLAIM.setFunctions(claimOwnership);

    ACTION_RELEASE.setTo(STATE_IN_REVIEW);
    ACTION_RELEASE.setConditions(new OrCondition(isPromoterCondition, isAdminCondition));
    ACTION_RELEASE.setFunctions(bulkRelease, pendingWorkItemNotification, grantReviewerModelAccess,
        removeModelPromoterPolicy);

    ACTION_APPROVE.setTo(STATE_RELEASED);
    ACTION_APPROVE.setConditions(new OrCondition(isAdminCondition, isReviewerCondition));
    ACTION_APPROVE.setValidators(
        new CheckStatesOfDependenciesValidator(repositoryFactory, STATE_IN_REVIEW.getName(),
            STATE_RELEASED.getName(), STATE_DEPRECATED.getName()));
    ACTION_APPROVE.setFunctions(bulkApprove, grantPublisherModelAccess, removeModelReviewerPolicy);

    ACTION_REJECT.setTo(STATE_DRAFT);
    ACTION_REJECT.setConditions(new OrCondition(isAdminCondition, isReviewerCondition));

    ACTION_WITHDRAW.setTo(STATE_DRAFT);
    ACTION_WITHDRAW.setConditions(isPromoterCondition);

    ACTION_DEPRECATE.setTo(STATE_DEPRECATED);
    ACTION_DEPRECATE.setConditions(new OrCondition(isPromoterCondition, isAdminCondition));

    STATE_DRAFT.setActions(ACTION_RELEASE, ACTION_CLAIM);
    STATE_IN_REVIEW.setActions(ACTION_APPROVE, ACTION_REJECT, ACTION_WITHDRAW);
    STATE_RELEASED.setActions(ACTION_DEPRECATE);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public IAction getInitialAction() {
    return ACTION_INITAL;
  }

  @Override
  public Optional<IState> getState(String name) {
    return ALL_STATES.stream().filter(state -> state.getName().equals(name)).findFirst();
  }

  private IRole getRole(String roleName, RoleService roleService) {
    return roleService.findAnyByName(roleName)
        .orElseThrow(() -> new IllegalStateException(
            String.format("The role '%s' was not found.", roleName)));
  }
}
