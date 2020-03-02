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
package org.eclipse.vorto.repository.web.api.v1.dto;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.Attachment;
import org.springframework.web.multipart.MultipartFile;

/**
 * This POJO was originally created for the
 * {@link org.eclipse.vorto.repository.web.api.v1.AttachmentController}. <br/>
 * Its scope is now expanded to represent a status from an attempt to upload an attachment <i>or
 * an image</i> to a model. <br/>
 * Unfortunately, since it was designed for the {@link org.eclipse.vorto.repository.web.api.v1.AttachmentController}
 * whose API cannot be changed, controller endpoints using it cannot be refactored to wrap it into
 * a {@link org.springframework.http.ResponseEntity} and slowly harmonized with other controller
 * responses. <br/>
 * One solution would be to rethink this when designing the API v.2. <br/>
 * This is now used both in {@link org.eclipse.vorto.repository.web.api.v1.AttachmentController#attach(String, MultipartFile)}
 * and {@link org.eclipse.vorto.repository.web.core.ModelRepositoryController#uploadModelImage}.
 *
 */
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
