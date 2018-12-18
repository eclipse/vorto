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
