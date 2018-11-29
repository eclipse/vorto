/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.core.impl.diagnostics;

import java.util.Collection;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.impl.RepositoryDiagnostics.NodeDiagnostic;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.springframework.stereotype.Component;
import com.google.common.collect.Lists;

@Component
public class ReferenceIntegrityDiagnostic implements NodeDiagnostic {
  private static final String VORTO_REFERENCES = "vorto:references";

  @Override
  public Collection<Diagnostic> apply(Node node) {
    Collection<Diagnostic> diagnostics = Lists.newArrayList();

    try {
      String nodeId = node.getIdentifier();
      ModelId modelId = NodeDiagnosticUtils.getModelId(node.getPath())
          .orElseThrow(() -> new NodeDiagnosticException(
              "Cannot generate modelId from supplied node with identifier '" + nodeId + "'"));

      if (node.hasProperty(VORTO_REFERENCES)) {
        for (Value value : node.getProperty(VORTO_REFERENCES).getValues()) {
          try {
            node.getSession().getNodeByIdentifier(value.getString());
          } catch (ItemNotFoundException e) {
            diagnostics.add(new Diagnostic(modelId, "The model has reference to node '"
                + value.getString() + "' but it cannot be found in the repository."));
          }
        }
      }

      PropertyIterator propIter = node.getReferences();
      while (propIter.hasNext()) {
        Property prop = propIter.nextProperty();
        try {
          Node referencedByFileNode = prop.getParent();
          ModelIdHelper.fromPath(referencedByFileNode.getParent().getPath());
        } catch (Exception e) {
          diagnostics
              .add(new Diagnostic(modelId, "The model is being referenced by a stale node."));
        }
      }
    } catch (RepositoryException e) {
      throw new NodeDiagnosticException("Error in accessing some properties of node", e);
    }
    return diagnostics;
  }

  @Override
  public String getName() {
    return "Reference integrity";
  }

}
