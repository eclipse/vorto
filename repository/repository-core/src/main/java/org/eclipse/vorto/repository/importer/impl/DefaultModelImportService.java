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
