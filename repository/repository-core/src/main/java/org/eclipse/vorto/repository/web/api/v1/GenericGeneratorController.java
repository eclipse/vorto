/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.plugin.generator.GeneratedOutput;
import org.eclipse.vorto.repository.plugin.generator.GeneratorPluginConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import io.swagger.annotations.ApiParam;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController
@RequestMapping(value = "/api/v1/generators")
public class GenericGeneratorController extends AbstractGeneratorController {

  private static final String ZIPFILE_EXTENSION = ".zip";

  private static final Logger LOGGER = LoggerFactory.getLogger(GenericGeneratorController.class);

  @RequestMapping(value = "/{serviceKey}/models/{modelId:.+}/!/**", method = RequestMethod.GET)
  @CrossOrigin(origins = "https://www.eclipse.org")
  public void generateAndExtract(
      @ApiParam(value = "The iD of vorto model, e.g. com.mycompany:Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "Service key for a specified platform, e.g. lwm2m",
          required = true) @PathVariable String serviceKey,
      final HttpServletRequest request, final HttpServletResponse response) {

    try {
      ModelId modelIdToGen = ModelId.fromPrettyFormat(modelId);

      GeneratedOutput generatedOutput = generatorService.generate(getUserContext(modelIdToGen),
          modelIdToGen, URLDecoder.decode(serviceKey, "utf-8"), getRequestParams(request));

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

  @RequestMapping(value = "/{pluginkey}/models/{modelId:.+}", method = RequestMethod.GET)
  @CrossOrigin(origins = "https://www.eclipse.org")
  public void generate(
      @ApiParam(value = "the vorto model ID, e.g. com.mycompany:Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "generator key, e.g. lwm2m",
          required = true) @PathVariable String pluginkey,
      final HttpServletRequest request, final HttpServletResponse response) {
    Objects.requireNonNull(modelId, "modelID must not be null");
    Objects.requireNonNull(pluginkey, "plugin key must not be null");

    generateAndWriteToOutputStream(modelId, pluginkey, request, response);
  }
  

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin(origins = "https://www.eclipse.org")
  public Collection<GeneratorPluginConfiguration> getRegisteredGeneratorServices(
      @ApiParam(value = "Prioritize results with given tag",
          allowableValues = "demo,infra,production", required = false) @RequestParam(
              value = "orderBy", required = false, defaultValue = "production") String orderBy) {
    List<GeneratorPluginConfiguration> generatorInfoResult = new ArrayList<>();

    for (String serviceKey : this.generatorService.getKeys()) {
      try {
        generatorInfoResult.add(this.generatorService.getPluginInfo(serviceKey, false));
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

      return genInfo1.getKey().compareTo(genInfo2.getKey());
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

  @RequestMapping(value = "/{pluginkey}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin(origins = "https://www.eclipse.org")
  public GeneratorPluginConfiguration getGeneratorInfo(@ApiParam(value = "The plugin key, e.g. openapi",
      required = true) final @PathVariable String pluginkey) {
    return this.generatorService.getPluginInfo(pluginkey, true);
  }

}
