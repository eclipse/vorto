/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.importer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.core.Attachment;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.impl.ITemporaryStorage;
import org.eclipse.vorto.repository.core.impl.StorageItem;
import org.eclipse.vorto.repository.core.impl.parser.ErrorMessageProvider;
import org.eclipse.vorto.repository.core.impl.parser.IModelParser;
import org.eclipse.vorto.repository.core.impl.parser.LocalModelWorkspace;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.DependencyManager;
import org.eclipse.vorto.repository.core.impl.utils.ModelValidationHelper;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.services.UserRepositoryRoleService;
import org.eclipse.vorto.repository.utils.ZipUtils;
import org.eclipse.vorto.repository.web.core.exceptions.BulkUploadException;
import org.modeshape.common.collection.Collections;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Extend this class for Implementation of a special importer for Vorto
 */
public abstract class AbstractModelImporter implements IModelImporter {

  public static final long TTL_TEMP_STORAGE_INSECONDS = 60 * 5;

  protected static Logger logger = Logger.getLogger(AbstractModelImporter.class);

  @Autowired
  private ITemporaryStorage uploadStorage;

  @Autowired
  protected IModelRepositoryFactory modelRepoFactory;

  @Autowired
  protected DefaultUserAccountService userAccountService;

  @Autowired
  protected ModelParserFactory modelParserFactory;

  @Autowired
  protected ModelValidationHelper modelValidationHelper;

  @Autowired
  protected ErrorMessageProvider errorMessageProvider;

  @Autowired
  protected UserRepositoryRoleService userRepositoryRoleService;

  private Set<String> supportedFileExtensions = new HashSet<>();

  protected static final String EXTENSION_ZIP = ".zip";

  public AbstractModelImporter(String modelTypeFileExtension, String... additionalExtensions) {
    if (handleZipUploads()) {
      supportedFileExtensions.add(EXTENSION_ZIP);
    }
    supportedFileExtensions.add(modelTypeFileExtension);
    supportedFileExtensions.addAll(Arrays.asList(additionalExtensions));
  }

  @Override
  public UploadModelResult upload(FileUpload fileUpload, Context context) {
    if (!this.supportedFileExtensions.contains(fileUpload.getFileExtension())) {
      return new UploadModelResult(null, Arrays.asList(ValidationReport.invalid(null,
          String.format("File type is invalid. Must be %s", this.supportedFileExtensions))));
    }
    List<ValidationReport> reports = new ArrayList<>();
    if (handleZipUploads() && isZipFile(fileUpload)) {
      getUploadedFilesFromZip(fileUpload.getContent()).stream().filter(this::isSupported)
          .forEach(extractedFile -> {
            logger.info(String.format("Validating uploaded file %s", extractedFile.getFileName()));
            extractedFile = preValidation(extractedFile, context);
            List<ValidationReport> validationResult = validate(extractedFile, context);
            postValidate(validationResult, context);
            reports.addAll(validationResult);
          });

    } else if (getSupportedFileExtensions().contains(fileUpload.getFileExtension())) {
      logger.info(String.format("Validating uploaded file %s", fileUpload.getFileName()));
      fileUpload = preValidation(fileUpload, context);
      List<ValidationReport> validationResult = validate(fileUpload, context);
      postValidate(validationResult, context);
      reports.addAll(validationResult);
    }

    logger.info("Upload completed");

    if (reports.size() == 0) {
      return new UploadModelResult(null, Arrays.asList(
          ValidationReport
              .invalid(String.format("Uploaded File does not contain any %s files.", getKey()))));
    } else if (reports.stream().filter(report -> !report.isValid()).count() == 0) {
      return new UploadModelResult(createUploadHandle(fileUpload), reports);
    } else {
      return new UploadModelResult(createUploadHandle(fileUpload), reports);
    }
  }

  protected FileUpload preValidation(FileUpload fileUpload, Context context) {
    return fileUpload;
  }

