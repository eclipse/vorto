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
package org.eclipse.vorto.utilities.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.vorto.core.api.model.model.Model;

public class DefaultModelWorkspace implements IModelWorkspace {

  private List<Model> models = new ArrayList<Model>();

  public void addModels(List<Model> models) {
    this.models.addAll(models);
  }

  @Override
  public List<Model> get() {
    return Collections.unmodifiableList(this.models);
  }

}
