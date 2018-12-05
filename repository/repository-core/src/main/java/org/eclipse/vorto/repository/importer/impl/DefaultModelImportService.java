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
package org.eclipse.vorto.repository.importer.impl;

import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.repository.importer.IModelImportService;
import org.eclipse.vorto.repository.importer.IModelImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultModelImportService implements IModelImportService {

  @Autowired
  private List<IModelImporter> modelImporters;

  @Override
  public List<IModelImporter> getImporters() {
    return modelImporters;
  }

  @Override
  public Optional<IModelImporter> getImporterByKey(final String key) {
    return modelImporters.stream().filter(p -> p.getKey().equals(key)).findFirst();
  }

}
