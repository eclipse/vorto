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
package org.eclipse.vorto.repository.core.indexing;

import java.util.Map;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.elasticsearch.search.SearchHit;

public class IndexedModelInfo {
  private String tenantId;
  private String modelId;
  private ModelType type;
  private String displayName;
  private String description;
  private String author;
  private String state;
  private String visibility;

  public static IndexedModelInfo fromSearchHit(SearchHit searchHit) {
    IndexedModelInfo modelInfo = new IndexedModelInfo();
    
    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
    
    modelInfo.setTenantId((String) sourceAsMap.get("tenantId"));
    modelInfo.setModelId((String) sourceAsMap.get("modelId"));
    modelInfo.setType(ModelType.valueOf((String) sourceAsMap.get("modelType")));
    modelInfo.setState((String) sourceAsMap.get("state"));
    modelInfo.setVisibility((String) sourceAsMap.get("visibility"));
    modelInfo.setAuthor((String) sourceAsMap.get("author"));
    modelInfo.setDescription((String) sourceAsMap.get("description"));
    modelInfo.setDisplayName((String) sourceAsMap.get("displayName"));
    
    return modelInfo;
  }
  
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getModelId() {
    return modelId;
  }

  public void setModelId(String modelId) {
    this.modelId = modelId;
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

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getVisibility() {
    return visibility;
  }

  public void setVisibility(String visibility) {
    this.visibility = visibility;
  }
  
  public ModelId getId() {
    return ModelId.fromPrettyFormat(modelId);
  }

  @Override
  public String toString() {
    return "IndexedModelInfo [tenantId=" + tenantId + ", modelId=" + modelId + ", type=" + type
        + ", displayName=" + displayName + ", description=" + description + ", author=" + author
        + ", state=" + state + ", visibility=" + visibility + "]";
  }
}
