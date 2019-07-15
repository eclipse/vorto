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

import java.util.List;
import java.util.Set;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;

public class ChangeSet implements IChangeSet {

  private IModelWorkspace workspace;
  private Set<Model> changedModels;
  
  public ChangeSet(IModelWorkspace workspace, Set<Model> changedModels) {
    super();
    this.workspace = workspace;
    this.changedModels = changedModels;
  }

  public List<Model> get() {
    return workspace.get();
  }

  public Set<Model> getChanges() {
    return changedModels;
  }
  
  
  
}
