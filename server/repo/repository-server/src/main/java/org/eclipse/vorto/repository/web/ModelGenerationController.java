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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.generation.GeneratedOutput;
import org.eclipse.vorto.repository.api.generation.GeneratorInfo;
import org.eclipse.vorto.repository.api.generation.ServiceClassifier;
import org.eclipse.vorto.repository.service.IGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value="/generate", description="Generate code from information models")
@RestController
@RequestMapping(value = "/rest/generation-router")
public class ModelGenerationController extends AbstractRepositoryController {
	
	private static final String ZIPFILE_EXTENSION = ".zip";
	private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String CONTENT_DISPOSITION = "content-disposition";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ModelGenerationController.class);

	@Autowired
	private IGeneratorService generatorService;
	
	@ApiOperation(value = "Generate code for a specified platform, and extract specified path")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found")})
	@RequestMapping(value = "/{namespace}/{name}/{version:.+}/{serviceKey:[^!]+}!/**", method = RequestMethod.GET)
	public void generateAndExtract(@ApiParam(value = "The namespace of vorto model, e.g. com.mycompany", required = true) final @PathVariable String namespace, 
							@ApiParam(value = "The name of vorto model, e.g. NewInfomodel", required = true) final @PathVariable String name,
							@ApiParam(value = "The version of vorto model, e.g. 1.0.0", required = true) final @PathVariable String version, 
							@ApiParam(value = "Service key for a specified platform, e.g. lwm2m", required = true) @PathVariable String serviceKey,
							final HttpServletRequest request,
							final HttpServletResponse response) {
		
		try {
			GeneratedOutput generatedOutput = generatorService.generate(new ModelId(name,namespace,version), 
					URLDecoder.decode(serviceKey, "utf-8"), getRequestParams(request));
			
			String extractPath = getExtractPath(request);

			if (extractPath == null || extractPath.trim().isEmpty()) {
				writeToResponse(response, generatedOutput);
				return;
			}
			 
			if (generatedOutput.getFileName().endsWith(ZIPFILE_EXTENSION)) {
				Optional<GeneratedOutput> extractionResult = extractFromZip(generatedOutput.getContent(), extractPath);
				if (extractionResult.isPresent()) {
					writeToResponse(response, extractionResult.get());
					return;
				}
			}
			
			response.sendError(HttpServletResponse.SC_NOT_FOUND);		
		} catch (IOException e) {
			throw new RuntimeException("Error copying file.", e);
		}	
	}
	
	private Optional<GeneratedOutput> extractFromZip(byte[] zipFile, String filenameInZip) throws IOException {
		Objects.requireNonNull(zipFile);
		Objects.requireNonNull(filenameInZip);
		
		ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipFile));
		ZipEntry ze = null;
		while ((ze = zipInputStream.getNextEntry()) != null) {
			if (ze.getName().equals(filenameInZip)) {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream(9000);
				
				byte[] buffer = new byte[9000];
				int len = 0;
				while ((len = zipInputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, len);
				}
				
				byte[] outputBytes = outputStream.toByteArray();
				
				return Optional.of(new GeneratedOutput(outputBytes, Paths.get(ze.getName()).getFileName().toString(), outputBytes.length));
			}
		}
		
		return Optional.empty();
	}

	private String getExtractPath(final HttpServletRequest request) {
		String path = (String) request.getAttribute(
	            HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
	    String bestMatchPattern = (String ) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

	    AntPathMatcher apm = new AntPathMatcher();
	    return apm.extractPathWithinPattern(bestMatchPattern, path);
	}
	
	@ApiOperation(value = "Generate code for a specified platform")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found")})
	@RequestMapping(value = "/{namespace}/{name}/{version:.+}/{serviceKey:[^!]+}", method = RequestMethod.GET)
	public void generate( 	@ApiParam(value = "The namespace of vorto model, e.g. com.mycompany", required = true) final @PathVariable String namespace, 
							@ApiParam(value = "The name of vorto model, e.g. NewInfomodel", required = true) final @PathVariable String name,
							@ApiParam(value = "The version of vorto model, e.g. 1.0.0", required = true) final @PathVariable String version, 
							@ApiParam(value = "Service key for a specified platform, e.g. lwm2m", required = true) @PathVariable String serviceKey, 
							final HttpServletRequest request,
							final HttpServletResponse response) {
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");
		Objects.requireNonNull(serviceKey, "version must not be null");

		try {
			GeneratedOutput generatedOutput = generatorService.generate(new ModelId(name,namespace,version), URLDecoder.decode(serviceKey, "utf-8"), getRequestParams(request));
			writeToResponse(response, generatedOutput);
		} catch (IOException e) {
			throw new RuntimeException("Error copying file.", e);
		}
	}

	private void writeToResponse(final HttpServletResponse response, GeneratedOutput generatedOutput)
			throws IOException {
		response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + generatedOutput.getFileName());
		response.setContentLengthLong(generatedOutput.getSize());
		response.setContentType(APPLICATION_OCTET_STREAM);

		IOUtils.copy(new ByteArrayInputStream(generatedOutput.getContent()), response.getOutputStream());
		response.flushBuffer();
	}
	
	private Map<String, String> getRequestParams(final HttpServletRequest request) {
		Map<String, String> requestParams = new HashMap<>();
		request.getParameterMap().entrySet().stream().forEach(x -> {
			requestParams.put(x.getKey(), x.getValue()[0]);
		});
		return requestParams;
	}

	@ApiOperation(value = "Returns all currently registered Code Generator")
	@RequestMapping(value = "/{classifier}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<GeneratorInfo> getRegisteredGeneratorServices(
			@ApiParam(value = "Choose type of generator", allowableValues="platform,documentation", required = true) @PathVariable String classifier,
			@ApiParam(value = "Prioritize results with given tag", allowableValues="any given tags", required = false) @RequestParam(value = "orderBy", required=false, defaultValue="production") String orderBy) {
		List<GeneratorInfo> generatorInfoResult = new ArrayList<>();
		
		for (String serviceKey : this.generatorService.getRegisteredGeneratorServiceKeys(ServiceClassifier.valueOf(classifier))) {
			try {
				generatorInfoResult.add(this.generatorService.getGeneratorServiceInfo(serviceKey));
			} catch(Throwable t) {
				LOGGER.warn("Generator " + serviceKey+" appears to be offline or not deployed. Skipping...");
			}
		}
		
		generatorInfoResult.sort((genInfo1, genInfo2) -> {
			if (contains(genInfo1.getTags(), orderBy) ^ contains(genInfo2.getTags(), orderBy)) {
				if (contains(genInfo1.getTags(), orderBy)) {
					return -1;
				} else {
					return 1;
				}
			}
			
			return genInfo1.getName().compareTo(genInfo2.getName());
		});
		
		return generatorInfoResult;
	}
	
	private boolean contains(String[] tags, String tag) {
		if (tags != null) {
			for(String _tag : tags) {
				if (_tag.equals(tag)) {
					return true;
				}
			}
		}
		
		return false;
	}
		
	@ApiOperation(value = "Returns the rank of code generators by usage")
	@RequestMapping(value = "/topused/{top}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<GeneratorInfo> getMostlyUsedGenerators(@ApiParam(value = "The upper limit number of top code generator list", required = true) final @PathVariable int top) {
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
