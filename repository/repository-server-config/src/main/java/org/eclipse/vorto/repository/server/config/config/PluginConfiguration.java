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
package org.eclipse.vorto.repository.server.config.config;

import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.importer.IModelImporter;
import org.eclipse.vorto.repository.plugin.generator.GeneratorPluginInfo;
import org.eclipse.vorto.repository.plugin.generator.IGeneratorPluginService;
import org.eclipse.vorto.repository.plugin.generator.impl.DefaultGeneratorPluginService;
import org.eclipse.vorto.repository.plugin.generator.impl.IGeneratorMetrics;
import org.eclipse.vorto.repository.plugin.importer.ImporterPluginInfo;
import org.eclipse.vorto.repository.plugin.importer.RemoteImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class PluginConfiguration {

  @Autowired
  private IGeneratorMetrics generatorMetrics;

  @Autowired
  private IModelRepositoryFactory modelRepositoryFactory;

  // ++++++++++++++++++++++ GENERATOR PLUGINS +++++++++++++++++++++++++++++++++++++++

  @Bean
  @Profile("local-test")
  public IGeneratorPluginService generatorPluginServiceLocalIntegrationTestProfile() {
    DefaultGeneratorPluginService service = new DefaultGeneratorPluginService();
    service.setGeneratorMetrics(generatorMetrics);
    service.setModelRepositoryFactory(modelRepositoryFactory);
    return service;
  }

  @Bean
  @Profile("local")
  public IGeneratorPluginService generatorPluginServiceLocalProfile() {
    DefaultGeneratorPluginService service = new DefaultGeneratorPluginService();
    service.setGeneratorMetrics(generatorMetrics);
    service.setModelRepositoryFactory(modelRepositoryFactory);
    service.registerPlugin(
        new GeneratorPluginInfo("boschiotsuite", "2", "http://localhost:8081/generatorgateway"));
    service.registerPlugin(
        new GeneratorPluginInfo("eclipseditto", "2", "http://localhost:8081/generatorgateway"));
    service.registerPlugin(
        new GeneratorPluginInfo("eclipsehono", "2", "http://localhost:8081/generatorgateway"));
    service.registerPlugin(new GeneratorPluginInfo("jsonschema", "2",
        "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Development"));
    return service;
  }


  @Bean
  @Profile("int")
  public IGeneratorPluginService generatorPluginServiceIntProfile() {
    DefaultGeneratorPluginService service = new DefaultGeneratorPluginService();
    service.setGeneratorMetrics(generatorMetrics);
    service.setModelRepositoryFactory(modelRepositoryFactory);
    service.registerPlugin(new GeneratorPluginInfo("boschiotsuite", "1",
        "http://vorto-generators-dev.eu-central-1.elasticbeanstalk.com:8080"));
    service.registerPlugin(new GeneratorPluginInfo("eclipseditto", "1",
        "http://vorto-generators-dev.eu-central-1.elasticbeanstalk.com:8080"));
    service.registerPlugin(new GeneratorPluginInfo("eclipsehono", "1",
        "http://vorto-generators-dev.eu-central-1.elasticbeanstalk.com:8080"));
    service.registerPlugin(new GeneratorPluginInfo("jsonschema", "2",
        "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Development"));
    return service;
  }

  @Bean
  @Profile("aws")
  public IGeneratorPluginService generatorPluginServiceProdProfile() {
    DefaultGeneratorPluginService service = new DefaultGeneratorPluginService();
    service.setGeneratorMetrics(generatorMetrics);
    service.setModelRepositoryFactory(modelRepositoryFactory);
    service.registerPlugin(new GeneratorPluginInfo("boschiotsuite", "1",
        "http://vorto-generators.eu-central-1.elasticbeanstalk.com:8080"));
    service.registerPlugin(new GeneratorPluginInfo("eclipseditto", "1",
        "http://vorto-generators.eu-central-1.elasticbeanstalk.com:8080"));
    service.registerPlugin(new GeneratorPluginInfo("eclipsehono", "1",
        "http://vorto-generators.eu-central-1.elasticbeanstalk.com:8080"));
    return service;
  }

  //++++++++++++++++++++++ IMPORTER PLUGINS +++++++++++++++++++++++++++++++++++++++
  
  
  @Bean
  @Profile("local")
  public IModelImporter createExampleImporter() {
    ImporterPluginInfo info = new ImporterPluginInfo("lwm2m", "LwM2M",
        "Converts LwM2M descriptions to Vorto", "Vorto Team",
        "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Development", ".xml");
    return new RemoteImporter(info);
  }
}
