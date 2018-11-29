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
package org.eclipse.vorto.codegen.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * Generation Result that stores all generated files as a zip archieve
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class GenerationResultZip implements IGeneratedWriter, IGenerationResult {

  private ZipOutputStream zos;
  private ByteArrayOutputStream baos;

  private String fileName;

  public GenerationResultZip(String fileName) {
    this.fileName = fileName;
    baos = new ByteArrayOutputStream();
    this.zos = new ZipOutputStream(baos);
  }

  public GenerationResultZip(InformationModel infomodel, String suffix) {
    this(infomodel.getNamespace() + "_" + infomodel.getName() + "_" + infomodel.getVersion() + "-"
        + suffix + ".zip");
  }

  public void write(Generated generated) {
    ZipEntry zipEntry;
    if (generated.getFolderPath() == null || generated.getFolderPath().isEmpty()) {
      zipEntry = new ZipEntry(generated.getFileName());
    } else {
      zipEntry = new ZipEntry(
          cleanDirectoryPath(generated.getFolderPath()) + "/" + generated.getFileName());
    }

    try {
      zos.putNextEntry(zipEntry);
      zos.write(generated.getContent());
      zos.closeEntry();
    } catch (Exception e) {
      // possibly occurs if generated output already appears as zipentry, in this case skip and
      // continue. TODO: Ugly :(
    }
  }

  private String cleanDirectoryPath(String outputDirectory) {
    String _outputDirectory = outputDirectory;
    if (outputDirectory.charAt(outputDirectory.length() - 1) == '/') {
      _outputDirectory = outputDirectory.substring(0, outputDirectory.length() - 1);
    }

    if (_outputDirectory.charAt(0) == '/') {
      _outputDirectory = _outputDirectory.substring(1);
    }

    return _outputDirectory;
  }

  @Override
  public byte[] getContent() {
    try {
      zos.close();
      baos.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return baos.toByteArray();
  }

  public String getFileName() {
    return fileName;
  }

  public String getMediatype() {
    return "application/zip";
  }
}
