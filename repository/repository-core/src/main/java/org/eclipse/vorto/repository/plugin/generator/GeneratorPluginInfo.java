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

import java.util.Set;
import org.eclipse.vorto.repository.plugin.PluginInfo;

public class GeneratorPluginInfo extends PluginInfo {

  private String image32x32;
  private String image144x144;
  private String[] tags;
  private int amountOfDownloads;
  private String configTemplate = null;
  private Set<String> configKeys = null;
  private String apiVersion;
  
  @Deprecated
  private String creator;
  @Deprecated
  private String infoUrl;
  
  public GeneratorPluginInfo() {
    
  }
  
  public GeneratorPluginInfo(String key, String apiVersion, String endpointUrl, String...tags) {
    this.setKey(key);
    this.setApiVersion(apiVersion);
    this.setBaseEndpointUrl(endpointUrl);
    if (tags != null && tags.length > 0) {
      this.setTags(tags);
    } else {
      this.setTags(new String[] {"production"});
    } 
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
