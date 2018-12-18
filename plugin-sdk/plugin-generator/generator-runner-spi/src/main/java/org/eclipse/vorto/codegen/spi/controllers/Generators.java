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
package org.eclipse.vorto.codegen.spi.controllers;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.spi.config.IGeneratorConfiguration;
import org.eclipse.vorto.codegen.spi.model.Generator;
import org.eclipse.vorto.codegen.spi.repository.GeneratorRepository;
import org.eclipse.vorto.codegen.spi.service.VortoService;
import org.eclipse.vorto.codegen.spi.utils.GatewayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class Generators {

  private static final String AUTHORIZATION = "Authorization";

  @Autowired
  private IGeneratorConfiguration env;

  @Autowired
  private VortoService vorto;

  @Autowired
  private GeneratorRepository repo;

  @RequestMapping(value = "/generators", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Collection<GeneratorServiceInfo> list() {
    return repo.list().stream().map(generator -> generator.getInfo()).collect(Collectors.toList());
  }

  @RequestMapping(value = "/generators/{key}/generate/info", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public GeneratorServiceInfo info(final @PathVariable String key,
      @RequestParam boolean includeConfigUI) {
    Generator generator =
        repo.get(key).orElseThrow(GatewayUtils.notFound(String.format("[Generator %s]", key)));
    return includeConfigUI ? generator.getFullInfo() : generator.getInfo();
  }

  @RequestMapping(value = "/generators/{key}/generate/{namespace}/{name}/{version:.+}",
      method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<InputStreamResource> generate(final @PathVariable String key,
      @PathVariable String namespace, @PathVariable String name, @PathVariable String version,
      final HttpServletRequest request) {
    return responseFromResult(vorto.generate(key, namespace, name, version,
        GatewayUtils.mapFromRequest(request), getAuthorization(request)));
  }

  @RequestMapping(value = "/generators/{key}/generate", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<InputStreamResource> generateInfra(final @PathVariable String key,
      final HttpServletRequest request) {
    return responseFromResult(
        vorto.generate(key, GatewayUtils.mapFromRequest(request), getAuthorization(request)));
  }

  private Optional<String> getAuthorization(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(AUTHORIZATION));
  }

  private ResponseEntity<InputStreamResource> responseFromResult(IGenerationResult result) {
    return ResponseEntity.ok().contentLength(result.getContent().length)
        .header("content-disposition", "attachment; filename = " + result.getFileName())
        .contentType(MediaType.parseMediaType(result.getMediatype()))
        .body(new InputStreamResource(new ByteArrayInputStream(result.getContent())));
  }

  @RequestMapping(value = "/reset", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, String> reRegister() {
    Map<String, String> map = new HashMap<String, String>();

    try {
      repo.list().stream().forEach(vorto::register);
      map.put("result", "OK");
    } catch (Exception e) {
      map.put("result", "ERROR");
      map.put("errorMessage", e.getMessage());
    }

    map.put("vortoServerUrl", env.getVortoRepoUrl());
    map.put("applicationServiceUrl", env.getAppServiceUrl());
    map.put("numGenerators", Integer.toString(repo.list().size()));

    return map;
  }
}
