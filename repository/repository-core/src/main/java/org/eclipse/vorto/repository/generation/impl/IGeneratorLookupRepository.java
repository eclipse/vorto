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
package org.eclipse.vorto.repository.generation.impl;

import java.util.List;
import org.eclipse.vorto.repository.domain.Generator;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public interface IGeneratorLookupRepository extends CrudRepository<Generator, Long> {

  /**
   * finds all generators by the specific generator service key
   * 
   * @param generatorKey
   * @return
   */
  List<Generator> findByGeneratorKey(String generatorKey);

  /**
   * Finds all generators by either platform or documentation classifier
   * 
   * @param classifier
   * @return
   */
  List<Generator> findByClassifier(String classifier);
}
