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
package org.eclipse.vorto.model;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Infomodel.class, name = "infomodel"),
    @JsonSubTypes.Type(value = FunctionblockModel.class, name = "functionblock"),
    @JsonSubTypes.Type(value = EntityModel.class, name = "entity"),
    @JsonSubTypes.Type(value = EnumModel.class, name = "enum")
})
public interface IModel {

  /**
   * unique id of the model
   * 
   * @return
   */
  ModelId getId();

  /**
   * Type of model
   * 
   * @return
   */
  ModelType getType();

  /**
   * Gets the display name of the model as described in the vorto model, can be null
   * 
   * @return
   */
  String getDisplayName();

  /**
   * Gets the description of the model as described in the vorto model, can be null
   * 
   * @return
   */
  String getDescription();

  /**
   * Gets all model ids that the model depends on, never null
   * 
   * @return
   */
  Collection<ModelId> getReferences();
}
