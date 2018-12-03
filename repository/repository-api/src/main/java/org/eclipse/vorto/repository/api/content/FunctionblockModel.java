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
package org.eclipse.vorto.repository.api.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.repository.api.AbstractModel;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;

@Deprecated
public class FunctionblockModel extends AbstractModel {

  private List<ModelProperty> configurationProperties = new ArrayList<>();
  private List<ModelProperty> statusProperties = new ArrayList<>();
  private List<ModelProperty> faultProperties = new ArrayList<>();
  private List<ModelEvent> events = new ArrayList<>();

  private List<Operation> operations = new ArrayList<>();

  public FunctionblockModel(ModelId modelId, ModelType modelType) {
    super(modelId, modelType);
  }

  protected FunctionblockModel() {

  }

  public List<ModelProperty> getConfigurationProperties() {
    return configurationProperties;
  }

  public void setConfigurationProperties(List<ModelProperty> configurationProperties) {
    this.configurationProperties = configurationProperties;
  }

  public List<ModelProperty> getStatusProperties() {
    return statusProperties;
  }

  public Optional<ModelProperty> getStatusProperty(String propertyName) {
    return statusProperties.stream().filter(p -> p.getName().equals(propertyName)).findAny();
  }

  public Optional<ModelProperty> getConfigurationProperty(String propertyName) {
    return configurationProperties.stream().filter(p -> p.getName().equals(propertyName)).findAny();
  }

  public Optional<ModelProperty> getFaultProperty(String propertyName) {
    return faultProperties.stream().filter(p -> p.getName().equals(propertyName)).findAny();
  }

  public void setStatusProperties(List<ModelProperty> statusProperties) {
    this.statusProperties = statusProperties;
  }

  @Deprecated
  /**
   * Use events instead
   * 
   * @return
   */
  public List<ModelProperty> getFaultProperties() {
    return faultProperties;
  }

  @Deprecated
  public void setFaultProperties(List<ModelProperty> faultProperties) {
    this.faultProperties = faultProperties;
  }

  public List<ModelEvent> getEvents() {
    return events;
  }

  public void setEvents(List<ModelEvent> events) {
    this.events = events;
  }

  public List<Operation> getOperations() {
    return operations;
  }

  public void setOperations(List<Operation> operations) {
    this.operations = operations;
  }

  @Override
  public String toString() {
    return "FunctionblockModelDto [configurationProperties=" + configurationProperties
        + ", statusProperties=" + statusProperties + ", faultProperties=" + faultProperties
        + ", events=" + events + ", operations=" + operations + "]";
  }

}
