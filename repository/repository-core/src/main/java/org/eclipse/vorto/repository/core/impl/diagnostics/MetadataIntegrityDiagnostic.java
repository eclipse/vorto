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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.impl.RepositoryDiagnostics.NodeDiagnostic;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

@Component
public class MetadataIntegrityDiagnostic implements NodeDiagnostic {

  private Collection<String> validStates =
      Arrays.asList("Draft", "InReview", "Released", "Deprecated");

  @Autowired
  private ModelParserFactory modelParserFactory;

  @Override
  public Collection<Diagnostic> apply(Node node) {
    return NodeDiagnosticUtils.getModel(modelParserFactory, node).map(modelInfo -> {
      Collection<Diagnostic> diagnostics = Lists.newArrayList();

      checkNodeProperty(node, "vorto:name", modelInfo.getId().getName())
          .ifPresent(diagnostic -> diagnostics.add(diagnostic));

      checkNodeProperty(node, "vorto:namespace", modelInfo.getId().getNamespace())
          .ifPresent(diagnostic -> diagnostics.add(diagnostic));

      checkNodeProperty(node, "vorto:version", modelInfo.getId().getVersion())
          .ifPresent(diagnostic -> diagnostics.add(diagnostic));

      checkNodeProperty(node, "vorto:description", modelInfo.getDescription())
          .ifPresent(diagnostic -> diagnostics.add(diagnostic));

      checkNodeProperty(node, "vorto:displayname", modelInfo.getDisplayName())
          .ifPresent(diagnostic -> diagnostics.add(diagnostic));

      checkNodeState(node).ifPresent(diagnostic -> diagnostics.add(diagnostic));

      checkModelType(node).ifPresent(diagnostic -> diagnostics.add(diagnostic));

      return diagnostics;
    }).orElse(Collections.emptyList());
  }

  private Optional<Diagnostic> checkNodeProperty(final Node node, final String nodePropertyName,
      final String expectedValue) {
    ModelId modelId = null;
    try {
      modelId = NodeDiagnosticUtils.getModelId(node.getPath())
          .orElseThrow(() -> new NodeDiagnosticException("Cannot get modelId of node"));
      if (!node.getProperty(nodePropertyName).getString().trim()
          .equals(Strings.nullToEmpty(expectedValue).trim())) {
        String message = new StringBuilder("Expected value for node property '")
            .append(nodePropertyName).append("' is '").append(expectedValue).append("' but is '")
            .append(node.getProperty(nodePropertyName).getString()).append("'").toString();

        return Optional.of(new Diagnostic(modelId, message));
      }

      return Optional.empty();
    } catch (RepositoryException e) {
      return Optional.of(new Diagnostic(modelId,
          "Got exception while checking node '" + nodePropertyName + "' : " + e.getMessage()));
    }
  }

  private Optional<Diagnostic> checkNodeState(final Node node) {
    ModelId modelId = null;
    try {
      modelId = NodeDiagnosticUtils.getModelId(node.getPath())
          .orElseThrow(() -> new NodeDiagnosticException("Cannot get modelId of node"));
      if (!validStates.contains(node.getProperty("vorto:state").getString())) {
        String message =
            new StringBuilder("Expected value for node property 'vorto:state' should be in '")
                .append(String.join("', '", validStates)).append("' but is '")
                .append(node.getProperty("vorto:state").getString()).append("'").toString();
        return Optional.of(new Diagnostic(modelId, message));
      }

      return Optional.empty();
    } catch (RepositoryException e) {
      return Optional.of(new Diagnostic(modelId,
          "Got exception while checking node 'vorto:state' : " + e.getMessage()));
    }
  }

  private Optional<Diagnostic> checkModelType(final Node node) {
    ModelId modelId = null;
    try {
      modelId = NodeDiagnosticUtils.getModelId(node.getPath())
          .orElseThrow(() -> new NodeDiagnosticException("Cannot get modelId of node"));
      ModelType fileModelType = ModelType.create(node.getName());
      ModelType nodeModelType = ModelType.valueOf(node.getProperty("vorto:type").getString());
      if (!fileModelType.equals(nodeModelType)) {
        String message =
            new StringBuilder("The model type should be '").append(fileModelType.name())
                .append("' but is '").append(nodeModelType.name()).append("'").toString();
        return Optional.of(new Diagnostic(modelId, message));
      }

      return Optional.empty();
    } catch (RepositoryException e) {
      return Optional.of(new Diagnostic(modelId,
          "Got exception while checking node 'vorto:type' : " + e.getMessage()));
    }
  }

  @Override
  public String getName() {
    return "Metadata integrity";
  }
}
