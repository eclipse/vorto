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
package org.eclipse.vorto.repository.plugin.generator.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Entity
@Table(name = "generator")
public class GeneratorMetric {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String generatorKey;

  @NotNull
  private String classifier;

  @NotNull
  private Integer invocationCount;

  public GeneratorMetric() {

  }
  
  public GeneratorMetric(String generatorKey) {
    this.generatorKey = generatorKey;
    this.invocationCount = 0;
    this.classifier = "platform";
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


  @Override
  public String toString() {
    return "Generator [id=" + id + ", generatorKey=" + generatorKey 
        + ", classifier=" + classifier + ", invocationCount=" + invocationCount + "]";
  }


}
