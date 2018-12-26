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
package org.eclipse.vorto.repository.core.impl.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.InvocationContext;
import org.eclipse.vorto.repository.core.impl.parser.IModelParser;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.validation.BulkModelDuplicateIdValidation;
import org.eclipse.vorto.repository.core.impl.validation.BulkModelReferencesValidation;
import org.eclipse.vorto.repository.core.impl.validation.DuplicateModelValidation;
import org.eclipse.vorto.repository.core.impl.validation.IModelValidator;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.importer.ValidationReport;
import org.eclipse.vorto.repository.web.core.exceptions.BulkUploadException;
import org.springframework.util.StringUtils;

public class BulkUploadHelper {

  private IModelRepository repositoryService;

  private IUserRepository userRepository;
  
  private IModelPolicyManager policyManager;

  public BulkUploadHelper(IModelRepository modelRepository, IModelPolicyManager policyManager, 
      IUserRepository userRepository) {
    this.repositoryService = modelRepository;
    this.policyManager = policyManager;
    this.userRepository = userRepository;
  }

  public List<ValidationReport> uploadMultiple(byte[] content, String zipFileName,
      IUserContext user) {
    if (!isValid(zipFileName)) {
      throw new FatalModelRepositoryException("Filename/type is invalid", null);
    }

    ZipParseResult parseResult = parseZipFile(content);

    try {
      /*
       * Create mapping function that will convert from a ModelInfo to an UploadModelResult using
       * validators
       */
      Function<ModelInfo, ValidationReport> convertToValidationReport =
          createConvertToUploadModelResultFn(constructBulkUploadValidators(parseResult.validModels),
              InvocationContext.create(user));

      /*
       * Convert parsed models to ValidationReport
       */
      Set<ValidationReport> validatedModelResults = parseResult.validModels.stream()
          .map(convertToValidationReport).collect(Collectors.toSet());

      /*
       * Add everything to a Set to eliminate redundancy
       */
      Set<ValidationReport> validationReports = new HashSet<>();
      validationReports.addAll(parseResult.invalidModels);
      validationReports.addAll(validatedModelResults);

      return new ArrayList<>(validationReports);

    } catch (Exception e) {
      throw new BulkUploadException("Invalid zip file", e);
    }
  }

  private Function<ModelInfo, ValidationReport> createConvertToUploadModelResultFn(
      List<IModelValidator> bulkUploadValidators, InvocationContext context) {
    return (modelInfo) -> {
      try {
        bulkUploadValidators.stream().forEach(validator -> validator.validate(modelInfo, context));
      } catch (ValidationException validationException) {
        return ValidationReport.invalid(modelInfo, validationException);
      }
      return ValidationReport.valid(modelInfo);
    };
  }

  private class ZipParseResult {
    Set<ValidationReport> invalidModels;
    Set<ModelInfo> validModels;
  }

  private ZipParseResult parseZipFile(byte[] content) {
    assert (content != null);

    ZipParseResult parsingResult = new ZipParseResult();

    parsingResult.invalidModels = new HashSet<>();
    parsingResult.validModels = new HashSet<>();

    Collection<FileContent> fileContents = getFileContentsFromZip(content);
    fileContents.forEach(fileContent -> {
      Collection<FileContent> possibleDependencies = fileContents.stream()
          .filter(file -> !file.equals(fileContent)).collect(Collectors.toList());
      try {
        IModelParser parser = ModelParserFactory.instance().getParser(fileContent.getFileName());
        parser.setReferences(possibleDependencies);
        parsingResult.validModels
            .add(parser.parse(new ByteArrayInputStream(fileContent.getContent())));
      } catch (ValidationException grammarProblem) {
        parsingResult.invalidModels.add(ValidationReport
            .invalid(trytoCreateModelFromCorruptFile(fileContent.getFileName()), grammarProblem));
      } catch (UnsupportedOperationException fileNotSupportedException) {
        // Do nothing. Don't process the file
      }
    });

    return parsingResult;
  }

  private Collection<FileContent> getFileContentsFromZip(byte[] content) {
    Collection<FileContent> fileContents = new ArrayList<FileContent>();

    ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(content));
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

  private ModelInfo trytoCreateModelFromCorruptFile(String fileName) {
    try {
      final String modelName = fileName.substring(0, fileName.lastIndexOf("."));
      final ModelType type = ModelType.fromFileName(fileName);
      return new ModelInfo(new ModelId(modelName, "unknown", "unknown"), type);
    } catch (Throwable t) {
      return null;
    }
  }

  private boolean isValid(String file) {
    return !StringUtils.isEmpty(file) && StringUtils.endsWithIgnoreCase(file, ".zip");
  }

  private List<IModelValidator> constructBulkUploadValidators(Set<ModelInfo> modelResources) {
    List<IModelValidator> bulkUploadValidators = new LinkedList<IModelValidator>();
    bulkUploadValidators
        .add(new DuplicateModelValidation(this.repositoryService, this.policyManager, this.userRepository));
    bulkUploadValidators
        .add(new BulkModelDuplicateIdValidation(this.repositoryService, modelResources));
    bulkUploadValidators
        .add(new BulkModelReferencesValidation(this.repositoryService, modelResources));
    return bulkUploadValidators;
  }

  protected static byte[] copyStream(ZipInputStream in, ZipEntry entry) {
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

}
