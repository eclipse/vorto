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
package org.eclipse.vorto.repository.workflow.impl.functions;

import java.util.Map;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.ModelState;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Traverses the models references and triggers the release process, recursively
 *
 */
public class BulkReleaseFunction implements IWorkflowFunction {
  
  private IWorkflowService workflowService;
  private IModelRepositoryFactory repositoryFactory;
  
  private static final Logger logger = LoggerFactory.getLogger(BulkReleaseFunction.class);

  
  public BulkReleaseFunction(IWorkflowService workflowService,IModelRepositoryFactory repositoryFactory) {
    this.workflowService = workflowService;
    this.repositoryFactory = repositoryFactory;
  }
  
  @Override
  public void execute(ModelInfo model, IUserContext user,Map<String,Object> context) {    
    for (ModelId referenceId : model.getReferences()) {
      IModelRepository repository = repositoryFactory.getRepositoryByModel(referenceId);
      ModelInfo referenceModel = repository.getById(referenceId);
      if (ModelState.DRAFT.getName().equals(referenceModel.getState())) {
        try {
          workflowService.doAction(referenceId, user, SimpleWorkflowModel.ACTION_RELEASE.getName());
        } catch (WorkflowException e) {
          logger.error("Problem executing release for model " + referenceId + "during bulk release function",e);
        }
      }
      
    }
  }

}
