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
package org.eclipse.vorto.repository.server.config.config.plugins;

import java.util.Arrays;
import java.util.Base64;
import javax.annotation.PostConstruct;
import org.eclipse.vorto.plugin.importer.ImporterPluginInfo;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.impl.ITemporaryStorage;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.importer.impl.DefaultModelImportService;
import org.eclipse.vorto.repository.plugin.generator.GeneratorPluginConfiguration;
import org.eclipse.vorto.repository.plugin.generator.impl.DefaultGeneratorPluginService;
import org.eclipse.vorto.repository.plugin.importer.RemoteImporter;
import org.eclipse.vorto.repository.server.config.config.plugins.Plugin.PluginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class PluginConfiguration {

  @Autowired
  private IModelRepositoryFactory modelRepositoryFactory;
  
  @Autowired
  private ModelParserFactory modelParserFactory;
  
  @Autowired
  private IUserAccountService userAccountService;
  
  @Value("${plugins:#{null}}")
  private String pluginsJson;
  
  @Autowired
  private ITemporaryStorage fileStorage;
  
  @Autowired 
  private DefaultGeneratorPluginService generatorPluginService;
  
  @Autowired
  private DefaultModelImportService importerPluginService;
  
  @Autowired
  private RestTemplate restTemplate;
  
  @PostConstruct
  public void registerPlugins() throws Exception {
    if (this.pluginsJson == null) {
      return;
    }
    
    ObjectMapper mapper = new ObjectMapper();
    
    Plugin[] plugins = mapper.readValue(Base64.getDecoder().decode(this.pluginsJson.getBytes()), Plugin[].class);
    
    if (plugins != null && plugins.length > 0) {
      Arrays.asList(plugins).stream().forEach(plugin -> {
        if (plugin.getPluginType().equals(PluginType.generator)) {
          GeneratorPluginConfiguration config = GeneratorPluginConfiguration.of(plugin.getKey(), plugin.getApiVersion(), plugin.getEndpoint());
          if (plugin.getTag()!= null) {
            config.setTags(new String[] {plugin.getTag()});
          }
          generatorPluginService.registerPlugin(config);
        } else {
          ImporterPluginInfo info = new ImporterPluginInfo(plugin.getKey(), plugin.getName(),
              plugin.getDescription(), plugin.getVendor(),plugin.getFileType());
          this.importerPluginService.registerImporter(createImporter(info, plugin.getEndpoint()));
        }
      });
    }
    
    
  }
  
  private RemoteImporter createImporter(ImporterPluginInfo info, String endpointUrl) {
    RemoteImporter importer = new RemoteImporter(info,endpointUrl);
    importer.setModelParserFactory(this.modelParserFactory);
    importer.setModelRepoFactory(this.modelRepositoryFactory);
    importer.setUploadStorage(fileStorage);
    importer.setUserRepository(userAccountService);
    importer.setRestTemplate(restTemplate);
    return importer;
}
}
