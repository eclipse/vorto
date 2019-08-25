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
package org.eclipse.vorto.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class FunctionblockModel extends AbstractModel implements IReferenceType {

  private List<ModelProperty> configurationProperties = new ArrayList<>();
  private List<ModelProperty> statusProperties = new ArrayList<>();
  private List<ModelProperty> faultProperties = new ArrayList<>();
  private List<ModelEvent> events = new ArrayList<>();

  private List<Operation> operations = new ArrayList<>();

  public FunctionblockModel(ModelId modelId) {
    super(modelId, ModelType.Functionblock);
  }

  protected FunctionblockModel() {

  }
  
  public static FunctionblockModelBuilder Builder(ModelId id) {
    return new FunctionblockModelBuilder(id);
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
  
  @JsonIgnore
  public List<ModelProperty> getProperties(){
    List<ModelProperty> properties = new ArrayList<>();
    properties.addAll(getStatusProperties());
    properties.addAll(getConfigurationProperties());
    return properties;
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

  public static class FunctionblockModelBuilder extends Builder<FunctionblockModel> {

    private FunctionblockModelBuilder(ModelId id) {
      super(new FunctionblockModel(id));
    }

    public FunctionblockModelBuilder statusProperty(ModelProperty property) {
      this.model.getStatusProperties().add(property);
      return this;
    }
    
    public FunctionblockModelBuilder configurationProperty(ModelProperty property) {
      this.model.getConfigurationProperties().add(property);
      return this;
    }
    
    public FunctionblockModelBuilder event(ModelEvent event) {
      this.model.getEvents().add(event);
      return this;
    }
    
    public FunctionblockModelBuilder operation(Operation operation) {
      this.model.getOperations().add(operation);
      return this;
    }
  }
}
