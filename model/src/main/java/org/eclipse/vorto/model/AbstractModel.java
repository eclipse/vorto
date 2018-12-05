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

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModel extends DefaultMappedElement implements IModel {

  protected ModelId id;
  protected ModelType type;
  protected String displayName;
  protected String description;
  protected String fileName;

  protected List<ModelId> references = new ArrayList<>();

  public AbstractModel(ModelId modelId, ModelType modelType) {
    this.id = modelId;
    this.type = modelType;
  }

  public AbstractModel() {

  }

  public ModelId getId() {
    return id;
  }

  public void setId(ModelId id) {
    this.id = id;
  }

  public ModelType getType() {
    return type;
  }

  public void setType(ModelType type) {
    this.type = type;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<ModelId> getReferences() {
    return references;
  }

  public void setReferences(List<ModelId> references) {
    this.references = references;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }



}
