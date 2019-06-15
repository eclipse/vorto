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
      if(!ignoreNamespaceList.contains(oldNamespace)) {
        model.setNamespace(targetNamespace);
        model.getReferences().stream().forEach(reference -> {
          reference.setImportedNamespace(reference.getImportedNamespace().replace(oldNamespace,targetNamespace));
        });
        changedModels.add(model);
      }

    });
    return this;
  }
  
  public ChangeSet execute() {
    return new ChangeSet(this.workspace,changedModels);
  }

}
