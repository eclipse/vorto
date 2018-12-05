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
