/*******************************************************************************
 * Copyright (C) 2015 Bosch Software Innovations GmbH. All rights reserved.
 *******************************************************************************/

package org.eclipse.vorto.service.generator.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.ServiceClassifier;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl;
import org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.service.generator.web.utils.MappingZipFileExtractor;
import org.eclipse.vorto.service.generator.web.utils.ModelZipFileExtractor;
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
	
	@Value("${server.contextPath}")
	private String serverContextPath;
	
	@Value("${server.port}")
	private int serverPort;

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeGenerationController.class);

	@Value("${vorto.service.repositoryUrl}")
	private String basePath;

	@Autowired
	private IVortoCodeGenerator vortoGenerator;

	@RequestMapping(value = "/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> generate(@PathVariable String namespace,
			@PathVariable String name, @PathVariable String version) {

		byte[] modelResources = downloadModelWithReferences(namespace, name, version);
		
		ModelZipFileExtractor extractor = new ModelZipFileExtractor(modelResources);
		
		InformationModel infomodel = extractor.extract(new ModelId(ModelType.InformationModel, name, namespace, version));
		
		IGenerationResult result = vortoGenerator.generate(infomodel, resolveMappingContext(infomodel, vortoGenerator.getServiceKey()));
				
		return ResponseEntity.ok().contentLength(result.getContent().length)
				.header("content-disposition", "attachment; filename = " + result.getFileName())
				.contentType(MediaType.parseMediaType(result.getMediatype()))
				.body(new InputStreamResource(new ByteArrayInputStream(result.getContent())));
	}
	
	
	private InvocationContext resolveMappingContext(InformationModel model, String targetPlatform) {
		byte[] mappingResources = downloadMappingModel(model, targetPlatform);
		return new MappingZipFileExtractor(mappingResources).extract();
	}
	

	private byte[] downloadMappingModel(InformationModel model, String targetPlatform) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.getForObject(basePath + "/model/mapping/zip/{namespace}/{name}/{version}/{targetPlatform}",
					byte[].class, model.getNamespace(), model.getName(),model.getVersion(),targetPlatform);
		} catch (RestClientException e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private byte[] downloadModelWithReferences(String namespace, String name, String version) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.getForObject(basePath + "/model/zip/{namespace}/{name}/{version}?output=DSL",
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
