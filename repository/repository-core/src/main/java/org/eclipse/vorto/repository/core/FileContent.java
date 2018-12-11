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
package org.eclipse.vorto.repository.core;

public class FileContent {

  private String fileName;
  private byte[] content;
  private long size;


  public FileContent(String fileName, byte[] content) {
    this.fileName = fileName;
    this.content = content;
    this.size = content.length;
  }

  public FileContent(String fileName, byte[] content, long size) {
    this.fileName = fileName;
    this.content = content;
    this.size = size;
  }

  public String getFileName() {
    return fileName;
  }

  public byte[] getContent() {
    return content;
  }

  public long getSize() {
    return size;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
    result = prime * result + (int) (size ^ (size >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FileContent other = (FileContent) obj;
    if (fileName == null) {
      if (other.fileName != null)
        return false;
    } else if (!fileName.equals(other.fileName))
      return false;
    return (size == other.size);
  }

}
