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
package org.eclipse.vorto.repository.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.vorto.model.AbstractModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelInfo extends AbstractModel {

  protected String author;
  protected Date creationDate;
  protected Date modificationDate;
  protected String lastModifiedBy;
  protected boolean hasImage = false;
  protected String state;
  protected Boolean imported = false;
  protected String visibility = "private";
  protected List<ModelId> referencedBy = new ArrayList<ModelId>();

  protected Map<String,String> platformMappings = new HashMap<>();

  public ModelInfo(ModelId modelId, ModelType modelType) {
    super(modelId, modelType);
  }

  public ModelInfo(ModelId modelId, String type) {
    this(modelId, ModelType.valueOf(type));
  }

  public ModelInfo() {}

  public List<ModelId> getReferencedBy() {
    return referencedBy;
  }

  public void setReferencedBy(List<ModelId> referencedBy) {
    this.referencedBy = referencedBy;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Date getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(Date modificationDate) {
    this.modificationDate = modificationDate;
  }

  public Map<String,String> getPlatformMappings() {
    return this.platformMappings;
  }

  public void setPlatformMappings(Map<String,String> platformMappings) {
    this.platformMappings = platformMappings;
  }

  public boolean isHasImage() {
    return hasImage;
  }

  public void setHasImage(boolean hasImage) {
    this.hasImage = hasImage;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
  
  public String getLastModifiedBy() {
    return lastModifiedBy;
  }
  
  public String getVisibility() {
    return visibility;
  }

  public void setVisibility(String visibility) {
    this.visibility = visibility;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  @Override
  public String toString() {
    return "ModelInfo [ id =" + id + ", type=" + type + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ModelInfo other = (ModelInfo) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;

    return (type == other.type);
  }

  public void addPlatformMapping(String targetPlatform, ModelId mappingId) {
    if (targetPlatform != null && !targetPlatform.equals("")) {
      this.platformMappings.put(mappingId.getPrettyFormat(),targetPlatform);
    }
  }

  public void addReferencedBy(ModelId id) {
    this.referencedBy.add(id);
  }

  public boolean isReleased() {
    return "released".equalsIgnoreCase(this.state) || "deprecated".equalsIgnoreCase(this.state);
  }

  public Boolean getImported() {
    return imported;
  }

  public void setImported(Boolean imported) {
    this.imported = imported;
  }

}
