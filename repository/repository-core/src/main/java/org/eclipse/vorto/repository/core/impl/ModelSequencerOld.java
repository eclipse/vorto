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
package org.eclipse.vorto.repository.core.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.Binary;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.parser.IModelParser;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
import org.eclipse.vorto.repository.core.impl.utils.ModelReferencesHelper;
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
public class ModelSequencerOld extends Sequencer {

  @Override
  public void initialize(NamespaceRegistry registry, NodeTypeManager nodeTypeManager)
      throws RepositoryException, IOException {
    registerNodeTypes(new ClassPathResource("sequencer-model.cnd").getInputStream(),
        nodeTypeManager, true);
  }

  @Override
  public boolean execute(Property inputProperty, Node outputNode, Context context)
      throws Exception {
    Binary binaryValue = inputProperty.getBinary();
    CheckArg.isNotNull(binaryValue, "binary");
    ModelInfo modelResource = null;
    try {
      IModelParser parser = ModelParserFactory.instance().getParser(outputNode.getPath());
      
       modelResource = parser.parse(binaryValue.getStream());
       outputNode.setProperty("vorto:description",
           modelResource.getDescription() != null ? modelResource.getDescription() : "");
       outputNode.setProperty("vorto:type", modelResource.getType().name());

       outputNode.setProperty("vorto:displayname", modelResource.getDisplayName());
       outputNode.setProperty("vorto:version", modelResource.getId().getVersion());
       outputNode.setProperty("vorto:namespace", modelResource.getId().getNamespace());
       outputNode.setProperty("vorto:name", modelResource.getId().getName());

       if (outputNode.hasProperty("vorto:references")) { // first remove any previous references of the node.
         outputNode.getProperty("vorto:references").remove();
         outputNode.getSession().save();
       }
       
       ModelReferencesHelper referencesHelper =
           new ModelReferencesHelper(modelResource.getReferences());
       if (referencesHelper.hasReferences()) {
         List<Value> references = new ArrayList<Value>();
         for (ModelId modelId : referencesHelper.getReferences()) {
           ModelIdHelper modelIdHelper = new ModelIdHelper(modelId);
           Node referencedFolder = outputNode.getSession().getNode(modelIdHelper.getFullPath());
           Node reference = referencedFolder.getNodes().nextNode();
           references.add(context.valueFactory().createValue(reference));
         }
         outputNode.setProperty("vorto:references", references.toArray(new Value[references.size()]));
       }
    } catch(Throwable couldNotParse) {
      outputNode.setProperty("vorto:description","Erronous Model !!!!");
      outputNode.setProperty("vorto:type", ModelType.create(outputNode.getPath()).name());
      outputNode.setProperty("vorto:displayname", "");
      outputNode.setProperty("vorto:version", "1.0.0");
      outputNode.setProperty("vorto:namespace", "erronous");
      outputNode.setProperty("vorto:name", outputNode.getName());
    }
   
    return true;
  }
}
