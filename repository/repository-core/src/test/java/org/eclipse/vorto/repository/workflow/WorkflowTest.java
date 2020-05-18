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
package org.eclipse.vorto.repository.workflow;

import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class WorkflowTest extends AbstractIntegrationTest {

  private static final String ADMIN = "admin";
  private static final String PLAYGROUND = "playground";
  private static final String GITHUB = "GITHUB";


  @Test
  public void testGetModelByState() throws Exception {
    IUserContext user = createUserContext("admin", PLAYGROUND);
    ModelInfo model = importModel("Color.type");
    workflow.start(model.getId(), user);
    assertEquals(1,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_DRAFT.getName(), user).size());
  }

  @Test
  public void testSearchByTypeAndState() throws Exception {
    IUserContext user = createUserContext("alex", PLAYGROUND);
    ModelInfo model = importModel("Color.type");
    workflow.start(model.getId(), user);
    model = importModel("Colorlight.fbmodel");
    workflow.start(model.getId(), user);
    assertEquals(0,
        repositoryFactory.getRepository(user).search("Functionblock state:Released").size());
  }

  @Test
  public void testGetPossibleActionsForDraftState() throws Exception {
    ModelInfo model = importModel("Color.type");
    workflow.start(model.getId(), createUserContext("alex", PLAYGROUND));
    assertEquals(1, workflow
        .getPossibleActions(model.getId(), createUserContext(getCallerId(), PLAYGROUND)).size());
    assertEquals(SimpleWorkflowModel.ACTION_RELEASE.getName(), workflow
        .getPossibleActions(model.getId(), createUserContext(getCallerId(), PLAYGROUND)).get(0));
  }

  @Test
  public void testStartReviewProcessForModel() throws Exception {
    ModelInfo model = importModel("creator", "Color.type");
    IUserContext user = createUserContext("creator", PLAYGROUND);
    workflow.start(model.getId(), user);
    model = workflow.doAction(model.getId(), createUserContext("promoter", PLAYGROUND),
        SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), model.getState());
    assertEquals(0,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_DRAFT.getName(), user).size());
    assertEquals(1,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), user).size());

    assertEquals(1, workflow
        .getPossibleActions(model.getId(), createUserContext("promoter", PLAYGROUND)).size());
  }

  @Test(expected = WorkflowException.class)
  public void testTransitionWorkflowInvalidAction() throws Exception {
    ModelInfo model = importModel("Color.type");
    workflow.start(model.getId(), createUserContext("alex", PLAYGROUND));
    model = workflow.doAction(model.getId(), createUserContext(getCallerId(), PLAYGROUND),
        SimpleWorkflowModel.ACTION_APPROVE.getName());
  }

  @Test
  public void testApproveModelByAdminInReviewState() throws Exception {
    ModelInfo model = importModel("creator", "Color.type");
    IUserContext creator = createUserContext("creator", PLAYGROUND);
    IUserContext promoter = createUserContext("promoter", PLAYGROUND);
    IUserContext reviewer = createUserContext("reviewer", PLAYGROUND);

    workflow.start(model.getId(), creator);

    model = workflow.doAction(model.getId(), promoter, SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), model.getState());

    assertEquals(1, workflow.getPossibleActions(model.getId(), promoter).size());
    assertEquals(2, workflow.getPossibleActions(model.getId(), reviewer).size());

    model = workflow.doAction(model.getId(), reviewer, SimpleWorkflowModel.ACTION_APPROVE.getName());
    assertEquals(1,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_RELEASED.getName(), creator).size());
    assertEquals(0,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), creator).size());
    assertEquals(0,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_DRAFT.getName(), creator).size());
    assertEquals(0, workflow.getPossibleActions(model.getId(), creator).size());
  }

  @Test
  public void testApproveModelByModelReviewerInReviewState() throws Exception {
    ModelInfo model = importModel("creator", "Color.type");
    IUserContext user = createUserContext("creator", PLAYGROUND);
    IUserContext promoter = createUserContext("promoter", PLAYGROUND);
    workflow.start(model.getId(), user);

    model = workflow.doAction(model.getId(), promoter, SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), model.getState());

    assertEquals(1, workflow.getPossibleActions(model.getId(), promoter).size());
    assertEquals(2, workflow
        .getPossibleActions(model.getId(), createUserContext("reviewer", PLAYGROUND)).size());
    model = workflow.doAction(model.getId(), createUserContext("reviewer", PLAYGROUND),
        SimpleWorkflowModel.ACTION_APPROVE.getName());
    assertEquals(1,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_RELEASED.getName(), user).size());
    assertEquals(0,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), user).size());
    assertEquals(0,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_DRAFT.getName(), user).size());
    assertEquals(0, workflow.getPossibleActions(model.getId(), user).size());
  }

  @Test
  public void testRejectModelByModelReviewerInReviewState() throws Exception {
    ModelInfo model = importModel("creator", "Color.type");
    IUserContext creator = createUserContext("creator", PLAYGROUND);
    IUserContext promoter = createUserContext("promoter", PLAYGROUND);
    workflow.start(model.getId(), creator);

    model = workflow.doAction(model.getId(), promoter, SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), model.getState());

    assertEquals(1, workflow.getPossibleActions(model.getId(), promoter).size());

    assertEquals(2, workflow
        .getPossibleActions(model.getId(), createUserContext("reviewer", PLAYGROUND)).size());

    model = workflow.doAction(model.getId(), createUserContext("reviewer", PLAYGROUND),
        SimpleWorkflowModel.ACTION_REJECT.getName());

    assertEquals(0,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_RELEASED.getName(), creator).size());
    assertEquals(0,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), creator).size());
    assertEquals(1,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_DRAFT.getName(), creator).size());
    assertEquals(1, workflow.getPossibleActions(model.getId(), promoter).size());
  }

  @Test(expected = WorkflowException.class)
  public void testApproveModelByUserInReviewState() throws Exception {
    ModelInfo model = importModel("creator", "Color.type");
    IUserContext creator = createUserContext("creator", PLAYGROUND);
    workflow.start(model.getId(), creator);

    model = workflow.doAction(model.getId(), createUserContext("promoter", PLAYGROUND), SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), model.getState());
    model = workflow.doAction(model.getId(), creator, SimpleWorkflowModel.ACTION_APPROVE.getName());
  }

  @Test
  public void testRejectModelInReviewState() throws Exception {
    ModelInfo model = importModel("Color.type");
    IUserContext user = createUserContext("alex", PLAYGROUND);
    workflow.start(model.getId(), user);

    when(
        userRepository.findByUsername(createUserContext(getCallerId(), PLAYGROUND).getUsername()))
            .thenReturn(User.create(getCallerId(), GITHUB, null, new Tenant(PLAYGROUND), Role.USER));

    model = workflow.doAction(model.getId(), createUserContext(getCallerId(), PLAYGROUND),
        SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), model.getState());

    when(userRepository.findByUsername(createUserContext(ADMIN, PLAYGROUND).getUsername()))
        .thenReturn(User.create(ADMIN, GITHUB, null, new Tenant(PLAYGROUND), Role.SYS_ADMIN));

    model = workflow.doAction(model.getId(), createUserContext(ADMIN, PLAYGROUND),
        SimpleWorkflowModel.ACTION_REJECT.getName());
    assertEquals(0,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_RELEASED.getName(), user).size());
    assertEquals(0,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), user).size());
    assertEquals(1,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_DRAFT.getName(), user).size());
    assertEquals(1, workflow
        .getPossibleActions(model.getId(), createUserContext(getCallerId(), PLAYGROUND)).size());
  }

  @Test
  public void testStartReleaseModelWithReleasedDependencies() throws Exception {

    ModelInfo typeModel = importModel("Color.type");
    workflow.start(typeModel.getId(), createUserContext("alex", PLAYGROUND));

    ModelInfo fbModel = importModel("Colorlight.fbmodel");
    workflow.start(fbModel.getId(), createUserContext("alex", PLAYGROUND));

    setReleaseState(typeModel);

    when(
        userRepository.findByUsername(createUserContext(getCallerId(), PLAYGROUND).getUsername()))
            .thenReturn(User.create(getCallerId(), GITHUB, null, new Tenant(PLAYGROUND), Role.USER));
    fbModel = workflow.doAction(fbModel.getId(), createUserContext(getCallerId(), PLAYGROUND),
        SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), fbModel.getState());
  }

  @Test
  public void testStartReleaseModelWithNonReleasedDependencies() throws Exception {
    ModelInfo typeModel = importModel("Color.type");
    workflow.start(typeModel.getId(), createUserContext("alex", PLAYGROUND));

    ModelInfo fbModel = importModel("Colorlight.fbmodel");
    workflow.start(fbModel.getId(), createUserContext("alex", PLAYGROUND));

    when(userRepository
        .findByUsername(createUserContext(getCallerId(), PLAYGROUND).getUsername()))
            .thenReturn(User.create(getCallerId(), GITHUB, null, new Tenant(PLAYGROUND), Role.USER));
    workflow.doAction(fbModel.getId(), createUserContext(getCallerId(), PLAYGROUND),
        SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals("InReview",this.repositoryFactory.getRepositoryByModel(typeModel.getId()).getById(typeModel.getId()).getState());
    assertEquals("InReview",this.repositoryFactory.getRepositoryByModel(fbModel.getId()).getById(fbModel.getId()).getState());
  }

  @Test
  public void testApproveWithNonApprovedDependencies() throws Exception {
    ModelInfo typeModel = importModel("Color.type");
    workflow.start(typeModel.getId(), createUserContext("alex", PLAYGROUND));

    ModelInfo fbModel = importModel("Colorlight.fbmodel");
    workflow.start(fbModel.getId(), createUserContext("alex", PLAYGROUND));

    when(userRepository
            .findByUsername(createUserContext(getCallerId(), PLAYGROUND).getUsername()))
            .thenReturn(User.create(getCallerId(), GITHUB, null, new Tenant(PLAYGROUND), Role.USER));
    workflow.doAction(fbModel.getId(), createUserContext(getCallerId(), PLAYGROUND),
            SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals("InReview",this.repositoryFactory.getRepositoryByModel(typeModel.getId()).getById(typeModel.getId()).getState());
    assertEquals("InReview",this.repositoryFactory.getRepositoryByModel(fbModel.getId()).getById(fbModel.getId()).getState());
    workflow.doAction(fbModel.getId(), createUserContext(getCallerId(), PLAYGROUND),
            SimpleWorkflowModel.ACTION_APPROVE.getName());
  }


  @Test
  public void testStartReleaseModelWithReviewedDependencies() throws Exception {
    ModelInfo typeModel = importModel("Color.type");
    workflow.start(typeModel.getId(), createUserContext("alex", PLAYGROUND));

    ModelInfo fbModel = importModel("Colorlight.fbmodel");
    workflow.start(fbModel.getId(), createUserContext("alex", PLAYGROUND));

    when(
        userRepository.findByUsername(createUserContext(getCallerId(), PLAYGROUND).getUsername()))
            .thenReturn(User.create(getCallerId(), GITHUB, null, new Tenant(PLAYGROUND), Role.USER));
    workflow.doAction(typeModel.getId(), createUserContext(getCallerId(), PLAYGROUND),
        SimpleWorkflowModel.ACTION_RELEASE.getName());

    assertEquals("InReview",
        workflow.doAction(fbModel.getId(), createUserContext(getCallerId(), PLAYGROUND),
            SimpleWorkflowModel.ACTION_RELEASE.getName()).getState());

  }

}
