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
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.ModelInfo;
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
public class ModelSequencer extends Sequencer {

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
    ModelInfo modelResource = ModelParserFactory.instance().getParser(outputNode.getPath())
        .parse(binaryValue.getStream());

    outputNode.setProperty("vorto:description",
        modelResource.getDescription() != null ? modelResource.getDescription() : "");
    outputNode.setProperty("vorto:type", modelResource.getType().name());

    outputNode.setProperty("vorto:displayname", modelResource.getDisplayName());
    outputNode.setProperty("vorto:version", modelResource.getId().getVersion());
    outputNode.setProperty("vorto:namespace", modelResource.getId().getNamespace());
    outputNode.setProperty("vorto:name", modelResource.getId().getName());

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
    return true;
  }
}
