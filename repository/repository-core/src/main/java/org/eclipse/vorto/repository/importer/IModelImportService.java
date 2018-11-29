/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.importer;

import java.util.List;
import java.util.Optional;

public interface IModelImportService {
  /**
   * 
   * @return a list of all registered model importers
   */
  List<IModelImporter> getImporters();

  /**
   * Gets an importer by its key
   * 
   * @param key
   * @return
   */
  Optional<IModelImporter> getImporterByKey(String key);
}
