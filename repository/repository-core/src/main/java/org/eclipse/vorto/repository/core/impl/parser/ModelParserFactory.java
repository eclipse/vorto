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
package org.eclipse.vorto.repository.core.impl.parser;

import javax.annotation.PostConstruct;
import org.eclipse.vorto.core.api.model.datatype.DatatypePackage;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup;
import org.eclipse.vorto.editor.infomodel.InformationModelStandaloneSetup;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */

@Component
public class ModelParserFactory {

  private static ModelParserFactory instance;

  private boolean isInit = false;

  @Autowired
  private IModelRepository repository;

  @Autowired
  private ErrorMessageProvider errorMessageProvider;

  @PostConstruct
  public void init() {
    if (!isInit) {
      initDSLPackages();
    }
    ModelParserFactory.instance = this;
  }

  public void initDSLPackages() {
    DatatypePackage.eINSTANCE.eClass();
    FunctionblockPackage.eINSTANCE.eClass();
    InformationModelPackage.eINSTANCE.eClass();
    MappingPackage.eINSTANCE.eClass();

    FunctionblockStandaloneSetup.doSetup();
    InformationModelStandaloneSetup.doSetup();
    MappingStandaloneSetup.doSetup();

    this.isInit = true;
  }

  public IModelParser getParser(String fileName) {
    if (fileName.endsWith(ModelType.Datatype.getExtension())) {
      return new DatatypeModelParser(fileName, repository, errorMessageProvider);
    } else if (fileName.endsWith(ModelType.Functionblock.getExtension())) {
      return new FunctionblockModelParser(fileName, repository, errorMessageProvider);
    } else if (fileName.endsWith(ModelType.InformationModel.getExtension())) {
      return new InformationModelParser(fileName, repository, errorMessageProvider);
    } else if (fileName.endsWith(ModelType.Mapping.getExtension())) {
      return new MappingModelParser(fileName, repository, errorMessageProvider);
    } else {
      throw new UnsupportedOperationException("File cannot be parsed, because it is not supported");
    }
  }

  public static ModelParserFactory instance() {
    if (instance == null) {
      throw new ParsingException("Repository instance is not yet initialized.");
    }

    return instance;
  }

  public IModelRepository getRepository() {
    return repository;
  }

  public void setRepository(IModelRepository repository) {
    this.repository = repository;
  }

  public static boolean hasParserFor(String fileName) {
    return fileName.endsWith(ModelType.Datatype.getExtension())
        || fileName.endsWith(ModelType.Functionblock.getExtension())
        || fileName.endsWith(ModelType.InformationModel.getExtension())
        || fileName.endsWith(ModelType.Mapping.getExtension());
  }
}
