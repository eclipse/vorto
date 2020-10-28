/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.server.ui.util;

import java.util.Objects;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.server.ui.SeleniumVortoHelper;

/**
 * Utility builder with model creation parametrization
 */
public class CreateModelParams {

  private String name;
  private ModelType type;
  private String namespaceName;

  public static final CreateModelParams DEFAULT = new CreateModelParams(
      SeleniumVortoHelper.USER1_EMPTY_INFO_MODEL,
      ModelType.InformationModel.name(),
      SeleniumVortoHelper.PRIVATE_NAMESPACE_PREFIX + SeleniumVortoHelper.USER1_PRIVATE_NAMESPACE
  );

  public CreateModelParams() {
  }

  public CreateModelParams(String name, String type, String namespaceName) {
    this.name = name;
    this.type = ModelType.valueOf(type);
    if (Objects.isNull(this.type)) {
      throw new IllegalArgumentException(String.format("Invalid model type [%s]", type));
    }
    this.namespaceName = namespaceName;
  }

  public static CreateModelParams defaults() {
    return DEFAULT;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ModelType getType() {
    return type;
  }

  public void setType(ModelType type) {
    this.type = type;
  }

  public String getNamespaceName() {
    return namespaceName;
  }

  public void setNamespaceName(String namespaceName) {
    this.namespaceName = namespaceName;
  }
}
