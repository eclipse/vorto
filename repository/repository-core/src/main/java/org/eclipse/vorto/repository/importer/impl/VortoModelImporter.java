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
package org.eclipse.vorto.repository.importer.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.refactor.ChangeSet;
import org.eclipse.vorto.model.refactor.RefactoringTask;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.BulkUploadHelper;
import org.eclipse.vorto.repository.core.impl.utils.ModelValidationHelper;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.importer.AbstractModelImporter;
import org.eclipse.vorto.repository.importer.FileUpload;
import org.eclipse.vorto.repository.importer.ValidationReport;
import org.eclipse.vorto.repository.web.core.exceptions.BulkUploadException;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.springframework.stereotype.Component;

/**
 * Imports (a bulk of) Vorto DSL Files to the Vorto Repository
 *
 */
@Component
public class VortoModelImporter extends AbstractModelImporter {

  private static Logger logger = Logger.getLogger(VortoModelImporter.class);

  public VortoModelImporter() {
    super(".infomodel", ".fbmodel", ".type", ".mapping", ".zip");

  }

  @Override
  public String getKey() {
    return "Vorto";
  }

  @Override
  public String getShortDescription() {
    return "Imports Vorto Informtion Models, defined with the Vorto DSL.";
  }

  protected boolean handleZipUploads() {
    return false;
  }

  /**
   * changes the namespace of the uploaded vorto model(s) , if target namespace is specified
   */
  @Override
  protected FileUpload preProcess(FileUpload fileUpload, Optional<String> targetNamespace) {
    if (targetNamespace.isPresent()) {
      if (fileUpload.getFileExtension().endsWith(EXTENSION_ZIP)) {
        ZipUploadFile zipFile = new ZipUploadFile(fileUpload.getFileName());
        
        getUploadedFilesFromZip(fileUpload.getContent()).stream().filter(this::isSupported)
            .forEach(extractedFile -> {
              zipFile.addToZip(refactor(extractedFile, targetNamespace.get()));
            });
              
        return zipFile.getFileUpload();
      } else {
        return refactor(fileUpload, targetNamespace.get());
      }
    } else {
      return fileUpload;
    }
  }

  private FileUpload refactor(FileUpload fileUpload, String targetNamespace) {
    IModelWorkspace workspace =
        IModelWorkspace.newReader().addFile(new ByteArrayInputStream(fileUpload.getContent()),
            ModelType.fromFileName(fileUpload.getFileName())).read();
    ChangeSet changeSet = RefactoringTask.from(workspace).toNamespace(targetNamespace).execute();
    ModelResource resource = new ModelResource(changeSet.get().get(0));
    try {
      return FileUpload.create(fileUpload.getFileName(), resource.toDSL());
    } catch (IOException e) {
      logger.error("Could not serialize model to DSL", e);
      return fileUpload;
    }
  }

  @Override
  protected List<ValidationReport> validate(FileUpload fileUpload, IUserContext user) {
    if (fileUpload.getFileExtension().equalsIgnoreCase(EXTENSION_ZIP)) {
      BulkUploadHelper bulkUploadService =
          new BulkUploadHelper(getModelRepoFactory(), getUserRepository(), getTenantService());
      return bulkUploadService.uploadMultiple(fileUpload.getContent(), fileUpload.getFileName(),
          user);
    } else {
      ModelValidationHelper validationHelper =
          new ModelValidationHelper(getModelRepoFactory(), getUserRepository(), getTenantService());
      try {
        final ModelInfo modelInfo = parseDSL(fileUpload.getFileName(), fileUpload.getContent());
        return Arrays.asList(validationHelper.validate(modelInfo, user));
      } catch (ValidationException ex) {
        return Arrays.asList(ValidationReport.invalid(null, ex));
      }
    }
  }

  @Override
  protected List<ModelResource> convert(FileUpload fileUpload, IUserContext user) {
    List<ModelResource> result = new ArrayList<ModelResource>();

    if (fileUpload.getFileExtension().equalsIgnoreCase(EXTENSION_ZIP)) {
      Collection<FileContent> fileContents = getFileContents(fileUpload);
      result.addAll(fileContents.stream().map(fileContent -> parseDSL(fileContent.getFileName(),
          fileContent.getContent(), fileContents)).collect(Collectors.toList()));
    } else {
      final ModelResource modelInfo =
          (ModelResource) getModelParserFactory().getParser(fileUpload.getFileExtension())
              .parse(new ByteArrayInputStream(fileUpload.getContent()));
      result.add(modelInfo);
    }

    return Collections.unmodifiableList(result);
  }

  private Collection<FileContent> getFileContents(FileUpload fileUpload) {
    Collection<FileContent> fileContents = new ArrayList<FileContent>();

    ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(fileUpload.getContent()));
    ZipEntry entry = null;

    try {
      while ((entry = zis.getNextEntry()) != null) {
        if (!entry.isDirectory() && ModelParserFactory.hasParserFor(entry.getName())) {
          fileContents.add(new FileContent(entry.getName(), copyStream(zis, entry)));
        }
      }
    } catch (IOException e) {
      throw new BulkUploadException("IOException while getting next entry from zip file", e);
    }

    return fileContents;
  }

  private static byte[] copyStream(ZipInputStream in, ZipEntry entry) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      int size;
      byte[] buffer = new byte[2048];

      BufferedOutputStream bos = new BufferedOutputStream(out);

      while ((size = in.read(buffer, 0, buffer.length)) != -1) {
        bos.write(buffer, 0, size);
      }

      bos.flush();
      bos.close();
    } catch (IOException e) {
      throw new BulkUploadException("IOException while copying stream to ZipEntry", e);
    }

    return out.toByteArray();
  }

  @Override
  protected void postProcessImportedModel(ModelInfo importedModel, FileContent originalFileContent,
      IUserContext user) {
    // no need to process the imported file further
  }

  private static class ZipUploadFile {

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private ZipOutputStream zos = new ZipOutputStream(baos);

    private String fileName;

    private ZipUploadFile(String fileName) {
      this.fileName = fileName;
    }

    public void addToZip(FileUpload file) {
      try {
        ZipEntry zipEntry = new ZipEntry(file.getFileName());
        zos.putNextEntry(zipEntry);
        zos.write(file.getContent());
        zos.closeEntry();
      } catch (Exception ex) {
        // entry possible exists already, so skipping TODO: ugly hack!!
      }
    }

    public FileUpload getFileUpload() {
      try {
        zos.close();
        baos.close();
      } catch (Exception ex) {
      }

      return FileUpload.create(fileName, baos.toByteArray());
    }
  }
}
