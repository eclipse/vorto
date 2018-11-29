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
package org.eclipse.vorto.repository.api;

import org.eclipse.vorto.model.ModelType;

@Deprecated
public class ModelQueryBuilder {

  private StringBuilder expression = new StringBuilder();

  public ModelQueryBuilder name(String modelName) {
    expression.append("name:");
    expression.append(modelName);
    expression.append(" ");
    return this;
  }

  public ModelQueryBuilder namespace(String modelNamespace) {
    expression.append("namespace:");
    expression.append(modelNamespace);
    expression.append(" ");
    return this;
  }

  public ModelQueryBuilder type(ModelType modelType) {
    expression.append(modelType.name());
    expression.append(" ");
    return this;
  }

  public ModelQueryBuilder freeText(String freeText) {
    expression.append(freeText);
    expression.append(" ");
    return this;
  }

  public ModelQuery build() {
    return new ModelQuery(this.expression.toString());
  }
}
