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
