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
package org.eclipse.vorto.codegen.api;

/**
 * Please use the Plugin SDK API instead
 */
@Deprecated
public class SingleGenerationResult implements IGeneratedWriter, IGenerationResult {

  private String fileName;
  private String mediatype;
  private byte[] content;

  SingleGenerationResult(String mediatype) {
    this.mediatype = mediatype;
  }

  public String getFileName() {
    return fileName;
  }

  public String getMediatype() {
    return mediatype;
  }

  public byte[] getContent() {
    return content;
  }

  public void write(Generated generated) {
    this.content = generated.getContent();
    this.fileName = generated.getFileName();
  }
}
