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
package org.eclipse.vorto.repository.core.impl.parser;

import com.google.common.base.Strings;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.impl.validation.CouldNotResolveReferenceException;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import com.google.inject.Injector;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public abstract class AbstractModelParser implements IModelParser {

  private String fileName;
  private LocalModelWorkspace workspace;
  private ErrorMessageProvider errorMessageProvider;
  private boolean isValidationEnabled = false;

  public AbstractModelParser(String fileName, IModelRepositoryFactory repositoryFactory) {
    this.fileName = fileName;
    this.workspace = new LocalModelWorkspace(repositoryFactory);
  }

  @Override
  public ModelInfo parse(InputStream is) {
    Injector injector = getInjector();

    XtextResourceSet resourceSet = workspace.getResourceSet();

    Resource resource = createResource(fileName, getContent(is), resourceSet)
        .orElseThrow(() -> new ValidationException(
            "Xtext is not able to create a resource for this model. Check if you are using the correct parser.",
            getModelInfoFromFilename(fileName)));

    if (resource.getContents().size() <= 0) {
      throw new ValidationException(
          "Xtext is not able to create a model out of this file. Check if the file you are using is correct.",
          getModelInfoFromFilename(fileName));
    }

    Model model = (Model) resource.getContents().get(0);

    workspace.loadFromRepository(
        getReferences(model)); // always load direct references to make the model complete

    if (this.isValidationEnabled) {
      /* Execute validators */
      IResourceValidator validator = injector.getInstance(IResourceValidator.class);
      List<Issue> issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
      if (issues.size() > 0) {
        List<ModelId> missingReferences = getMissingReferences(model, issues);
        if (missingReferences.size() > 0) {
          throw new CouldNotResolveReferenceException(
              getModelInfo(model, fileName).orElse(getModelInfoFromFilename(fileName)),
              missingReferences);
        } else {
          Set<ValidationIssue> validationIssues = convertIssues(issues);
          throw new ValidationException(collate(validationIssues), validationIssues,
              getModelInfo(model, fileName).orElse(getModelInfoFromFilename(fileName)));
        }
      }

      if (!resource.getErrors().isEmpty()) {
        throw new ValidationException(resource.getErrors().get(0).getMessage(),
            getModelInfo(model, fileName).orElse(getModelInfoFromFilename(fileName)));
      }
    }

    // adds Vortolang version validation - will throw ValidationException if the Vortolang version
    // is in the supported format, but not supported as a value
    SupportedVortolangVersions.validate(model, fileName);

    // validating model properties after other validations
    validateModel(model, fileName);

    return new ModelResource(model);
  }

  /**
   * Checks that crucial model fields have a value. <br/>
   * If any doesn't, throws {@link ValidationException}.<br/>
   * The exception thrown can have suppressed {@link ValidationException}s for any field missing.
   *
   * @param model
   * @param fileName
   * @throws ValidationException
   */
  public static final void validateModel(Model model, String fileName) throws ValidationException {
    ModelInfo modelInfo = AbstractModelParser.getModelInfo(model, fileName)
        .orElse(AbstractModelParser.getModelInfoFromFilename(fileName));
    ValidationException root = new ValidationException("Cannot parse model", modelInfo);
    boolean valid = true;
    if (Strings.isNullOrEmpty(model.getLang())) {
      valid = false;
      root.addSuppressed(new ValidationException("Cannot parse Vortolang version", modelInfo));
    }
    if (Strings.isNullOrEmpty(model.getName())) {
      valid = false;
      root.addSuppressed(new ValidationException("Cannot parse model name", modelInfo));
    }
    if (Strings.isNullOrEmpty(model.getNamespace())) {
      valid = false;
      root.addSuppressed(new ValidationException("Cannot parse model namespace", modelInfo));
    }
    if (Strings.isNullOrEmpty(model.getVersion())) {
      valid = false;
      root.addSuppressed(new ValidationException("Cannot parse model version", modelInfo));
    }
    if (!valid) {
      throw root;
    }
  }

  protected Collection<ModelId> getReferences(Model model) {
    return model.getReferences().stream().map(
        modelRef -> ModelId.fromReference(modelRef.getImportedNamespace(), modelRef.getVersion()))
        .collect(Collectors.toList());
  }

  public IModelParser setWorkspace(LocalModelWorkspace workspace) {
    this.workspace = workspace;
    return this;
  }

  public IModelParser enableValidation() {
    this.isValidationEnabled = true;
    return this;
  }

  private Set<ValidationIssue> convertIssues(List<Issue> issues) {
    return issues.stream().map(issue -> {
      if (errorMessageProvider != null) {
        return new ValidationIssue(issue.getLineNumber(),
            errorMessageProvider.convertError(issue.getMessage()));
      }
      return new ValidationIssue(issue.getLineNumber(), issue.getMessage());
    }).collect(Collectors.toSet());
  }

  private List<ModelId> getMissingReferences(Model model, List<Issue> issues) {
    return issues.stream().collect(ArrayList<ModelId>::new, (acc, issue) -> {
      if (issue.getCode() != null
          && issue.getCode().equals("org.eclipse.xtext.diagnostics.Diagnostic.Linking")) {
        getName(issue.getMessage()).flatMap(name -> getModelId(model, name))
            .ifPresent(modelId -> acc.add(modelId));
      }
    }, (list1, list2) -> {
      list1.addAll(list2);
    });
  }

  private Optional<ModelId> getModelId(Model model, String name) {
    return model.getReferences().stream().map(
        modelRef -> ModelId.fromReference(modelRef.getImportedNamespace(), modelRef.getVersion()))
        .filter(modelId -> modelId.getName().equals(name)).findFirst();
  }

  private Optional<String> getName(String message) {
    String[] words = message.split("\\s+");
    if (words.length <= 0) {
      return Optional.empty();
    }
    String dirtyName = words[words.length - 1];
    return Optional.ofNullable(dirtyName.replaceAll("'", "").replaceAll("\\.", ""));
  }

  public static Optional<ModelInfo> getModelInfo(Model model, String fileName) {
    if (model == null || model.getName() == null || model.getNamespace() == null
        || model.getVersion() == null) {
      return Optional.empty();
    }

    return Optional
        .of(new ModelInfo(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
            ModelType.fromFileName(fileName)));
  }

  private String collate(Set<ValidationIssue> issues) {
    StringBuffer error = new StringBuffer();
    for (ValidationIssue issue : issues) {
      error.append(issue);
    }
    return error.toString();
  }

  private Optional<Resource> createResource(String fileName, byte[] fileContent,
      XtextResourceSet resourceSet) {
    Objects.requireNonNull(fileName);
    Objects.requireNonNull(fileContent);
    Objects.requireNonNull(resourceSet);

    String filename = "file-" + UUID.randomUUID().toString().replace("-", "") + "-" + fileName;

    Resource resource = resourceSet.createResource(URI.createURI("dummy:/" + filename));
    if (resource != null) {
      try {
        resource.load(new ByteArrayInputStream(fileContent), resourceSet.getLoadOptions());
        return Optional.of(resource);
      } catch (IOException e) {
        throw new ValidationException(e.getMessage(), null);
      }
    }

    return Optional.empty();
  }

  private byte[] getContent(InputStream is) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      IOUtils.copy(is, baos);
    } catch (IOException e1) {
      throw new ParsingException("Error while converting stream to array", e1);
    }

    return baos.toByteArray();
  }

  public static ModelInfo getModelInfoFromFilename(String fileName) {
    return new ModelInfo(parseModelIdFromFileName(fileName), ModelType.fromFileName(fileName));
  }

  public static ModelId parseModelIdFromFileName(String fileName) {
    String pureFileName =
        fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf("."));
    ModelId modelId = new ModelId();
    try {
      modelId.setNamespace(pureFileName.substring(0, pureFileName.lastIndexOf(".")));
      modelId.setName(
          pureFileName.substring(pureFileName.lastIndexOf(".") + 1, pureFileName.indexOf("_")));

      String version = pureFileName.substring(pureFileName.indexOf("_") + 1);
      version = version.replaceAll("_", ".");
      modelId.setVersion(version.substring(0, 5));
    } catch (Throwable t) {
      return new ModelId(pureFileName, "", "0.0.0");
    }
    return modelId;
  }

  protected abstract Injector getInjector();

}
