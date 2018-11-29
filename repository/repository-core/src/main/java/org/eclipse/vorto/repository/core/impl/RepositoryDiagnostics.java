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
package org.eclipse.vorto.repository.core.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.impl.diagnostics.NodeDiagnosticException;
import org.eclipse.vorto.repository.core.impl.diagnostics.NodeDiagnosticUtils;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.common.collect.Lists;

@Component
public class RepositoryDiagnostics {

  @Autowired
  private Collection<NodeDiagnostic> nodeDiagnosticTests = Collections.emptyList();

  private static Logger logger = Logger.getLogger(RepositoryDiagnostics.class);
  private static final String FILE_MIXIN = "nt:file";
  private static final String JCR_SYSTEM_NODE_PREFIX = "/jcr:system";

  public interface NodeDiagnostic extends Function<Node, Collection<Diagnostic>> {
    public String getName();
  }

  public Collection<Diagnostic> diagnose(final Node node) {
    if (node == null) {
      return Collections.emptyList();
    }

    Collection<Diagnostic> diagnostics = Lists.newArrayList();
    try {
      NodeIterator iterator = node.getNodes();
      while (iterator.hasNext()) {
        Node childNode = iterator.nextNode();
        if (!isSystemNode(childNode)) {
          if (isModelNode(childNode)) {
            Collection<Diagnostic> modelDiagnostics = diagnoseNode(childNode);
            for (Diagnostic diagnostic : modelDiagnostics) {
              diagnostic.setNodeId(childNode.getIdentifier());
            }
            diagnostics.addAll(modelDiagnostics);
          } else {
            diagnostics.addAll(diagnose(childNode));
          }
        }
      }
    } catch (RepositoryException e) {
      throw new FatalModelRepositoryException("Diagnostics threw an exception", e);
    }

    return diagnostics.stream().distinct().collect(Collectors.toList());
  }

  private boolean isSystemNode(final Node node) throws RepositoryException {
    return node.getPath().startsWith(JCR_SYSTEM_NODE_PREFIX);
  }

  private boolean isModelNode(final Node node) throws RepositoryException {
    return node.isNodeType(FILE_MIXIN) && ModelParserFactory.hasParserFor(node.getName());
  }

  private Collection<Diagnostic> diagnoseNode(final Node node) throws RepositoryException {
    logger.info("Diagnosing " + node.getPath());
    Collection<Diagnostic> diagnostics = Lists.newArrayList();
    for (NodeDiagnostic nodeDiagnosticTest : nodeDiagnosticTests) {
      try {
        diagnostics.addAll(nodeDiagnosticTest.apply(node));
      } catch (NodeDiagnosticException e) {
        logger.error("Got an exception from diagnostic [" + nodeDiagnosticTest.getName() + "]:", e);
        Optional<ModelId> maybeModelId = NodeDiagnosticUtils.getModelId(node.getPath());
        diagnostics.add(new Diagnostic(maybeModelId.orElse(null),
            "'" + nodeDiagnosticTest.getName() + "' returned an error '" + e.getMessage() + "'"));
      }
    }

    return diagnostics;
  }

  public void setNodeDiagnosticTests(Collection<NodeDiagnostic> nodeDiagnostics) {
    this.nodeDiagnosticTests = Objects.requireNonNull(nodeDiagnostics);
  }
}
