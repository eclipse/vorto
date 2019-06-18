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
package org.eclipse.vorto.plugin;

public abstract class PluginInfo {

  private String key;
  private String name;
  private String description;
  private String vendor;
  private String documentationUrl;
  
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getVendor() {
    return vendor;
  }

  public void setVendor(String vendor) {
    this.vendor = vendor;
  }

  public String getDocumentationUrl() {
    return documentationUrl;
  }

  public void setDocumentationUrl(String documentationUrl) {
    this.documentationUrl = documentationUrl;
  }
  
  public static class PluginBuilder<T extends PluginInfo> { 
    
    protected T info;
    
    public PluginBuilder(T info) {
      this.info = info;
    }
    
    public PluginBuilder<T> withDescription(String description) {
      info.setDescription(description);
      return this;
    }
    
    public PluginBuilder<T> withName(String name) {
      info.setName(name);
      return this;
    }
    
    public PluginBuilder<T> withVendor(String vendor) {
      info.setVendor(vendor);
      return this;
    }
    
    public PluginBuilder<T> withDocumentationUrl(String documentationUrl) {
      info.setDocumentationUrl(documentationUrl);
      return this;
    }
    
    public T build() {
      return info;
    }
  }
}
