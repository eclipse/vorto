/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 * <p>
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.web.workflow;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.web.workflow.dto.WorkflowResponse;
import org.eclipse.vorto.repository.web.workflow.dto.WorkflowState;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController
@RequestMapping(value = "/rest/workflows")
public class WorkflowController {

    private IWorkflowService workflowService;

    private NamespaceService namespaceService;

    public WorkflowController(
        @Autowired IWorkflowService workflowService,
        @Autowired NamespaceService namespaceService) {

        this.workflowService = workflowService;
        this.namespaceService = namespaceService;
    }

    @ApiOperation(value = "Returns the list of possible actions for a the specific model state")
    @GetMapping(value = "/{modelId:.+}/actions", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<String> getPossibleActions(
        @ApiParam(value = "modelId", required = true) @PathVariable String modelId) {

        return workflowService.getPossibleActions(ModelId.fromPrettyFormat(modelId), UserContext
            .user(SecurityContextHolder.getContext().getAuthentication(), getWorkspaceId(modelId)));
    }

    @ApiOperation(value = "Transitions the model state to the next for the provided action.")
    @PutMapping(value = "/{modelId:.+}/actions/{actionName}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_MODEL_PROMOTER') || hasRole('ROLE_MODEL_REVIEWER')")
    public WorkflowResponse executeAction(
        @ApiParam(value = "modelId", required = true) @PathVariable String modelId,
        @ApiParam(value = "actionName", required = true) @PathVariable String actionName) {

        try {
            ModelInfo model = workflowService.doAction(ModelId.fromPrettyFormat(modelId),
                UserContext.user(SecurityContextHolder.getContext().getAuthentication(),
                    getWorkspaceId(modelId)), actionName);
            return WorkflowResponse.create(model);
        } catch (WorkflowException e) {
            return WorkflowResponse.withErrors(e);
        }
    }

    @ApiOperation(value = "Gets the model of the current workflow state")
    @GetMapping(value = "/{modelId:.+}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_USER')") public WorkflowState getState(
        @ApiParam(value = "modelId", required = true) @PathVariable String modelId) {

        IUserContext user = UserContext
            .user(SecurityContextHolder.getContext().getAuthentication(), getWorkspaceId(modelId));
        return new WorkflowState(
            this.workflowService.getStateModel(ModelId.fromPrettyFormat(modelId), user)
                .orElseThrow(() -> new IllegalStateException("No state model found for " + modelId)));
    }

    private String getWorkspaceId(String namespace) {
        return namespaceService.resolveWorkspaceIdForNamespace(namespace).orElseThrow(
            () -> new ModelNotFoundException("Namespace '" + namespace + "' could not be found."));
    }
}
