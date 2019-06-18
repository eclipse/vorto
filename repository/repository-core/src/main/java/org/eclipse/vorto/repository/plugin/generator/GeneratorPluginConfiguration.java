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
package org.eclipse.vorto.repository.plugin.generator;

import org.eclipse.vorto.plugin.generator.GeneratorPluginInfo;
import org.eclipse.vorto.repository.plugin.generator.impl.DefaultGeneratorConfigUI;
import org.eclipse.vorto.repository.plugin.generator.impl.GeneratorPluginInfoV1;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class GeneratorPluginConfiguration extends GeneratorPluginInfo {
  
  private static final DefaultGeneratorConfigUI DEFAULT_CONFIG = new DefaultGeneratorConfigUI();

  private String[] tags;
  
  private int amountOfDownloads;
  
  @JsonIgnore
  private String apiVersion;
  
  @JsonIgnore
  private String endpointUrl;
  
  @Deprecated
  private String creator;
  @Deprecated
  private String infoUrl;
  
  protected GeneratorPluginConfiguration() {
    
  }
  
  public static GeneratorPluginConfiguration of(String key, String apiVersion, String endpointUrl, String...tags) {
    GeneratorPluginConfiguration config = new GeneratorPluginConfiguration();
    config.setKey(key);
    config.setApiVersion(apiVersion);
    config.setEndpointUrl(endpointUrl);
    if (tags != null && tags.length > 0) {
      config.setTags(tags);
    } else {
      config.setTags(new String[] {"production"});
    } 
    
    return config;
  }
  
  public static GeneratorPluginConfiguration of (GeneratorPluginInfoV1 info, String endpointUrl, String... tags) {
    GeneratorPluginConfiguration config = GeneratorPluginConfiguration.of(info.getKey(),"1",endpointUrl,tags);   
    config.setConfigKeys(info.getConfigKeys());
    if (info.getConfigTemplate() == null || info.getConfigTemplate().length() == 0) {
      config.setConfigTemplate(DEFAULT_CONFIG.getContent().toString());
    } else {
      config.setConfigTemplate(info.getConfigTemplate());
    }
    config.setName(info.getName());
    config.setCreator(info.getCreator());
    config.setDescription(info.getDescription());
    config.setDocumentationUrl(info.getDocumentationUrl());
    config.setImage144x144(info.getImage144x144());
    config.setImage32x32(info.getImage32x32());
    config.setInfoUrl(info.getDocumentationUrl());
    config.setTags(info.getTags());
    config.setVendor(info.getCreator());
    return config;
  }
  
  public static GeneratorPluginConfiguration of (GeneratorPluginInfo info, String endpointUrl, String... tags) {
    GeneratorPluginConfiguration config = GeneratorPluginConfiguration.of(info.getKey(),"2",endpointUrl,tags);   
    config.setConfigKeys(info.getConfigKeys());
    
    if (info.getConfigTemplate() == null || info.getConfigTemplate().length() == 0) {
      config.setConfigTemplate(DEFAULT_CONFIG.getContent().toString());
    } else {
      config.setConfigTemplate(info.getConfigTemplate());
    }
    config.setName(info.getName());
    config.setCreator(info.getVendor());
    config.setDescription(info.getDescription());
    config.setDocumentationUrl(info.getDocumentationUrl());
    config.setImage144x144(info.getImage144x144());
    config.setImage32x32(info.getImage32x32());
    config.setInfoUrl(info.getDocumentationUrl());
    config.setVendor(info.getVendor());
    return config;
  }
   
  public String[] getTags() {
    return tags;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
  }

  public int getAmountOfDownloads() {
    return amountOfDownloads;
  }

  public void setAmountOfDownloads(int amountOfDownloads) {
    this.amountOfDownloads = amountOfDownloads;
  }

  public String getInfoUrl() {
    return this.getDocumentationUrl();
  }

  public void setInfoUrl(String infoUrl) {
    this.infoUrl = infoUrl;
  }
  
  public String getEndpointUrl() {
    return endpointUrl;
  }

  public void setEndpointUrl(String endpointUrl) {
    this.endpointUrl = endpointUrl;
  }

  public String getApiVersion() {
    return apiVersion;
  }

  public void setApiVersion(String apiVersion) {
    this.apiVersion = apiVersion;
  }

  public boolean isApiVersion(String apiVersion) {
    return this.apiVersion.equalsIgnoreCase(apiVersion);
  }
    
  @Deprecated
  public String getCreator() {
    return this.getVendor();
  }

  @Deprecated
  public void setCreator(String creator) {
    this.creator = creator;
  }
  
}
