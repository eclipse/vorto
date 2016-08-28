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
package org.eclipse.vorto.repository.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.model.GeneratedOutput;
import org.eclipse.vorto.repository.model.GeneratorServiceInfo;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ServiceClassifier;
import org.eclipse.vorto.repository.service.IGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value="/generate", description="Generate code from vorto models")
@RestController
@RequestMapping(value = "/rest/generation-router")
public class ModelGenerationController extends RepositoryController {
	
	private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String CONTENT_DISPOSITION = "content-disposition";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ModelGenerationController.class);

	@Autowired
	private IGeneratorService generatorService;
	
	@ApiOperation(value = "Generate code for a specified platform")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found")})
	@RequestMapping(value = "/{namespace}/{name}/{version:.+}/{serviceKey}", method = RequestMethod.GET)
	public void generate( 	@ApiParam(value = "The namespace of vorto model, e.g. com.mycompany", required = true) final @PathVariable String namespace, 
							@ApiParam(value = "The name of vorto model, e.g. NewInfomodel", required = true) final @PathVariable String name,
							@ApiParam(value = "The version of vorto model, e.g. 1.0.0", required = true) final @PathVariable String version, 
							@ApiParam(value = "Service key for a specified platform, e.g. lwm2m", required = true) @PathVariable String serviceKey, 
							final HttpServletRequest request,
							final HttpServletResponse response) {
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		Map<String, String> requestParams = new HashMap<>();
		request.getParameterMap().entrySet().stream().forEach(x -> requestParams.put(x.getKey(), x.getValue()[0]));
		 
		GeneratedOutput generatedOutput = generatorService.generate(new ModelId(name,namespace,version), serviceKey, requestParams);
		response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + generatedOutput.getFileName());
		response.setContentLengthLong(generatedOutput.getSize());
		response.setContentType(APPLICATION_OCTET_STREAM);
		try {
			IOUtils.copy(new ByteArrayInputStream(generatedOutput.getContent()), response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			throw new RuntimeException("Error copying file.", e);
		}
	}

	@ApiOperation(value = "Returns all currently registered Code Generator")
	@RequestMapping(value = "/{classifier}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<GeneratorServiceInfo> getRegisteredGeneratorServices(@ApiParam(value = "Choose type of generator",allowableValues="platform,documentation", required = true) @PathVariable String classifier) {
		List<GeneratorServiceInfo> generatorInfoResult = new ArrayList<>();
		
		for (String serviceKey : this.generatorService.getRegisteredGeneratorServiceKeys(ServiceClassifier.valueOf(classifier))) {
			try {
				generatorInfoResult.add(this.generatorService.getGeneratorServiceInfo(serviceKey));
			} catch(Throwable t) {
				LOGGER.warn("Generator " + serviceKey+" appears to be offline or not deployed. Skipping...");
			}
		}

		return generatorInfoResult;
	}
		
	@ApiOperation(value = "Returns the rank of code generators by usage")
	@RequestMapping(value = "/topused/{top}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<GeneratorServiceInfo> getMostlyUsedGenerators(@ApiParam(value = "The upper limit number of top code generator list", required = true) final @PathVariable int top) {
		return this.generatorService.getMostlyUsedGenerators(top);
	}
	
	@ApiOperation(value = "Register a code generator",hidden=true)
	@RequestMapping(value = "/register/{serviceKey}/{classifier}", method = RequestMethod.PUT)
	public void registerGenerator(	@ApiParam(value = "Service key for a specified platform, e.g. lwm2m", required = true) final @PathVariable String serviceKey,
									@ApiParam(value = "Service type for a specified code generator, e.g. platform", required = true) final @PathVariable ServiceClassifier classifier,
									@ApiParam(value = "The URL links to a specified code generator", required = true) final @RequestBody String baseUrl) {
		this.generatorService.registerGenerator(serviceKey, baseUrl,classifier);
	}

	@ApiOperation(value = "Deregister a code generator",hidden=true)
	@RequestMapping(value = "/deregister/{serviceKey}", method = RequestMethod.PUT)
	public boolean deregisterGenerator(@ApiParam(value = "Service key for a specified platform, e.g. lwm2m", required = true) final @PathVariable String serviceKey) {
		this.generatorService.unregisterGenerator(serviceKey);
		return true;
	}
}
