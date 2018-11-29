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
package org.eclipse.vorto.repository.api;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.eclipse.vorto.repository.api.exception.GenerationException;
import org.eclipse.vorto.repository.api.exception.GeneratorNotFoundException;
import org.eclipse.vorto.repository.api.exception.ModelNotFoundException;
import org.eclipse.vorto.repository.api.generation.GeneratedOutput;
import org.eclipse.vorto.repository.api.generation.GeneratorInfo;

/**
 * Lets you generate platform specific code for Information Models that were found either via
 * {@link IModelRepository} or resolved via {@link IModelResolver}
 *
 */
public interface IModelGeneration {

  /**
   * Gets all available generators keys of generators that are currently registered in the
   * repository.
   * 
   * @return a list of generator keys
   */
  CompletableFuture<Set<String>> getAvailableGeneratorKeys();

  /**
   * Gives further information about the generator for a given generator key.
   * 
   * @param generatorKey
   * @return generator information
   * 
   * @throws GeneratorNotFoundException if no generator can be found for the given key
   */
  CompletableFuture<GeneratorInfo> getInfo(String generatorKey);

  /**
   * Generates platform-specific code for a given Vorto model id using a code generator
   * 
   * @param modelId
   * @param generatorKey
   * @param invocationParams
   * @return the generated platform - specific source code
   * 
   * @throws GeneratorNotFoundException if no generator can be found for the provided key
   * @throws ModelNotFoundException if no model exists in the repository with the specified model id
   * @throws GenerationException if something goes wrong during the generation
   */
  CompletableFuture<GeneratedOutput> generate(ModelId modelId, String generatorKey,
      Map<String, String> invocationParams);
}
