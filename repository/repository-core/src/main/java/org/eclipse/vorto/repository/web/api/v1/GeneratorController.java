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
package org.eclipse.vorto.repository.web.api.v1;

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
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.generation.GeneratedOutput;
import org.eclipse.vorto.repository.generation.GeneratorInfo;
import org.eclipse.vorto.repository.generation.IGeneratorService;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
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
@Api(value = "/generate")
@RestController
@RequestMapping(value = "/api/v1/generators")
public class GeneratorController extends AbstractRepositoryController {

  private static final String ZIPFILE_EXTENSION = ".zip";
  private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
  private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
  private static final String CONTENT_DISPOSITION = "content-disposition";

  private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorController.class);

  @Autowired
  private IGeneratorService generatorService;

  @ApiOperation(value = "Generate code for a specified platform, and extract specified path",
		  notes = "This method generates artifacts for a specified platform using the 'service key' value along with the 'modelId'."
				    + "<br/>"
			  		+ "<pre>"
			  		+ "* modelId : The combined value of 'namespace:name:version' of the model<br/>"
			  		+ "	Example: com.mycompany:MagneticSensor:1.0.0<br/>"
			  		+ "* serviceKey : The key value of the specific generator.<br/>"
			  		+ "</pre>")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Code was successfully generated."),
      @ApiResponse(code = 400, message = "Wrong input"),
      @ApiResponse(code = 404, message = "Model or generator not found")})
  @RequestMapping(value = "/{serviceKey}/models/{modelId:.+}/!/**", method = RequestMethod.GET)
  public void generateAndExtract(
      @ApiParam(value = "The iD of vorto model, e.g. com.mycompany:Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "Service key for a specified platform, e.g. lwm2m",
          required = true) @PathVariable String serviceKey,
      final HttpServletRequest request, final HttpServletResponse response) {

    try {
      GeneratedOutput generatedOutput = generatorService.generate(ModelId.fromPrettyFormat(modelId),
          URLDecoder.decode(serviceKey, "utf-8"), getRequestParams(request));

      String extractPath = getExtractPath(request);

      if (extractPath == null || extractPath.trim().isEmpty()) {
        writeToResponse(response, generatedOutput);
        return;
      }

      if (generatedOutput.getFileName().endsWith(ZIPFILE_EXTENSION)) {
        Optional<GeneratedOutput> extractionResult =
            extractFromZip(generatedOutput.getContent(), extractPath);
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

  private Optional<GeneratedOutput> extractFromZip(byte[] zipFile, String filenameInZip)
      throws IOException {
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

        return Optional.of(new GeneratedOutput(outputBytes,
            Paths.get(ze.getName()).getFileName().toString(), outputBytes.length));
      }
    }

    return Optional.empty();
  }

  private String getExtractPath(final HttpServletRequest request) {
    String path =
        (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String bestMatchPattern =
        (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

    AntPathMatcher apm = new AntPathMatcher();
    return apm.extractPathWithinPattern(bestMatchPattern, path);
  }

  @ApiOperation(value = "Generate code for a specified platform",
		  notes = "This method generates artifacts for a specified platform using the 'service key' value along with the 'modelId'."
				    + "<br/>"
			  		+ "<pre>"
			  		+ "* modelId : The combined value of 'namespace:name:version' of the model<br/>"
			  		+ "	Example: com.mycompany:MagneticSensor:1.0.0<br/>"
			  		+ "* serviceKey : The key value of the specific generator.<br/>"
			  		+ "</pre>")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Code was successfully generated."),
      @ApiResponse(code = 400, message = "Wrong input"),
      @ApiResponse(code = 404, message = "Model or generator not found")})
  @RequestMapping(value = "/{serviceKey}/models/{modelId:.+}", method = RequestMethod.GET)
  public void generate(
      @ApiParam(value = "the vorto model ID, e.g. com.mycompany:Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "generator key, e.g. lwm2m",
          required = true) @PathVariable String serviceKey,
      final HttpServletRequest request, final HttpServletResponse response) {
    Objects.requireNonNull(modelId, "modelID must not be null");
    Objects.requireNonNull(serviceKey, "generator Key must not be null");

    try {
      GeneratedOutput generatedOutput = generatorService.generate(ModelId.fromPrettyFormat(modelId),
          URLDecoder.decode(serviceKey, "utf-8"), getRequestParams(request));
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

    IOUtils.copy(new ByteArrayInputStream(generatedOutput.getContent()),
        response.getOutputStream());
    response.flushBuffer();
  }

  private Map<String, String> getRequestParams(final HttpServletRequest request) {
    Map<String, String> requestParams = new HashMap<>();
    request.getParameterMap().entrySet().stream().forEach(x -> {
      requestParams.put(x.getKey(), x.getValue()[0]);
    });

    return requestParams;
  }

  @ApiOperation(value = "Returns all currently registered Code Generator",
		  notes = "This method call retrieves all registered code generators currently in the repository."
				    + "<br/>The generators are grouped under 'production', 'infra' and 'demo' tags.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Retrieved generators successfully")})
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Collection<GeneratorInfo> getRegisteredGeneratorServices(
      @ApiParam(value = "Prioritize results with given tag", allowableValues = "demo,infra,production",
          required = false) @RequestParam(value = "orderBy", required = false,
              defaultValue = "production") String orderBy) {
    List<GeneratorInfo> generatorInfoResult = new ArrayList<>();

    for (String serviceKey : this.generatorService.getRegisteredGeneratorServiceKeys()) {
      try {
        generatorInfoResult.add(this.generatorService.getGeneratorServiceInfo(serviceKey, false));
      } catch (Throwable t) {
        LOGGER.warn(
            "Generator " + serviceKey + " appears to be offline or not deployed. Skipping...");
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
      for (String _tag : tags) {
        if (_tag.equals(tag)) {
          return true;
        }
      }
    }

    return false;
  }

  @ApiOperation(value = "Returns a specific generator info", 
		  notes = "This method retrieves information of a specific generator. The input that needs to be passed is the 'servicekey' of the generator.")
  @RequestMapping(value = "/{serviceKey}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public GeneratorInfo getGeneratorInfo(
      @ApiParam(value = "generator service key", required = true) @PathVariable String serviceKey) {

    return this.generatorService.getGeneratorServiceInfo(serviceKey, true);
  }

}
