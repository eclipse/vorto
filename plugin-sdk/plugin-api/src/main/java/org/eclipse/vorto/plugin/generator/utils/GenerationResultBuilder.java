/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.plugin.generator.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.eclipse.vorto.plugin.generator.IGenerationResult;

public class GenerationResultBuilder {

  private IGenerationResult result;

  private GenerationResultBuilder(IGenerationResult result) {
    this.result = result;
  }

  public static GenerationResultBuilder from(IGenerationResult result) {
    GenerationResultBuilder builder = new GenerationResultBuilder(result);
    return builder;
  }

  public GenerationResultBuilder append(IGenerationResult result) {
    if (result == null) {
      return this;
    }

    appendToResult(result.getContent(), ((IGeneratedWriter) result));

    return this;
  }

  public IGenerationResult build() {
    return result;
  }

  private void appendToResult(byte[] content, IGeneratedWriter result) {
    ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(content));

    try {

      ZipEntry ze = null;
      while ((ze = zis.getNextEntry()) != null) {
        if (ze.isDirectory()) {
          result.write(new Generated(null, ze.getName(), new byte[0]));
        } else {
          int indexOfLastSlash = ze.getName().lastIndexOf("/");
          String fileName = null;
          String folderName = null;
          if (indexOfLastSlash > -1) {
            fileName = ze.getName().substring(indexOfLastSlash + 1);
            folderName = ze.getName().substring(0, indexOfLastSlash);
          } else {
            fileName = ze.getName();
          }

          result.write(new Generated(fileName, folderName, new String(copyStream(zis), "utf-8")));
        }
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    } finally {
      if (zis != null) {
        try {
          zis.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private byte[] copyStream(InputStream in) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    int size;
    byte[] buffer = new byte[2048];

    BufferedOutputStream bos = new BufferedOutputStream(out);

    while ((size = in.read(buffer, 0, buffer.length)) != -1) {
      bos.write(buffer, 0, size);
    }
    bos.flush();
    bos.close();
    return out.toByteArray();
  } 
}
