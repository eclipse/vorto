/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 *
 *******************************************************************************/
package org.eclipse.vorto.codegen.api;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class Generated {
  public static final String ROOT_FOLDER_PATH = null;

  private String fileName;
  private String folderPath;
  private byte[] content;

  public Generated(String fileName, String folderPath, byte[] content) {
    this.fileName = fileName;
    this.folderPath = folderPath;
    this.content = content;
  }

  public Generated(String fileName, String folderPath, String content) {
    this(fileName, folderPath, content.getBytes());
  }

  public String getFileName() {
    return fileName;
  }

  public String getFolderPath() {
    return folderPath;
  }

  public byte[] getContent() {
    return content;
  }

  public boolean isDirectory() {
    return fileName == null;
  }
}
