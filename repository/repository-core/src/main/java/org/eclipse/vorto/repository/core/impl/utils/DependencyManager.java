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
package org.eclipse.vorto.repository.core.impl.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.ModelInfo;



public class DependencyManager {

  private Set<ModelInfo> resources = new HashSet<>();

  public DependencyManager() {

  }

  public DependencyManager(Set<ModelInfo> resources) {
    this.resources = resources;
  }

  public void addResource(ModelInfo resource) {
    this.resources.add(resource);
  }

  public List<ModelInfo> getSorted() {
    List<ModelInfo> sorted = new ArrayList<>();

    for (ModelInfo resource : resources) {
      addResourceRecursive(resource, sorted);
    }
    return sorted;
  }

  private void addResourceRecursive(ModelInfo resource, List<ModelInfo> sorted) {
    for (ModelId reference : resource.getReferences()) {
      ModelInfo referencedResource = findResource(reference);
      if (referencedResource != null) {
        addResourceRecursive(referencedResource, sorted);
      }
    }
    if (!sorted.contains(resource)) {
      sorted.add(resource);
    }
  }

  private ModelInfo findResource(final ModelId modelId) {
    for (ModelInfo resource : this.resources) {
      if (resource.getId().equals(modelId)) {
        return resource;
      }
    }
    return null;
  }
}
