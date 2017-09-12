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
package org.eclipse.vorto.service.mapping.spec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.Functions;
import org.eclipse.vorto.repository.api.IModelRepository;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.repository.client.RepositoryClientBuilder;
import org.eclipse.vorto.service.mapping.IMappingSpecification;
import org.eclipse.vorto.service.mapping.converters.JavascriptFunctions;

public class RemoteMappingSpecification implements IMappingSpecification {

	private IModelRepository repositoryClient;
	
	private ModelId modelId;
	
	private String mappingKey; 
	
	private Infomodel infomodel;
	
	private static final String STEREOTYPE = "functions";
	
	private Map<ModelId, FunctionblockModel> fbs = new HashMap<ModelId, FunctionblockModel>();
	
	private FunctionLibrary library = new FunctionLibrary();
	private HashMap<String, JavascriptFunctions> namespaces = new HashMap<String, JavascriptFunctions>();
	
	public RemoteMappingSpecification(ModelId infoModelId, String mappingKey, String endpoint, Optional<String> proxyHost, Optional<Integer> proxyPort) {
		RepositoryClientBuilder builder = RepositoryClientBuilder.newBuilder().setBaseUrl(endpoint);
		if (proxyHost.isPresent() && proxyPort.isPresent()) {
			builder.setProxyHost(proxyHost.get());
			builder.setProxyPort(proxyPort.get());
		}
		this.repositoryClient = builder.buildModelRepositoryClient();
		this.modelId = infoModelId;
		this.mappingKey = mappingKey;
		
		fetchModels();
	}
	
	private void fetchModels() {
		try {
			this.infomodel = this.repositoryClient.getContent(this.modelId, Infomodel.class, this.mappingKey).get();
			if (this.infomodel == null) {
				throw new IllegalArgumentException("Information Model cannot be found with given model ID and/or key");
			}
			
			for (ModelProperty fbProperty : this.infomodel.getFunctionblocks()) {
				ModelId fbModelId = (ModelId)fbProperty.getType();
				FunctionblockModel fbm = this.repositoryClient.getContent(fbModelId, FunctionblockModel.class,this.mappingKey).get();
								
				if (fbm.getStereotype(STEREOTYPE).isPresent()) {
					JavascriptFunctions functions;
					Stereotype functionsStereotype = fbm.getStereotype(STEREOTYPE).get();
					String namespace = functionsStereotype.getAttributes().get("_namespace");
					if (namespaces.containsKey(namespace))
					{
						functions = namespaces.get(namespace);
					}
					else
					{
						functions = new JavascriptFunctions(namespace);
						this.library.addFunctions(functions);
					}
					
					for (String key : functionsStereotype.getAttributes().keySet()) {
						if (!"_namespace".equalsIgnoreCase(key)) {
							functions.addFunction(key,functionsStereotype.getAttributes().get(key));
						}
					}
				}
				this.fbs.put(fbModelId, fbm);
			}
		} catch (Exception e) {
			throw new MappingSpecificationProblem("Cannot create mapping specification", e);
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

	@Override
	public Optional<Functions> getCustomFunctions() {
		return Optional.ofNullable(this.library);
	}


}
