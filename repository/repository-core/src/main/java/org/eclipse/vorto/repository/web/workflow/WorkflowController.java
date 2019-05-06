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
package org.eclipse.vorto.repository.web.workflow;

import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.web.workflow.dto.WorkflowResponse;
import org.eclipse.vorto.repository.web.workflow.dto.WorkflowState;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController
@RequestMapping(value = "/rest/tenants/{tenantId}/workflows")
public class WorkflowController {

  @Autowired
  private IWorkflowService workflowService;

  @ApiOperation(value = "Returns the list of possible actions for a the specific model state")
  @RequestMapping(method = RequestMethod.GET, value = "/{modelId:.+}/actions",
      produces = "application/json")
  @PreAuthorize("hasRole('ROLE_USER')")
  public List<String> getPossibleActions(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId) {
    return workflowService.getPossibleActions(ModelId.fromPrettyFormat(modelId),
        UserContext.user(SecurityContextHolder.getContext().getAuthentication(), tenantId));
  }

  @ApiOperation(value = "Transitions the model state to the next for the provided action.")
  @RequestMapping(method = RequestMethod.PUT, value = "/{modelId:.+}/actions/{actionName}",
      produces = "application/json")
  @PreAuthorize("hasRole('ROLE_MODEL_PROMOTER')")
  public WorkflowResponse executeAction(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId,
      @ApiParam(value = "actionName", required = true) @PathVariable String actionName) {

    try {
      ModelInfo model = workflowService.doAction(ModelId.fromPrettyFormat(modelId),
          UserContext.user(SecurityContextHolder.getContext().getAuthentication(), tenantId),
          actionName);
      return WorkflowResponse.create(model);
    } catch (WorkflowException e) {
      return WorkflowResponse.withErrors(e);
    }

  }

  @ApiOperation(value = "Gets the model of the current workflow state")
  @RequestMapping(method = RequestMethod.GET, value = "/{modelId:.+}",
      produces = "application/json")
  @PreAuthorize("hasRole('ROLE_USER')")
  public WorkflowState getState(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId) {
    
    IUserContext user = UserContext.user(SecurityContextHolder.getContext().getAuthentication(), tenantId);
    
    return new WorkflowState(
        this.workflowService.getStateModel(ModelId.fromPrettyFormat(modelId), user).get());
  }
}
