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
package org.eclipse.vorto.repository.importer;

public class FileUpload {

  private String fileName;

  private byte[] content;

  private FileUpload() {}

  public static FileUpload create(String filePath, byte[] content) {
    FileUpload fileUpload = new FileUpload();
    fileUpload.fileName = extractFileName(filePath);
    fileUpload.content = content;
    return fileUpload;
  }

  private static String extractFileName(String filePath) {
    return filePath.substring(filePath.lastIndexOf('/') + 1);
  }

  public String getFileName() {
    return fileName;
  }

  public byte[] getContent() {
    return content;
  }

  public String getFileExtension() {
    return fileName.substring(fileName.lastIndexOf("."));
  }
}
