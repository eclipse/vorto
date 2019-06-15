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

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.impl.ITemporaryStorage;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.importer.IModelImporter;
import org.eclipse.vorto.repository.plugin.generator.GeneratorPluginInfo;
import org.eclipse.vorto.repository.plugin.generator.IGeneratorPluginService;
import org.eclipse.vorto.repository.plugin.generator.impl.DefaultGeneratorPluginService;
import org.eclipse.vorto.repository.plugin.generator.impl.IGeneratorMetrics;
import org.eclipse.vorto.repository.plugin.importer.ImporterPluginInfo;
import org.eclipse.vorto.repository.plugin.importer.RemoteImporter;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.tenant.ITenantUserService;
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
  
  @Autowired
  private ModelParserFactory modelParserFactory;
  
  @Autowired
  private IUserAccountService userAccountService;
  
  @Autowired
  private ITenantService tenantService;
  
  @Autowired
  private ITenantUserService tenantUserService;
  
  @Autowired
  private ITemporaryStorage fileStorage;

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
        new GeneratorPluginInfo("boschiotsuite", "2", "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Development"));
    service.registerPlugin(
        new GeneratorPluginInfo("eclipseditto", "2", "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Development"));
    service.registerPlugin(
        new GeneratorPluginInfo("eclipsehono", "2", "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Development"));
    service.registerPlugin(new GeneratorPluginInfo("protobuf", "2",
        "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Development","demo"));
    return service;
  }


  @Bean
  @Profile("int")
  public IGeneratorPluginService generatorPluginServiceIntProfile() {
    DefaultGeneratorPluginService service = new DefaultGeneratorPluginService();
    service.setGeneratorMetrics(generatorMetrics);
    service.setModelRepositoryFactory(modelRepositoryFactory);
    service.registerPlugin(new GeneratorPluginInfo("boschiotsuite", "2",
        "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Development"));
    service.registerPlugin(new GeneratorPluginInfo("eclipseditto", "2",
        "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Development"));
    service.registerPlugin(new GeneratorPluginInfo("eclipsehono", "2",
        "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Development"));
    service.registerPlugin(new GeneratorPluginInfo("protobuf", "2",
        "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Development","demo"));
    return service;
  }

  @Bean
  @Profile("aws")
  public IGeneratorPluginService generatorPluginServiceProdProfile() {
    DefaultGeneratorPluginService service = new DefaultGeneratorPluginService();
    service.setGeneratorMetrics(generatorMetrics);
    service.setModelRepositoryFactory(modelRepositoryFactory);
    service.registerPlugin(new GeneratorPluginInfo("boschiotsuite", "2",
        "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Production"));
    service.registerPlugin(new GeneratorPluginInfo("eclipseditto", "2",
        "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Production"));
    service.registerPlugin(new GeneratorPluginInfo("eclipsehono", "2",
        "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Production"));
    service.registerPlugin(new GeneratorPluginInfo("protobuf", "2",
        "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Production","demo"));
    return service;
  }

  //++++++++++++++++++++++ IMPORTER PLUGINS +++++++++++++++++++++++++++++++++++++++
  
  
  @Bean
  @Profile({ "local", "int" })
  public IModelImporter createExampleImporter() {
    ImporterPluginInfo info = new ImporterPluginInfo("lwm2m", "LwM2M",
        "Converts LwM2M descriptions to Vorto", "Vorto Team",
        "https://iyno3mzx1h.execute-api.eu-central-1.amazonaws.com/Development", ".xml");
    return createImporter(info);
  }
  
  private RemoteImporter createImporter(ImporterPluginInfo info) {
    RemoteImporter importer = new RemoteImporter(info);
    importer.setModelParserFactory(this.modelParserFactory);
    importer.setModelRepoFactory(this.modelRepositoryFactory);
    importer.setTenantUserService(tenantUserService);
    importer.setTenantService(tenantService);
    importer.setUploadStorage(fileStorage);
    return importer;
  }
}
