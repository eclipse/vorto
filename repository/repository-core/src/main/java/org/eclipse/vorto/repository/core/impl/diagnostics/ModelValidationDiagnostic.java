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
import java.util.Objects;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.impl.RepositoryDiagnostics.NodeDiagnostic;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.common.collect.Lists;

@Component
public class ModelValidationDiagnostic implements NodeDiagnostic {

  @Autowired
  private ModelParserFactory modelParserFactory;

  private static Logger logger = Logger.getLogger(ModelValidationDiagnostic.class);

  @Override
  public Collection<Diagnostic> apply(Node node) {
    Objects.requireNonNull(node);

    Collection<Diagnostic> diagnostics = Lists.newArrayList();

    String nodeId = null;

    try {
      nodeId = node.getIdentifier();

      Node contentNode = node.getNode("jcr:content");
      if (contentNode == null) {
        diagnostics.add(new Diagnostic(ModelIdHelper.fromPath(node.getPath()),
            "Model node is empty. No <jcr:content>."));
        return diagnostics;
      }

      Property contentProperty = contentNode.getProperty("jcr:data");
      if (contentProperty == null) {
        diagnostics.add(new Diagnostic(ModelIdHelper.fromPath(node.getPath()),
            "Model node has no file. No <jcr:data> property."));
        return diagnostics;
      }

      try {
        logger.debug("Validating \n" + IOUtils.toString(contentProperty.getBinary().getStream()));
        modelParserFactory.getParser(node.getName()).parse(contentProperty.getBinary().getStream());
      } catch (ValidationException e) {
        diagnostics.add(new Diagnostic(NodeDiagnosticUtils.getModelId(node.getPath()).orElse(null),
            e.getMessage()));
      } catch (Exception e) {
        logger.error("Caught error in diagnosing '" + nodeId + "'", e);
        diagnostics.add(new Diagnostic(NodeDiagnosticUtils.getModelId(node.getPath()).orElse(null),
            NodeDiagnosticUtils.compileErrorMessage(e)));
      }

    } catch (RepositoryException e) {
      throw new NodeDiagnosticException("Exception while trying to validate node '" + nodeId + "'",
          e);
    }

    return diagnostics;
  }

  public void setModelParserFactory(ModelParserFactory modelParserFactory) {
    this.modelParserFactory = modelParserFactory;
  }

  @Override
  public String getName() {
    return "Model Validation";
  }
}
