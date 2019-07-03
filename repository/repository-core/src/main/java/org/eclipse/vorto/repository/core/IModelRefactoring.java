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
package org.eclipse.vorto.repository.core;

import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.tenant.NewNamespacesNotSupersetException;

public interface IModelRefactoring {

  IModelRefactoring newId(ModelId newId) throws ModelAlreadyExistsException, NewNamespacesNotSupersetException;
    
  /**
   * Performs the actual refactoring on the model
   * 
   * @return result set of changes done by the refactoring
   * @throws RefactoringProblem if something went wrong during refactoring
   */
  RefactoringResult execute(IUserContext userContext) throws RefactoringProblem;
  

  public class RefactoringProblem extends RuntimeException {
    public RefactoringProblem(String msg) {
      super(msg);
    }
    
    public RefactoringProblem(String msg, Throwable t) {
      super(msg,t);
    }
  }
  
  public class RefactoringResult {
    private ModelInfo model;
    private List<ModelInfo> affectedModels;
    
    public static RefactoringResult of (ModelInfo model, List<ModelInfo> affectedModels) {
      return new RefactoringResult(model, affectedModels);
    }
    
    private RefactoringResult(ModelInfo model, List<ModelInfo> affectedModels) {
      this.model = model;
      this.affectedModels = affectedModels;
    }
    
    public ModelInfo getModel() {
      return model;
    }
    
    public List<ModelInfo> getAffectedModels() {
      return affectedModels;
    }
  }
}
