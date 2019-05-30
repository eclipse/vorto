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
package org.eclipse.vorto.repository.plugin;

import org.eclipse.vorto.repository.importer.FileUpload;
import org.springframework.core.io.ByteArrayResource;

public class FileContentResource extends ByteArrayResource  {

  private String fileName;

  public FileContentResource(FileUpload fileUpload) {
      super(fileUpload.getContent(), "");
      this.fileName = fileUpload.getFileName();
  }

  @Override
  public String getFilename() {
      return fileName;
  }
}
