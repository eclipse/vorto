/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.service.mapping.loader;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.vorto.repository.api.IModelRepository;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.client.RepositoryClientBuilder;
import org.eclipse.vorto.service.mapping.IModelLoader;

public class RepositoryLoader implements IModelLoader {

	private IModelRepository repositoryClient;
	
	private ModelId modelId;
	
	private String mappingKey; 
	
	private Infomodel infomodel;
	
	private Map<ModelId, FunctionblockModel> fbs = new HashMap<ModelId, FunctionblockModel>();
	
	public RepositoryLoader(ModelId infoModelId, String mappingKey) {
		this.repositoryClient = RepositoryClientBuilder.newBuilder().setBaseUrl("http://vorto.eclipse.org").buildModelRepositoryClient();
		this.modelId = infoModelId;
		this.mappingKey = mappingKey;
		
		fetchModels();
	}
	
	private void fetchModels() {
		try {
			this.infomodel = this.repositoryClient.getContent(this.modelId, Infomodel.class, this.mappingKey).get();
			for (ModelProperty fbProperty : this.infomodel.getFunctionblocks()) {
				ModelId fbModelId = (ModelId)fbProperty.getType();
				this.fbs.put(fbModelId, this.repositoryClient.getContent(fbModelId, FunctionblockModel.class,this.mappingKey).get());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
	}

	@Override
	public Infomodel getInfoModel() {
		return this.infomodel;
	}

	@Override
	public FunctionblockModel getFunctionBlock(ModelId modelId) {
		return fbs.get(modelId);
	}


}
