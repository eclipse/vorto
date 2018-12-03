package org.eclipse.vorto.repository.core;

import org.eclipse.vorto.model.ModelId;

public class AttachmentException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private ModelId modelId;

  public AttachmentException(ModelId modelId, String message) {
    super(message);
    this.modelId = modelId;
  }

  public AttachmentException(ModelId modelId, Throwable throwable) {
    super(throwable);
    this.modelId = modelId;
  }

  public ModelId getModelId() {
    return modelId;
  }

}
