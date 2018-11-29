package org.eclipse.vorto.repository.web.workflow.dto;

import org.eclipse.vorto.repository.workflow.model.IAction;

public class WorkflowAction {

  private String name;
  private String description;

  public WorkflowAction(IAction action) {
    this.name = action.getName();
    this.description = action.getDescription();
  }

  protected WorkflowAction() {

  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


}
