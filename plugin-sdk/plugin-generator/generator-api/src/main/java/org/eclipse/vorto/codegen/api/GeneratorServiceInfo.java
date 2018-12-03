/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.api;

import java.util.Set;

/**
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class GeneratorServiceInfo {

  private String key;
  private String name;
  private String description;
  private String creator;
  private String documentationUrl;
  private String image32x32;
  private String image144x144;
  private ServiceClassifier classifier;
  private String[] tags;

  private String configTemplate = null;
  private Set<String> configKeys = null;

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

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getDocumentationUrl() {
    return documentationUrl;
  }

  public void setDocumentationUrl(String documentationUrl) {
    this.documentationUrl = documentationUrl;
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

  public ServiceClassifier getClassifier() {
    return classifier;
  }

  public void setClassifier(ServiceClassifier classifier) {
    this.classifier = classifier;
  }

  public String[] getTags() {
    return tags;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
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



}
