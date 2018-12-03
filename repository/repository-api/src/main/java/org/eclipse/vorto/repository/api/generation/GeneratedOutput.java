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
package org.eclipse.vorto.repository.api.generation;

import java.util.Arrays;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Deprecated
public class GeneratedOutput {

  private byte[] content;

  private String fileName;

  private long size;

  public GeneratedOutput(byte[] content, String fileName, long size) {
    this.content = content;
    this.fileName = fileName;
    this.size = size;
  }

  public byte[] getContent() {
    return content;
  }

  public String getFileName() {
    return fileName;
  }

  public long getSize() {
    return size;
  }

  @Override
  public String toString() {
    return "GeneratedOutput [content=" + Arrays.toString(content) + ", fileName=" + fileName
        + ", size=" + size + "]";
  }
}
