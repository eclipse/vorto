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
package org.eclipse.vorto.repository.web.importer.dto;

import org.eclipse.vorto.repository.importer.UploadModelResult;

/**
 * Generic server response type for client request. *
 */
public class UploadModelResponse {

  private String message = null;
  private UploadModelResult result = null;

  public UploadModelResponse() {

  }

  public UploadModelResponse(String message, UploadModelResult result) {
    this.message = message;
    this.result = result;
  }

  /**
   * Status message returned from server about request.
   **/
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public UploadModelResult getResult() {
    return result;
  }

  public void setResult(UploadModelResult result) {
    this.result = result;
  }

  @Override
  public String toString() {
    return "UploadModelResponse [message=" + message + ", result=" + result + "]";
  }

}
