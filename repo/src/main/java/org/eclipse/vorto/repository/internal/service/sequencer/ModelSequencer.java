/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.repository.internal.service.sequencer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.eclipse.vorto.repository.internal.service.ModelParserFactory;
import org.eclipse.vorto.repository.internal.service.utils.ModelReferencesHelper;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.modeshape.common.util.CheckArg;
import org.modeshape.jcr.api.nodetype.NodeTypeManager;
import org.modeshape.jcr.api.sequencer.Sequencer;
import org.springframework.core.io.ClassPathResource;

/**
 * Sequencer which parses Vorto Models
 *
 */
public class ModelSequencer extends Sequencer {

	@Override
	public void initialize(NamespaceRegistry registry,
			NodeTypeManager nodeTypeManager) throws RepositoryException,
			IOException {
		registerNodeTypes(
				new ClassPathResource("sequencer-model.cnd").getInputStream(),
				nodeTypeManager, true);
	}

	@Override
	public boolean execute(Property inputProperty, Node outputNode,
			Context context) throws Exception {

		Binary binaryValue = inputProperty.getBinary();
		CheckArg.isNotNull(binaryValue, "binary");
		ModelResource modelResource = ModelParserFactory.getParser(outputNode.getPath()).parse(binaryValue.getStream());
		
		outputNode.setProperty("vorto:description", modelResource
				.getDescription() != null ? modelResource.getDescription()
				: "");
		outputNode.setProperty("vorto:type", modelResource.getModelType()
				.name());
		
		outputNode.setProperty("vorto:displayname", modelResource.getDisplayName());
		outputNode.setProperty("vorto:version", modelResource.getId().getVersion());
		outputNode.setProperty("vorto:namespace", modelResource.getId().getNamespace());
		outputNode.setProperty("vorto:name", modelResource.getId().getName());
		outputNode.setProperty("vorto:author", "admin");	
		
				
		ModelReferencesHelper referencesHelper = new ModelReferencesHelper(modelResource.getReferences());
		if (referencesHelper.hasReferences()) {
			List<Value> references = new ArrayList<Value>();
			for (ModelId modelId : referencesHelper.getReferences()) {
				Node referencedFolder = outputNode.getSession().getNode(modelId.getFullPath());
				Node reference = referencedFolder.getNodes().nextNode();
				references.add(context.valueFactory().createValue(reference));
			}
			outputNode.setProperty("vorto:references", references.toArray(new Value[references.size()]));
		}
		return true;
	}
}
