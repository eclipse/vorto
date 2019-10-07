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
package org.eclipse.vorto.repository.core.impl.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import com.google.inject.Injector;

public class LocalModelWorkspace {

  private XtextResourceSet resourceSet;

  private Set<ModelId> modelIds = new HashSet<ModelId>();

  private IModelRepositoryFactory repoFactory;

  static {
    // injector = Guice.createInjector(Modules2.mixin(new DatatypeRuntimeModule(),
    // new FunctionblockRuntimeModule(),
    // new InformationModelRuntimeModule(),
    // new MappingRuntimeModule()));

  }

  public LocalModelWorkspace(IModelRepositoryFactory repositoryFactory, Collection<FileContent> files) {
    Injector injector = new MappingStandaloneSetup().createInjectorAndDoEMFRegistration();
    resourceSet = injector.getInstance(XtextResourceSet.class);
    resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
    resourceSet.addLoadOption(XtextResource.OPTION_ENCODING, "UTF-8");

    this.repoFactory = repositoryFactory;
    
    this.load(files);
  }
  
  public LocalModelWorkspace(IModelRepositoryFactory repositoryFactory) {
    this(repositoryFactory,Collections.emptyList());
  } 

  private LocalModelWorkspace load(Collection<FileContent> files) {
    this.modelIds.addAll(importExternallySpecifiedDependencies(files));
    return this;
  }

  private Collection<ModelId> importExternallySpecifiedDependencies(
      Collection<FileContent> dependencies) {
    return dependencies.stream().map(fileContent -> {
      Optional<Resource> maybeDependency =
          createResource(fileContent.getFileName(), fileContent.getContent(), resourceSet);
      return maybeDependency.flatMap(dependency -> {
        Model dependencyModel = (Model) dependency.getContents().get(0);
        if (dependencyModel.getName() != null && dependencyModel.getNamespace() != null
            && dependencyModel.getVersion() != null) {
          return Optional.of(new ModelId(dependencyModel.getName(), dependencyModel.getNamespace(),
              dependencyModel.getVersion()));
        }
        return Optional.empty();
      }).orElse(null);
    }).collect(Collectors.toList());
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
  
  public Set<ModelId> getModelIds() {
    return this.modelIds;
  }

  public void loadFromRepository(Collection<ModelId> modelIds) {
    Collection<ModelId> allReferences = modelIds;
    allReferences.removeAll(this.modelIds);
    allReferences.forEach(refModelId -> {
      try {
        repoFactory.getRepositoryByModel(refModelId)
            .getFileContent(refModelId, Optional.empty()).ifPresent(refFile -> {
              createResource(refFile.getFileName(), refFile.getContent(), resourceSet);
            });
      } catch (ModelNotFoundException notFoundException) {
        throw new ValidationException("Could not find reference "+refModelId.getPrettyFormat(), null);
      }
    });
    // add references, so that they are not looked up again
    this.modelIds.addAll(allReferences);
  }

  public XtextResourceSet getResourceSet() {
    return this.resourceSet;
  }
  
  

}
