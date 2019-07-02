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
package org.eclipse.vorto.model.refactor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;

public class RefactoringTask {

  private IModelWorkspace workspace;
  
  private Set<Model> changedModels = new HashSet<Model>();
  
  private RefactoringTask(IModelWorkspace workspace) {
    this.workspace = workspace;
  }
  
  public static RefactoringTask from(IModelWorkspace workspace) {
    return new RefactoringTask(workspace);
  }
  
  public RefactoringTask toNamespace(String targetNamespace, String... ignoredNamespaces) {
    
    Set<String> ignoreNamespaceList = new HashSet<>();
    if (ignoredNamespaces != null) {
      ignoreNamespaceList.addAll(Arrays.asList(ignoredNamespaces));
    }
    
    workspace.get().forEach(model -> {
      final String oldNamespace = model.getNamespace();
      if(!ignoreNamespaceList.contains(oldNamespace) && !model.getNamespace().startsWith(targetNamespace)) {
        final String newNamespace = targetNamespace+"."+oldNamespace;
        model.setNamespace(newNamespace);
        ModelId newModelId = ModelIdFactory.newInstance(model);
        ModelId oldModelId = new ModelId(newModelId.getModelType(),newModelId.getName(),oldNamespace,newModelId.getVersion());
        updateReferences(oldModelId,newModelId);
        changedModels.add(model);
      }

    });
    return this;
  }
  
  private void updateReferences(ModelId oldId, ModelId newId ) {
    workspace.get().forEach(model -> {
      model.getReferences().stream().forEach(reference -> {
        if (reference.getImportedNamespace().equals(oldId.getNamespace()+"."+oldId.getName())) {
          reference.setImportedNamespace(newId.getNamespace()+"."+newId.getName());
          changedModels.add(model);
        }
      });
    });
  }
  
  public ChangeSet execute() {
    return new ChangeSet(this.workspace,changedModels);
  }

}
