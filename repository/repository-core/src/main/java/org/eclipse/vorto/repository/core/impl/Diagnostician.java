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
package org.eclipse.vorto.repository.core.impl;

import java.util.Collection;
import java.util.function.Function;
import javax.jcr.Node;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.IDiagnostics;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;

public class Diagnostician extends AbstractRepositoryOperation implements IDiagnostics {

  private RepositoryDiagnostics repoDiagnostics;
  
  public Diagnostician(RepositoryDiagnostics repoDiagnostics) {
    this.repoDiagnostics = repoDiagnostics;
  }

  @Override
  public Collection<Diagnostic> diagnoseAllModels() {
    return doInRootNode(repoDiagnostics::diagnose);
  }

  @Override
  public Collection<Diagnostic> diagnoseModel(ModelId modelId) {
    return doInSession(session -> {
      ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
      Node folderNode = session.getNode(modelIdHelper.getFullPath());
      return repoDiagnostics.diagnose(folderNode);
    });
  }

  private <Result> Result doInRootNode(Function<Node, Result> fn) {
    return doInSession(session -> {
      Node node = session.getRootNode();
      return fn.apply(node);
    });
  }

  public void setRepoDiagnostics(RepositoryDiagnostics repoDiagnostics) {
    this.repoDiagnostics = repoDiagnostics;
  }
}
