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
package org.eclipse.vorto.repository.web.api.v1.dto;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.Attachment;

public class AttachResult {
  private boolean success;
  private Attachment attachment;
  private String errorMessage;

  public static AttachResult success(ModelId modelId, String filename) {
    return new AttachResult(true, Attachment.newInstance(modelId, filename), null);
  }

  public static AttachResult fail(ModelId modelId, String filename, String errorMessage) {
    return new AttachResult(false, null, errorMessage);

  }

  public AttachResult(boolean success, Attachment attachment, String errorMessage) {
    this.success = success;
    this.attachment = attachment;
    this.errorMessage = errorMessage;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public Attachment getAttachment() {
    return attachment;
  }

  public void setAttachment(Attachment attachment) {
    this.attachment = attachment;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

}
