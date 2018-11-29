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
package org.eclipse.vorto.repository.generation.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Entity
public class Generator {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String generatorKey;

  @NotNull
  private String baseUrl;

  @NotNull
  private String classifier;

  @NotNull
  private Integer invocationCount;

  public Generator() {

  }

  public Generator(String generatorKey, String baseUrl, String classifier) {
    this.generatorKey = generatorKey;
    this.baseUrl = baseUrl;
    this.classifier = classifier;
    this.invocationCount = 0;
  }


  public Integer increaseInvocationCount() {
    return ++invocationCount;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getKey() {
    return generatorKey;
  }

  public void setKey(String generatorKey) {
    this.generatorKey = generatorKey;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getClassifier() {
    return classifier;
  }

  public void setClassifier(String classifier) {
    this.classifier = classifier;
  }

  public int getInvocationCount() {
    return invocationCount;
  }

  public void setInvocationCount(Integer invocationCount) {
    this.invocationCount = invocationCount;
  }

  public String getGenerationEndpointUrl() {
    return baseUrl + "/{namespace}/{name}/{version}";
  }

  public String getGenerationInfoUrl() {
    return baseUrl + "/info";
  }

  public String getInfraGenerationEndpointUrl() {
    return baseUrl;
  }

  @Override
  public String toString() {
    return "Generator [id=" + id + ", generatorKey=" + generatorKey + ", baseUrl=" + baseUrl
        + ", classifier=" + classifier + ", invocationCount=" + invocationCount + "]";
  }


}
