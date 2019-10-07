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
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
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
  private IModelRepositoryFactory modelRepoFactory;

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
      return new DatatypeModelParser(fileName,modelRepoFactory);
    } else if (fileName.endsWith(ModelType.Functionblock.getExtension())) {
      return new FunctionblockModelParser(fileName,modelRepoFactory);
    } else if (fileName.endsWith(ModelType.InformationModel.getExtension())) {
      return new InformationModelParser(fileName,modelRepoFactory);
    } else if (fileName.endsWith(ModelType.Mapping.getExtension())) {
      return new MappingModelParser(fileName, modelRepoFactory);
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

  public static boolean hasParserFor(String fileName) {
    return fileName.endsWith(ModelType.Datatype.getExtension())
        || fileName.endsWith(ModelType.Functionblock.getExtension())
        || fileName.endsWith(ModelType.InformationModel.getExtension())
        || fileName.endsWith(ModelType.Mapping.getExtension());
  }

  public void setModelRepositoryFactory(IModelRepositoryFactory modelRepoFactory) {
    this.modelRepoFactory = modelRepoFactory;
  }
}
