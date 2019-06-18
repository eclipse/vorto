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
package org.eclipse.vorto.repository.web.generation;

import java.util.Collection;
import org.eclipse.vorto.repository.plugin.generator.GeneratorPluginConfiguration;
import org.eclipse.vorto.repository.plugin.generator.IGeneratorPluginService;
import org.eclipse.vorto.repository.plugin.generator.impl.DefaultGeneratorPluginService;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = "/rest/{tenantId}/generators")
public class GeneratorController extends AbstractRepositoryController {

  @Autowired
  private IGeneratorPluginService generatorService;

  @ApiOperation(value = "Returns the rank of code generators by usage")
  @PreAuthorize("hasRole('ROLE_USER')")
  @RequestMapping(value = "/rankings/{top}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Collection<GeneratorPluginConfiguration> getMostlyUsedGenerators(
      @ApiParam(value = "The upper limit number of top code generator list",
          required = true) final @PathVariable int top) {
    return this.generatorService.getMostlyUsed(top);
  }
  
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  @RequestMapping(value = "/plugincache/{pluginkey}/remove", method = RequestMethod.GET)
  public void removeCachedPlugin(final @PathVariable String pluginkey) {
    ((DefaultGeneratorPluginService)generatorService).clearPluginCache(pluginkey);
  }
}
