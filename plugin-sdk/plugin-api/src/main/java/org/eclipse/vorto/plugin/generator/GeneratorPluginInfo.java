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
package org.eclipse.vorto.plugin.generator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.vorto.plugin.PluginInfo;

public class GeneratorPluginInfo extends PluginInfo {

  private String image32x32;
  private String image144x144;
  private String configTemplate = null;
  private Set<String> configKeys = null;
    
  public GeneratorPluginInfo() {
    
  }
  
  public static GeneratorPluginBuilder Builder(String key) {
    return new GeneratorPluginBuilder(key);
  }
  
  public GeneratorPluginInfo(String key) {
    this.setKey(key);    
  }
   
  public String getImage32x32() {
    return image32x32;
  }

  public void setImage32x32(String image32x32) {
    this.image32x32 = image32x32;
  }

  public String getImage144x144() {
    return image144x144;
  }

  public void setImage144x144(String image144x144) {
    this.image144x144 = image144x144;
  }

  public String getConfigTemplate() {
    return configTemplate;
  }

  public void setConfigTemplate(String configTemplate) {
    this.configTemplate = configTemplate;
  }

  public Set<String> getConfigKeys() {
    return configKeys;
  }

  public void setConfigKeys(Set<String> configKeys) {
    this.configKeys = configKeys;
  }
  
  public static class GeneratorPluginBuilder extends PluginBuilder<GeneratorPluginInfo> {

    public GeneratorPluginBuilder(String key) {
      super(new GeneratorPluginInfo(key));
    } 
    
    public GeneratorPluginBuilder withImage32x32(String image) {
      this.info.setImage32x32(image);
      return this;
    }
    
    public GeneratorPluginBuilder withImage144x144(String image) {
      this.info.setImage32x32(image);
      this.info.setImage144x144(image);
      return this;
    }

    public GeneratorPluginBuilder withConfigurationTemplate(String configHtmlTemplate) {
      this.info.setConfigTemplate(configHtmlTemplate);
      return this;
    }
    
    public GeneratorPluginBuilder withConfigurationKey(String... keys) {
      this.info.setConfigKeys(new HashSet<>(Arrays.asList(keys)));
      return this;
    }    
  }
    
}
