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
package org.eclipse.vorto.repository.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.resource.XtextResource;


/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelResource extends ModelInfo {

  private Model model;
  
  private static final Map<Object, Object> OPTIONS_DEFAULT = SaveOptions.newBuilder().format().getOptions().toOptionsMap();
  
  static {
    OPTIONS_DEFAULT.put(XtextResource.OPTION_ENCODING, StandardCharsets.UTF_8);
  }

  public ModelResource(Model model) {
    super(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
        createModelType(model));
    this.model = model;
  }

  private static ModelType createModelType(Model model) {
    if (model instanceof Type) {
      return ModelType.Datatype;
    } else if (model instanceof FunctionblockModel) {
      return ModelType.Functionblock;
    } else if (model instanceof InformationModel) {
      return ModelType.InformationModel;
    } else if (model instanceof MappingModel) {
      return ModelType.Mapping;
    } else {
      throw new UnsupportedOperationException(
          "Model of type " + model.getClass() + " cannot be parsed");
    }
  }

  public ModelId getId() {
    return new ModelId(model.getName(), model.getNamespace(), model.getVersion());
  }

  public String getDisplayName() {
    return model.getDisplayname() != null ? model.getDisplayname() : getId().getName();
  }

  public String getDescription() {
    return model.getDescription();
  }

  public List<ModelId> getReferences() {
    List<ModelId> references = new ArrayList<ModelId>(model.getReferences().size());
    for (ModelReference source : model.getReferences()) {
      references.add(ModelId.fromReference(source.getImportedNamespace(), source.getVersion()));
    }
    return references;
  }

  public byte[] toDSL() throws IOException {
    Resource resource = model.eResource().getResourceSet()
        .createResource(URI.createURI(model.getName() + this.type.getExtension()));
    resource.getContents().add(model);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    resource.save(baos, OPTIONS_DEFAULT);
    return baos.toByteArray();
  }

  public boolean matchesTargetPlatform(String targetPlatform) {
    return this.type == ModelType.Mapping
        && ((MappingModel) model).getTargetPlatform().equalsIgnoreCase(targetPlatform);
  }

  public String getTargetPlatform() {
    if (this.model instanceof MappingModel) {
      return ((MappingModel) this.model).getTargetPlatform();
    } else {
      return "";
    }
  }

  public Model getModel() {
    return this.model;
  }

}
