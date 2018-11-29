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
package org.eclipse.vorto.repository.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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


/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelResource extends ModelInfo {

  private Model model;

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
    resource.save(baos, null);
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
