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
package org.eclipse.vorto.repository.workflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.junit.Test;

public class WorkflowTest extends AbstractIntegrationTest {

  @Test
  public void testGetModelByState() throws Exception {
    IUserContext user = createUserContext("alex", "playground");
    ModelInfo model = importModel("Color.type");
    workflow.start(model.getId(), user);
    assertEquals(1,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_DRAFT.getName(), user).size());
  }

  @Test
  public void testSearchByTypeAndState() throws Exception {
    IUserContext user = createUserContext("alex", "playground");
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
    workflow.start(model.getId(), createUserContext("alex", "playground"));
    assertEquals(1, workflow
        .getPossibleActions(model.getId(), createUserContext(getCallerId(), "playground")).size());
    assertEquals(SimpleWorkflowModel.ACTION_RELEASE.getName(), workflow
        .getPossibleActions(model.getId(), createUserContext(getCallerId(), "playground")).get(0));
  }

  @Test
  public void testStartReviewProcessForModel() throws Exception {
    ModelInfo model = importModel("creator", "Color.type");
    IUserContext user = createUserContext("creator", "playground");
    workflow.start(model.getId(), user);
    model = workflow.doAction(model.getId(), createUserContext("promoter", "playground"),
        SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), model.getState());
    assertEquals(0,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_DRAFT.getName(), user).size());
    assertEquals(1,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), user).size());

    assertEquals(1, workflow
        .getPossibleActions(model.getId(), createUserContext("promoter", "playground")).size());
  }

  @Test(expected = WorkflowException.class)
  public void testTransitionWorkflowInvalidAction() throws Exception {
    ModelInfo model = importModel("Color.type");
    workflow.start(model.getId(), createUserContext("alex", "playground"));
    model = workflow.doAction(model.getId(), createUserContext(getCallerId(), "playground"),
        SimpleWorkflowModel.ACTION_APPROVE.getName());
  }

  @Test
  public void testApproveModelByAdminInReviewState() throws Exception {
    ModelInfo model = importModel("creator", "Color.type");
    IUserContext creator = createUserContext("creator", "playground");
    IUserContext promoter = createUserContext("promoter", "playground");
    IUserContext reviewer = createUserContext("reviewer", "playground");

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
    IUserContext user = createUserContext("creator", "playground");
    IUserContext promoter = createUserContext("promoter", "playground");
    workflow.start(model.getId(), user);

    model = workflow.doAction(model.getId(), promoter, SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), model.getState());

    assertEquals(1, workflow.getPossibleActions(model.getId(), promoter).size());
    assertEquals(2, workflow
        .getPossibleActions(model.getId(), createUserContext("reviewer", "playground")).size());
    model = workflow.doAction(model.getId(), createUserContext("reviewer", "playground"),
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
    IUserContext creator = createUserContext("creator", "playground");
    IUserContext promoter = createUserContext("promoter", "playground");
    workflow.start(model.getId(), creator);

    model = workflow.doAction(model.getId(), promoter, SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), model.getState());

    assertEquals(1, workflow.getPossibleActions(model.getId(), promoter).size());

    assertEquals(2, workflow
        .getPossibleActions(model.getId(), createUserContext("reviewer", "playground")).size());

    model = workflow.doAction(model.getId(), createUserContext("reviewer", "playground"),
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
    IUserContext creator = createUserContext("creator", "playground");
    workflow.start(model.getId(), creator);

    model = workflow.doAction(model.getId(), createUserContext("promoter", "playground"), SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), model.getState());
    model = workflow.doAction(model.getId(), creator, SimpleWorkflowModel.ACTION_APPROVE.getName());
  }

  @Test
  public void testRejectModelInReviewState() throws Exception {
    ModelInfo model = importModel("Color.type");
    IUserContext user = createUserContext("alex", "playground");
    workflow.start(model.getId(), user);

    when(
        userRepository.findByUsername(createUserContext(getCallerId(), "playground").getUsername()))
            .thenReturn(User.create(getCallerId(), new Tenant("playground"), Role.USER));

    model = workflow.doAction(model.getId(), createUserContext(getCallerId(), "playground"),
        SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), model.getState());

    when(userRepository.findByUsername(createUserContext("admin", "playground").getUsername()))
        .thenReturn(User.create("admin", new Tenant("playground"), Role.SYS_ADMIN));

    model = workflow.doAction(model.getId(), createUserContext("admin", "playground"),
        SimpleWorkflowModel.ACTION_REJECT.getName());
    assertEquals(0,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_RELEASED.getName(), user).size());
    assertEquals(0,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), user).size());
    assertEquals(1,
        workflow.getModelsByState(SimpleWorkflowModel.STATE_DRAFT.getName(), user).size());
    assertEquals(1, workflow
        .getPossibleActions(model.getId(), createUserContext(getCallerId(), "playground")).size());
  }

  @Test
  public void testStartReleaseModelWithReleasedDependencies() throws Exception {

    ModelInfo typeModel = importModel("Color.type");
    workflow.start(typeModel.getId(), createUserContext("alex", "playground"));

    ModelInfo fbModel = importModel("Colorlight.fbmodel");
    workflow.start(fbModel.getId(), createUserContext("alex", "playground"));

    setReleaseState(typeModel);

    when(
        userRepository.findByUsername(createUserContext(getCallerId(), "playground").getUsername()))
            .thenReturn(User.create(getCallerId(), new Tenant("playground"), Role.USER));
    fbModel = workflow.doAction(fbModel.getId(), createUserContext(getCallerId(), "playground"),
        SimpleWorkflowModel.ACTION_RELEASE.getName());
    assertEquals(SimpleWorkflowModel.STATE_IN_REVIEW.getName(), fbModel.getState());
  }

  @Test
  public void testStartReleaseModelWithNonReleasedDependencies() throws Exception {
    ModelInfo typeModel = importModel("Color.type");
    workflow.start(typeModel.getId(), createUserContext("alex", "playground"));

    ModelInfo fbModel = importModel("Colorlight.fbmodel");
    workflow.start(fbModel.getId(), createUserContext("alex", "playground"));

    try {
      when(userRepository
          .findByUsername(createUserContext(getCallerId(), "playground").getUsername()))
              .thenReturn(User.create(getCallerId(), new Tenant("playground"), Role.USER));
      workflow.doAction(fbModel.getId(), createUserContext(getCallerId(), "playground"),
          SimpleWorkflowModel.ACTION_RELEASE.getName());
      fail();
    } catch (InvalidInputException ex) {
      System.out.println(ex.getMessage());
    }
  }


  @Test
  public void testStartReleaseModelWithReviewedDependencies() throws Exception {
    ModelInfo typeModel = importModel("Color.type");
    workflow.start(typeModel.getId(), createUserContext("alex", "playground"));

    ModelInfo fbModel = importModel("Colorlight.fbmodel");
    workflow.start(fbModel.getId(), createUserContext("alex", "playground"));

    when(
        userRepository.findByUsername(createUserContext(getCallerId(), "playground").getUsername()))
            .thenReturn(User.create(getCallerId(), new Tenant("playground"), Role.USER));
    workflow.doAction(typeModel.getId(), createUserContext(getCallerId(), "playground"),
        SimpleWorkflowModel.ACTION_RELEASE.getName());

    assertEquals("InReview",
        workflow.doAction(fbModel.getId(), createUserContext(getCallerId(), "playground"),
            SimpleWorkflowModel.ACTION_RELEASE.getName()).getState());

  }

}
