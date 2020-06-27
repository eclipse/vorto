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
package org.eclipse.vorto.repository.core.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.jcr.Binary;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.impl.parser.IModelParser;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.modeshape.common.util.CheckArg;
import org.modeshape.jcr.api.nodetype.NodeTypeManager;
import org.modeshape.jcr.api.sequencer.Sequencer;
import org.springframework.core.io.ClassPathResource;

/**
 * Model Sequencer inspects the uploaded DSL model and extracts all information that is supposed to
 * be added as specific JCR properties and thus indexed for searching.
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelSequencer extends Sequencer {

  @Override
  public void initialize(NamespaceRegistry registry, NodeTypeManager nodeTypeManager)
      throws RepositoryException, IOException {
    registerNodeTypes(new ClassPathResource("sequencer-model.cnd").getInputStream(),
        nodeTypeManager, true);
  }

  @Override
  public boolean execute(Property inputProperty, Node fileNode, Context context)
      throws Exception {
    final Node folderNode = fileNode.getParent();
    
    Binary binaryValue = inputProperty.getBinary();
    CheckArg.isNotNull(binaryValue, "binary");
    IModelParser parser = ModelParserFactory.instance().getParser(fileNode.getPath());
    ModelInfo modelResource = parser.parse(binaryValue.getStream());

    fileNode.setProperty("vorto:description",
        modelResource.getDescription() != null ? modelResource.getDescription() : "");
    fileNode.setProperty("vorto:type", modelResource.getType().name());
    fileNode.setProperty("vorto:displayname", modelResource.getDisplayName());
    fileNode.setProperty("vorto:version", modelResource.getId().getVersion());
    fileNode.setProperty("vorto:namespace", modelResource.getId().getNamespace());
    fileNode.setProperty("vorto:name", modelResource.getId().getName());
    
    if (modelResource.getType() == ModelType.Mapping) {
      MappingModel mappingModel = (MappingModel) ((ModelResource)modelResource).getModel();
      fileNode.setProperty("vorto:targetplatform", mappingModel.getTargetPlatform());
    }
    
    folderNode.addMixin("mix:referenceable");
    folderNode.addMixin("vorto:meta");
    folderNode.setProperty("vorto:namespace", modelResource.getId().getNamespace());
    folderNode.setProperty("vorto:type", modelResource.getType().name());
    folderNode.setProperty("vorto:name", modelResource.getId().getName());


    if (folderNode.hasProperty("vorto:references")) { // first remove any previous references of the node.
      folderNode.getProperty("vorto:references").remove();
      folderNode.getSession().save();
    }
    
    Optional<Value[]> referencesAsValues = getReferencesAsValues(folderNode, modelResource.getReferences());
    if (referencesAsValues.isPresent()) {
      folderNode.setProperty("vorto:references", referencesAsValues.get());
    }
    
    return true;
  }
  
  private Optional<Value[]> getReferencesAsValues(Node folderNode, List<ModelId> references) throws RepositoryException {
    if (references != null && !references.isEmpty()) {
      List<Value> valueReferences = new ArrayList<Value>();
      for (ModelId modelId : references) {
        valueReferences.add(folderNode.getSession().getValueFactory().createValue(modelId.getPrettyFormat()));
      }
      return Optional.of(valueReferences.toArray(new Value[valueReferences.size()])); 
    }
    
    return Optional.empty();
  }
}
