/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
