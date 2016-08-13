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
package org.eclipse.vorto.repository.internal.service.generation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.vorto.repository.model.GeneratedOutput;
import org.eclipse.vorto.repository.model.Generator;
import org.eclipse.vorto.repository.model.GeneratorServiceInfo;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.model.ModelType;
import org.eclipse.vorto.repository.model.ServiceClassifier;
import org.eclipse.vorto.repository.service.GenerationException;
import org.eclipse.vorto.repository.service.GeneratorAlreadyExistsException;
import org.eclipse.vorto.repository.service.IGeneratorService;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.service.ModelNotFoundException;
import org.modeshape.common.collection.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service
public class GenerationDelegateProxyService implements IGeneratorService {
	
	@Autowired
	private GeneratorLookupRepository registeredGeneratorsRepository;
	
	@Autowired
	private IModelRepository modelRepositoryService;
	
	
	private RestTemplate restTemplate;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GenerationDelegateProxyService.class);
	
	public GenerationDelegateProxyService() {
		this.restTemplate = new RestTemplate();
	}
	
	@Override
	public void registerGenerator(String serviceKey, String baseUrl, ServiceClassifier classifier) {
		if (!registeredGeneratorsRepository.findByGeneratorKey(serviceKey).isEmpty()) {
			throw new GeneratorAlreadyExistsException(serviceKey);
		} else {
			LOGGER.info("Registered generator {} under base url {} and classifier {}",serviceKey,baseUrl,classifier);
				
			this.registeredGeneratorsRepository.save(new Generator(serviceKey, baseUrl, classifier.name()));
		}
	}

	@Override
	public void unregisterGenerator(String serviceKey) {
		Generator generator = getGenerator(serviceKey);
		if (generator != null) {
			this.registeredGeneratorsRepository.delete(generator);
		}
	}

	@Override
	public Set<String> getRegisteredGeneratorServiceKeys(ServiceClassifier classifier) {
		Set<String> serviceKeys = new HashSet<>();
		for (Generator generator : this.registeredGeneratorsRepository.findByClassifier(classifier.name())) {
			serviceKeys.add(generator.getKey());
		}
		return Collections.unmodifiableSet(serviceKeys);
	}

	@Override
	public GeneratorServiceInfo getGeneratorServiceInfo(String serviceKey) {
		Generator generatorEntity = getGenerator(serviceKey);
		GeneratorServiceInfo generatorInfo = restTemplate.getForObject(generatorEntity.getGenerationInfoUrl(), GeneratorServiceInfo.class);
		generatorInfo.setGeneratorInfoUrl(generatorEntity.getGenerationInfoUrl());
		generatorInfo.performRating(generatorEntity.getInvocationCount());		
		return generatorInfo;
	}

	@Override
	public GeneratedOutput generate(ModelId modelId, String serviceKey) {
		ModelResource modelResource = modelRepositoryService.getById(modelId);
		if (modelResource == null) {
			throw new ModelNotFoundException("Model with the given ID does not exist",null);
		}
		if (modelResource.getModelType() == ModelType.Datatype || modelResource.getModelType() == ModelType.Mapping) {
			throw new GenerationException("Provided model is neither an information model nor a function block model!");
		}
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		Generator generatorEntity = getGenerator(serviceKey);
		if (generatorEntity == null) {
			throw new GenerationException("Generator with key "+serviceKey+" is not a registered generator");
		}
		generatorEntity.increaseInvocationCount();
		this.registeredGeneratorsRepository.save(generatorEntity);
		
		ResponseEntity<byte[]> entity = restTemplate.getForEntity(generatorEntity.getGenerationEndpointUrl(), byte[].class, modelId.getNamespace(), modelId.getName(), modelId.getVersion());
		return new GeneratedOutput(entity.getBody(), extractFileNameFromHeader(entity), entity.getHeaders().getContentLength());
	}
	
	private String extractFileNameFromHeader(ResponseEntity<byte[]> entity) {
		List<String> values = entity.getHeaders().get("content-disposition");
		if (values.size() > 0) {
			int indexOfFileNameStart = values.get(0).indexOf("=");
			return values.get(0).substring(indexOfFileNameStart+1);
		}
		return "generated.output";
	}

	private Generator getGenerator(String serviceKey) {
		List<Generator> generators = this.registeredGeneratorsRepository.findByGeneratorKey(serviceKey);
		if (!generators.isEmpty()) {
			return generators.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Collection<GeneratorServiceInfo> getMostlyUsedGenerators(int top) {
		List<Generator> topResult = new ArrayList<Generator>();

		for (Generator entity : this.registeredGeneratorsRepository.findByClassifier(ServiceClassifier.platform.name())) {
			topResult.add(entity);
		}
		
		topResult.sort(new Comparator<Generator>() {

			@Override
			public int compare(Generator o1, Generator o2) {
				if (o1.getInvocationCount() > o2.getInvocationCount()) {
					return -1;
				} else if (o1.getInvocationCount() < o2.getInvocationCount()) {
					return +1;
				} else {
					return 0;
				}
			}
		});
		
		List<GeneratorServiceInfo> result = new ArrayList<>(top);
		int counter = 0;
		for (Generator entity : topResult) {
			if (counter < top) {
				try {
					result.add(getGeneratorServiceInfo(entity.getKey()));
					counter++;
				} catch(Throwable t) {
					LOGGER.warn("Generator " + entity.getKey()+" appears to be offline or not deployed. Skipping...");
				}
				
			}
		}
		
		return result;
	}

}
