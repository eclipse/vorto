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
package org.eclipse.vorto.codegen.spi.controllers;

import java.io.ByteArrayInputStream;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.spi.model.Generator;
import org.eclipse.vorto.codegen.spi.repository.GeneratorRepository;
import org.eclipse.vorto.codegen.spi.service.VortoService;
import org.eclipse.vorto.codegen.spi.utils.GatewayUtils;
import org.eclipse.vorto.model.ModelContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/2/plugins/generators")
public class GeneratorControllerV2 {

  @Autowired
  private VortoService vorto;

  @Autowired
  private GeneratorRepository repo;

  @RequestMapping(value = "/{pluginkey}/info", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public GeneratorServiceInfo info(final @PathVariable String pluginkey) {
    Generator generator = repo.get(pluginkey)
        .orElseThrow(GatewayUtils.notFound(String.format("[Generator %s]", pluginkey)));
    return generator.getFullInfo();
  }

  @RequestMapping(value = "/{pluginkey}",
      method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.GET})
  public ResponseEntity<InputStreamResource> generate(final @PathVariable String pluginkey,
      @RequestBody ModelContent model, final HttpServletRequest request) {
    return responseFromResult(
        vorto.generate(model, pluginkey, GatewayUtils.mapFromRequest(request)));
  }


  private ResponseEntity<InputStreamResource> responseFromResult(IGenerationResult result) {
    return ResponseEntity.ok().contentLength(result.getContent().length)
        .header("content-disposition", "attachment; filename = " + result.getFileName())
        .contentType(MediaType.parseMediaType(result.getMediatype()))
        .body(new InputStreamResource(new ByteArrayInputStream(result.getContent())));
  }
}
