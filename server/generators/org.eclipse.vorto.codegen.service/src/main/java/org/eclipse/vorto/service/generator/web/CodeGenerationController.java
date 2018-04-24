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
package org.eclipse.vorto.service.generator.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.codegen.api.Generated;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.ServiceClassifier;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.server.commons.reader.IModelWorkspace;
import org.eclipse.vorto.server.commons.ui.IGeneratorConfigUITemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RequestMapping("/rest/generation")
@RestController
public class CodeGenerationController {
	
	@Value("${vorto.service.name}") 
	private String serviceName;
	
	@Value("${vorto.service.description}") 
	private String serviceDescription;
	
	@Value("${vorto.service.creator}") 
	private String serviceCreator;
	
	@Value("${vorto.service.documentationUrl}") 
	private String serviceDocumentationUrl;
	
	@Value("${vorto.service.image32x32}") 
	private String serviceIconSmall = "img/icon32x32.png";
	
	@Value("${vorto.service.image144x144}") 
	private String serviceIconBig = "img/icon144x144.png";;
	
	@Value("${vorto.service.classifier}")
	private ServiceClassifier classifier;

	@Value("${vorto.service.tags:#{null}}")
	private String[] tags;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CodeGenerationController.class);

	@Value("${vorto.service.repositoryUrl}")
	private String basePath;

	@Autowired
	private IVortoCodeGenerator vortoGenerator;
	
	@Autowired
	private IGeneratorConfigUITemplate configTemplate;
	
	@Autowired
	private ServerGeneratorLookup lookupService;
	
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> generate(@PathVariable String namespace,
			@PathVariable String name, @PathVariable String version, final HttpServletRequest request) {

		byte[] modelResources = downloadModelWithReferences(namespace, name, version);
		
		IModelWorkspace workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(new ByteArrayInputStream(modelResources))).read();
		
		Model model = workspace.get().stream().filter(p -> p.getName().equals(name)).findFirst().get();
		
		InformationModel infomodel = null;
		
		if (model instanceof InformationModel) {
			infomodel = (InformationModel)model;
		} else if (model instanceof FunctionblockModel) {
			infomodel = Utils.wrapFunctionBlock((FunctionblockModel)model);
		}
				
		IGenerationResult result = null;
		try {
			Map<String,String> requestParams = new HashMap<>();
			request.getParameterMap().entrySet().stream().forEach(x -> requestParams.put(x.getKey(), x.getValue()[0]));
			
			result = vortoGenerator.generate(infomodel, createInvocationContext(infomodel, vortoGenerator.getServiceKey(), requestParams),null);
		} catch (Exception e) {
			GenerationResultZip output = new GenerationResultZip(infomodel,vortoGenerator.getServiceKey());
			Generated generated = new Generated("generation_error.log", "/generated", e.getMessage());
			output.write(generated);
			result = output;
		}

		return ResponseEntity.ok().contentLength(result.getContent().length)
				.header("content-disposition", "attachment; filename = " + result.getFileName())
				.contentType(MediaType.parseMediaType(result.getMediatype()))
				.body(new InputStreamResource(new ByteArrayInputStream(result.getContent())));

	}
	
	private InvocationContext createInvocationContext(InformationModel model, String targetPlatform, Map<String, String> requestParams) {
		byte[] mappingResources = downloadMappingModel(model, targetPlatform);
		IModelWorkspace workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(new ByteArrayInputStream(mappingResources))).read();
		List<MappingModel> mappingModels = workspace.get().stream().filter(p -> p instanceof MappingModel)
													   .map(MappingModel.class::cast)
													   .collect(Collectors.toList());
		
		return new InvocationContext(mappingModels, lookupService, requestParams);
	}
	

	private byte[] downloadMappingModel(InformationModel model, String targetPlatform) {
		try {
			return restTemplate.getForObject(basePath + "/model/mapping/zip/{namespace}/{name}/{version}/{targetPlatform}",
					byte[].class, model.getNamespace(), model.getName(),model.getVersion(),targetPlatform);
		} catch (RestClientException e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private byte[] downloadModelWithReferences(String namespace, String name, String version) {
		try {
			return restTemplate.getForObject(basePath + "/model/file/{namespace}/{name}/{version}?output=DSL&includeDependencies=true",
					byte[].class, namespace, name, version);
		} catch (RestClientException e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratorServiceInfo getInfo() {
		GeneratorServiceInfo serviceInfo = new GeneratorServiceInfo();
		serviceInfo.setCreator(serviceCreator);
		serviceInfo.setDescription(serviceDescription);
		serviceInfo.setDocumentationUrl(serviceDocumentationUrl);
		serviceInfo.setImage144x144(encodeToBase64(this.serviceIconBig));
		serviceInfo.setImage32x32(encodeToBase64(this.serviceIconSmall));
		serviceInfo.setKey(this.vortoGenerator.getServiceKey());
		serviceInfo.setName(serviceName);
		serviceInfo.setClassifier(classifier);
		serviceInfo.setTags(tags);
		serviceInfo.setConfigTemplate(this.configTemplate.getContent(serviceInfo));
		serviceInfo.setConfigKeys(this.configTemplate.getKeys());
		return serviceInfo;
	}
	
	private String encodeToBase64(String image) {
		try {
			return Base64Utils.encodeToString(IOUtils.toByteArray(new ClassPathResource(image).getInputStream()));
		} catch(IOException ex) {
			LOGGER.error("Could not encode image {}",image,ex);
			return null;
		}
	}
	
	@PostConstruct
	public void init() {
		DatatypePackageImpl.init();
		FunctionblockPackageImpl.init();
		InformationModelPackageImpl.init();
	}

}
