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
package org.eclipse.vorto.plugin.importer;

import org.eclipse.vorto.plugin.PluginInfo;

public class ImporterPluginInfo extends PluginInfo {

  private String fileType;
  
  protected ImporterPluginInfo() {    
  }
  
  public ImporterPluginInfo(String pluginkey, String name, String description, String vendor, String fileType) {
    setKey(pluginkey);
    setName(name);
    setDescription(description);
    setVendor(vendor);
    setFileType(fileType);
    
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }
 
  
}
