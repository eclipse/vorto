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
