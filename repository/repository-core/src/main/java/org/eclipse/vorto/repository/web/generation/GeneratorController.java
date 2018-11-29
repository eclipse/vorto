/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.web.generation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.generation.GeneratedOutput;
import org.eclipse.vorto.repository.generation.GeneratorInfo;
import org.eclipse.vorto.repository.generation.IGeneratorService;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value = "/generate", description = "Generate code from information models")
@RestController("internal.GeneratorController")
@RequestMapping(value = "/rest/{tenant}/generators")
public class GeneratorController extends AbstractRepositoryController {

  @Autowired
  private IGeneratorService generatorService;

  @ApiOperation(value = "Returns the rank of code generators by usage")
  @PreAuthorize("hasRole('ROLE_USER')")
  @RequestMapping(value = "/rankings/{top}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Collection<GeneratorInfo> getMostlyUsedGenerators(
      @ApiParam(value = "The upper limit number of top code generator list",
          required = true) final @PathVariable int top) {
    return this.generatorService.getMostlyUsedGenerators(top);
  }

  @ApiOperation(value = "Register a code generator", hidden = true)
  @RequestMapping(value = "/{serviceKey}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('ROLE_GENERATOR_PROVIDER')")
  public void registerGenerator(
      @ApiParam(value = "Service key for a specified platform, e.g. lwm2m",
          required = true) final @PathVariable String serviceKey,
      @ApiParam(value = "The URL links to a specified code generator",
          required = true) final @RequestBody String baseUrl) {
    this.generatorService.registerGenerator(serviceKey, baseUrl);
  }

  @ApiOperation(value = "Deregister a code generator", hidden = true)
  @RequestMapping(value = "/{serviceKey}", method = RequestMethod.DELETE)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public boolean deregisterGenerator(
      @ApiParam(value = "Service key for a specified platform, e.g. lwm2m",
          required = true) final @PathVariable String serviceKey) {
    this.generatorService.unregisterGenerator(serviceKey);
    return true;
  }

  @RequestMapping(value = "/{serviceKey}", method = RequestMethod.GET)
  public void generate(
      @ApiParam(value = "generator key, e.g. lwm2m",
          required = true) @PathVariable String serviceKey,
      final HttpServletRequest request, final HttpServletResponse response) {
    Objects.requireNonNull(serviceKey, "generator Key must not be null");

    try {
      GeneratedOutput generatedOutput = generatorService
          .generate(URLDecoder.decode(serviceKey, "utf-8"), getRequestParams(request));
      writeToResponse(response, generatedOutput);
    } catch (IOException e) {
      throw new RuntimeException("Error copying file.", e);
    }
  }

  private Map<String, String> getRequestParams(final HttpServletRequest request) {
    Map<String, String> requestParams = new HashMap<>();
    request.getParameterMap().entrySet().stream().forEach(x -> {
      requestParams.put(x.getKey(), x.getValue()[0]);
    });

    return requestParams;
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
}
