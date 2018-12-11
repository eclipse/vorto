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
