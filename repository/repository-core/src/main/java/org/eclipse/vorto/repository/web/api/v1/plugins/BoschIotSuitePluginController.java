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
package org.eclipse.vorto.repository.web.api.v1.plugins;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.vorto.repository.plugin.generator.GeneratorPluginConfiguration;
import org.eclipse.vorto.repository.web.api.v1.AbstractGeneratorController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiParam;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController
@RequestMapping(value = "/api/v1/plugins/generators/boschiotsuite")
public class BoschIotSuitePluginController extends AbstractGeneratorController {

  @RequestMapping(value = "/models/{modelId:.+}/connectivity",
      method = RequestMethod.GET)
  @CrossOrigin(origins = "https://www.eclipse.org")
  public void generateBoschIoTSuiteDeviceConnectivity(
      @ApiParam(value = "the vorto model ID, e.g. com.acme:Thermostat:1.0.0",
          required = true) final @PathVariable String modelId, @RequestParam(value="language",required=true) Language language, final HttpServletResponse response) {
    Map<String, String> params = new HashMap<>();
    params.put("language", language.name());
    generateAndWriteToOutputStream(modelId, "boschiotsuite", params, response);
  }

  @RequestMapping(value = "/models/{modelId:.+}/provision",
      method = RequestMethod.GET)
  @CrossOrigin(origins = "*")
  public void generateBoschIoTSuitePostman(
      @ApiParam(value = "the vorto model ID, e.g. com.acme:Thermostat:1.0.0",
          required = true) final @PathVariable String modelId, final HttpServletResponse response) {
    Map<String, String> params = new HashMap<>();
    params.put("provision", "true");
    generateAndWriteToOutputStream(modelId, "boschiotsuite", params, response);
  }

  @RequestMapping(value = "/", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin(origins = "https://www.eclipse.org")
  public GeneratorPluginConfiguration getBoschIoTSuiteInfo() {
    return this.generatorService.getPluginInfo("boschiotsuite", true);
  }
  
  public enum Language {
    python, arduino, java
  }
  
}
