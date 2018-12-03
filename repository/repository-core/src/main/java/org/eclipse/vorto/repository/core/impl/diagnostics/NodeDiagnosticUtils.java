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
import java.util.Optional;
import java.util.stream.Collectors;
import javax.jcr.Node;
import javax.jcr.Property;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import com.google.common.base.Strings;

public class NodeDiagnosticUtils {

  public static Optional<ModelInfo> getModel(ModelParserFactory parserFactory, Node node) {
    try {
      Node contentNode = node.getNode("jcr:content");
      if (contentNode == null) {
        return Optional.empty();
      }

      Property contentProperty = contentNode.getProperty("jcr:data");
      if (contentProperty == null) {
        return Optional.empty();
      }

      return Optional.of(
          parserFactory.getParser(node.getName()).parse(contentProperty.getBinary().getStream()));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public static Optional<ModelId> getModelId(String path) {
    Collection<String> fragments = Arrays.asList(path.split("/")).stream()
        .filter(str -> !Strings.isNullOrEmpty(str)).collect(Collectors.toList());
    String[] pathFragments = fragments.toArray(new String[fragments.size()]);

    if (ModelType.supports(pathFragments[pathFragments.length - 1])) {
      if (pathFragments.length > 3) {
        return Optional.of(new ModelId(pathFragments[pathFragments.length - 3],
            String.join(".", Arrays.copyOfRange(pathFragments, 0, pathFragments.length - 3)),
            pathFragments[pathFragments.length - 2]));
      }
    } else {
      if (pathFragments.length > 2) {
        return Optional.of(new ModelId(pathFragments[pathFragments.length - 2],
            String.join(".", Arrays.copyOfRange(pathFragments, 0, pathFragments.length - 2)),
            pathFragments[pathFragments.length - 1]));
      }
    }

    return Optional.empty();
  }

  public static String compileErrorMessage(Throwable e) {
    return e.getClass().getName() + ":" + e.getMessage()
        + (e.getCause() != null ? " <- " + compileErrorMessage(e.getCause()) : "");
  }

}
