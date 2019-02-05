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
import org.eclipse.vorto.model.ModelId;

public class DefaultMappedElement implements IMappedElement {

  protected String targetPlatformKey;

  protected List<Stereotype> stereotypes = new ArrayList<Stereotype>();

  protected ModelId mappingReference = null;

  public String getTargetPlatformKey() {
    return targetPlatformKey;
  }

  public void setTargetPlatformKey(String targetPlatformKey) {
    this.targetPlatformKey = targetPlatformKey;
  }

  @Override
  public List<Stereotype> getStereotypes() {
    return stereotypes;
  }

  @Override
  public Optional<Stereotype> getStereotype(String name) {
    return this.stereotypes.stream().filter(s -> s.getName().equalsIgnoreCase(name)).findAny();
  }

  public void addStereotype(Stereotype stereotype) {
    this.stereotypes.add(stereotype);
  }

  public void setStereotypes(List<Stereotype> stereotypes) {
    this.stereotypes = stereotypes;
  }

  public void setMappingReference(ModelId mappingReference) {
    this.mappingReference = mappingReference;
  }

  public ModelId getMappingReference() {
    return this.mappingReference;
  }
}
