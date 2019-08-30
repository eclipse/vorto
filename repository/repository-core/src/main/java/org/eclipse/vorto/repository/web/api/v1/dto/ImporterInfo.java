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
package org.eclipse.vorto.repository.web.api.v1.dto;

import java.util.LinkedHashSet;
import java.util.Set;

public class ImporterInfo {

  private String key;

  private Set<String> extensionTypes = new LinkedHashSet<>();

  private String shortDescription;

  public ImporterInfo(String key, Set<String> extensionTypes, String shortDescription) {
    this.key = key;
    this.extensionTypes = extensionTypes;
    this.shortDescription = shortDescription;
  }

  protected ImporterInfo() {

  }

  public Set<String> getExtensionTypes() {
    return extensionTypes;
  }

  public void setExtensionTypes(Set<String> extensionTypes) {
    this.extensionTypes = extensionTypes;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }


}