  private boolean isZipFile(FileUpload fileUpload) {
    return fileUpload.getFileExtension().equalsIgnoreCase(EXTENSION_ZIP);
  }

  protected boolean isSupported(FileUpload fileUpload) {
    return getSupportedFileExtensions().contains(fileUpload.getFileExtension());
  }

  protected Collection<FileUpload> getUploadedFilesFromZip(byte[] uploadedFile) {
    Collection<FileUpload> fileUploads = new ArrayList<>();

    ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(uploadedFile));
    ZipEntry entry = null;

    try {
      while ((entry = zis.getNextEntry()) != null) {
        if (!entry.isDirectory()
            && !entry.getName().substring(entry.getName().lastIndexOf("/") + 1).startsWith(".")) {
          fileUploads.add(FileUpload.create(entry.getName(), ZipUtils.copyStream(zis, entry)));
        }
      }
    } catch (IOException e) {
      throw new BulkUploadException("Problem while reading zip file during validation", e);
    }

    return fileUploads;
  }

  /**
   * Checks if the uploaded models already exist in the repository.
   *
   * @param reports reports from the specific importer implementation
   */
  protected void postValidate(List<ValidationReport> reports, Context context) {
    reports.forEach(report -> {
      if (report.getModel() != null) {
        try {
          IModelRepository modelRepository = modelRepoFactory
              .getRepositoryByModel(report.getModel().getId());
          ModelInfo m = modelRepository.getById(report.getModel().getId());
          if (m != null) {
            if (m.isReleased()) {
              report.setMessage(ValidationReport.ERROR_MODEL_ALREADY_RELEASED);
              report.setValid(false);
            } else {
              if (isAdmin(context.getUser()) || m.getAuthor()
                  .equals(context.getUser().getUsername())) {
                report.setMessage(ValidationReport.WARNING_MODEL_ALREADY_EXISTS);
                report.setValid(true);
              } else {
                report.setMessage(ValidationReport.ERROR_MODEL_ALREADY_EXISTS);
                report.setValid(false);
              }
            }
          }
        } catch (Exception e) {
          logger.error(
              String.format("Error while validating the model %s", report.getModel().getId()), e);
          report.setMessage(new StatusMessage(
              String.format("Internal error while trying to import model [%s]",
                  report.getModel().getId()),
              MessageSeverity.WARNING));
          report.setValid(false);
        }
      }

    });
  }

  private boolean isAdmin(IUserContext userContext) {
    User user = getUserAccountService().getUser(userContext);
    return Objects.nonNull(user) && userRepositoryRoleService.isSysadmin(user);
  }

  private String createUploadHandle(FileUpload fileUpload) {
    final String handleId = UUID.randomUUID().toString();
    return this.uploadStorage.store(handleId, fileUpload, TTL_TEMP_STORAGE_INSECONDS).getKey();
  }

  @Override
  public List<ModelInfo> doImport(String uploadHandleId, Context context) {
    StorageItem uploadedItem = this.uploadStorage.get(uploadHandleId);

    if (uploadedItem == null) {
      throw new ModelImporterException(
          String.format("No uploaded file found for handleId '%s'", uploadHandleId));
    }

    List<ModelInfo> importedModels = new ArrayList<>();

    try {

      if (handleZipUploads() && isZipFile(uploadedItem.getValue())) {

        getUploadedFilesFromZip(uploadedItem.getValue().getContent()).stream()
            .forEach(extractedFile -> {
              List<ModelResource> resources = this.convert(extractedFile, context);
              importedModels.addAll(sortAndSaveToRepository(resources, extractedFile, context));
            });

      } else {
        List<ModelResource> resources = this.convert(uploadedItem.getValue(), context);
        importedModels.addAll(sortAndSaveToRepository(resources, uploadedItem.getValue(), context));
      }
    } finally {
      this.uploadStorage.remove(uploadHandleId);
    }

    return importedModels;
  }

  private List<ModelInfo> sortAndSaveToRepository(List<ModelResource> resources,
      FileUpload extractedFile, Context context) {
    final IUserContext user = context.getUser();

    List<ModelInfo> savedModels = new ArrayList<>();
    DependencyManager dm = new DependencyManager();
    for (ModelResource resource : resources) {
      dm.addResource(resource);
    }

    dm.getSorted().forEach(resource -> {
      try {
        IModelRepository modelRepository = modelRepoFactory.getRepositoryByModel(resource.getId());
        ModelInfo importedModel = modelRepository.save((ModelResource) resource, user);
        savedModels.add(importedModel);
        postProcessImportedModel(importedModel,
            new FileContent(extractedFile.getFileName(), extractedFile.getContent()), user);

      } catch (Exception e) {
        logger.error("Problem importing model", e);
        throw new ModelImporterException("Problem importing model", e);
      }
    });

    return savedModels;
  }

  protected boolean handleZipUploads() {
    return true;
  }

  @Override
  public Set<String> getSupportedFileExtensions() {
    return Collections.unmodifiableSet(this.supportedFileExtensions);
  }

  protected ModelResource parseDSL(String fileName, byte[] content) {
    return parseDSL(fileName, content, java.util.Collections.emptyList());
  }

  protected ModelResource parseDSL(String fileName, byte[] content,
      Collection<FileContent> fileReferences) {
    IModelParser modelParser = modelParserFactory.getParser(fileName);
    modelParser.setWorkspace(new LocalModelWorkspace(modelRepoFactory, fileReferences));
    modelParser.enableValidation();
    return (ModelResource) modelParser.parse(new ByteArrayInputStream(content));
  }

  protected ModelResource parseDSL(ModelType type, byte[] content) {
    return parseDSL("model" + type.getExtension(), content, java.util.Collections.emptyList());
  }

  protected ModelResource parseDSL(ModelType type, byte[] content,
      Collection<FileContent> fileReferences) {
    return parseDSL("model" + type.getExtension(), content, fileReferences);
  }

  protected String createFileName(ModelInfo resource) {
    return resource.getId().getName() + resource.getType().getExtension();
  }

  protected void postProcessImportedModel(ModelInfo importedModel, FileContent originalFileContent,
      IUserContext user) {
    IModelRepository modelRepository = modelRepoFactory.getRepositoryByModel(importedModel.getId());
    modelRepository.attachFile(importedModel.getId(), originalFileContent, user,
        Attachment.TAG_IMPORTED);
    importedModel.setImported(true);
  }

  protected abstract List<ValidationReport> validate(FileUpload fileUpload, Context context);

  /**
   * converts the given file upload content to Vorto DSL content
   *
   * @param fileUpload
   * @return Vorto DSL content
   */
  protected abstract List<ModelResource> convert(FileUpload fileUpload, Context context);

  public void setUploadStorage(ITemporaryStorage uploadStorage) {
    this.uploadStorage = uploadStorage;
  }

  public void setUserAccountService(DefaultUserAccountService userAccountService) {
    this.userAccountService = userAccountService;
  }

  public void setModelParserFactory(ModelParserFactory modelParserFactory) {
    this.modelParserFactory = modelParserFactory;
  }

  public void setModelRepoFactory(IModelRepositoryFactory modelRepoFactory) {
    this.modelRepoFactory = modelRepoFactory;
  }

  public ITemporaryStorage getUploadStorage() {
    return uploadStorage;
  }

  public DefaultUserAccountService getUserAccountService() {
    return userAccountService;
  }

  public ModelParserFactory getModelParserFactory() {
    return modelParserFactory;
  }

  public IModelRepositoryFactory getModelRepoFactory() {
    return modelRepoFactory;
  }

  public ModelValidationHelper getModelValidationHelper() {
    return modelValidationHelper;
  }

  public void setModelValidationHelper(ModelValidationHelper modelValidationHelper) {
    this.modelValidationHelper = modelValidationHelper;
  }

}
