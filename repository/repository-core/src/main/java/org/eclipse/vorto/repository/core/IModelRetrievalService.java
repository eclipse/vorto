package org.eclipse.vorto.repository.core;

import java.util.Map;
import java.util.Optional;
import org.eclipse.vorto.model.ModelId;

/**
 * This service is for retrieving models across tenants 
 * 
 * @author ERM1SGP
 *
 */
public interface IModelRetrievalService {
  /**
   * Returns the model, along with the tenant
   * 
   * @param modelId
   * @return
   */
  Optional<Map.Entry<String, ModelInfo>> getModel(ModelId modelId);
  
  /**
   * Returns the content of the model, along with the tenant
   * 
   * @param modelId
   * @return
   */
  Optional<Map.Entry<String, FileContent>> getContent(ModelId modelId);
}
