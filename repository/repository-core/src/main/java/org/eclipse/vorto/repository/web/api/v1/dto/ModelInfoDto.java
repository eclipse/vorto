package org.eclipse.vorto.repository.web.api.v1.dto;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.core.ModelInfo;

public class ModelInfoDto extends ModelInfo {

  private String tenantId;
  
  public ModelInfoDto(ModelId modelId, ModelType modelType) {
    super(modelId, modelType);
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }
}
