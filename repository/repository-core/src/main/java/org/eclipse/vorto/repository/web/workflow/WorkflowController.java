package org.eclipse.vorto.repository.web.workflow;

import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepository;
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
@RequestMapping(value = "/rest/{tenant}/workflows")
public class WorkflowController {

  @Autowired
  private IWorkflowService workflowService;

  @Autowired
  private IModelRepository modelRepository;

  @ApiOperation(value = "Returns the list of possible actions for a the specific model state")
  @RequestMapping(method = RequestMethod.GET, value = "/{modelId:.+}/actions",
      produces = "application/json")
  @PreAuthorize("hasRole('ROLE_MODEL_PROMOTER')")
  public List<String> getPossibleActions(
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId) {
    return workflowService.getPossibleActions(ModelId.fromPrettyFormat(modelId),
        UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName()));
  }

  @ApiOperation(value = "Transitions the model state to the next for the provided action.")
  @RequestMapping(method = RequestMethod.PUT, value = "/{modelId:.+}/actions/{actionName}",
      produces = "application/json")
  @PreAuthorize("hasRole('ROLE_MODEL_PROMOTER')")
  public WorkflowResponse executeAction(
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId,
      @ApiParam(value = "actionName", required = true) @PathVariable String actionName) {

    try {
      ModelInfo model = workflowService.doAction(ModelId.fromPrettyFormat(modelId),
          UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName()),
          actionName);
      return WorkflowResponse.create(model);
    } catch (WorkflowException e) {
      return WorkflowResponse.withErrors(e);
    }

  }

  @ApiOperation(value = "Claims the ownership of a specific model")
  @PreAuthorize("hasRole('ROLE_MODEL_PROMOTER')")
  @RequestMapping(method = RequestMethod.PUT, value = "/{modelId:.+}/actions/Claim",
      produces = "application/json")
  public ModelInfo claimModel(
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId) {
    ModelInfo model = this.modelRepository.getById(ModelId.fromPrettyFormat(modelId));
    model.setAuthor(UserContext
        .user(SecurityContextHolder.getContext().getAuthentication().getName()).getUsername());
    return this.modelRepository.updateMeta(model);

  }

  @ApiOperation(value = "Gets the model of the current workflow state")
  @RequestMapping(method = RequestMethod.GET, value = "/{modelId:.+}",
      produces = "application/json")
  @PreAuthorize("hasRole('ROLE_USER')")
  public WorkflowState getState(
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId) {
    return new WorkflowState(
        this.workflowService.getStateModel(ModelId.fromPrettyFormat(modelId)).get());
  }
}
