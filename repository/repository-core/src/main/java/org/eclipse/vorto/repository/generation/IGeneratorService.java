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
package org.eclipse.vorto.repository.generation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.eclipse.vorto.model.ModelId;

/**
 * This service is a facade service to communicate to all registered Vorto Generators
 * 
 * @author Alexander Edelmann
 *
 */
public interface IGeneratorService {

  /**
   * registers a vorto generator with the given key and baseUrl
   * 
   * @param serviceKey
   * @param baseUrl
   * @param classifier throws GeneratorAlreadyExistsException thrown, if a generator with the same
   *        serviceKey already exists
   */
  void registerGenerator(String serviceKey, String baseUrl);

  /**
   * unregisters a generator with the specific serviceKey. Unregistered generators can no longer be
   * used to generate any code from information models.
   * 
   * @param serviceKey
   */
  void unregisterGenerator(String serviceKey);

  /**
   * Gets a list of currently registered code generator services
   * 
   * @return a list of service keys of generators
   */
  Set<String> getRegisteredGeneratorServiceKeys();

  /**
   * Gets more information about the generator, such as description, rating, creator etc.
   * 
   * @param serviceKey
   * @param includeUITemplate
   * @return
   */
  GeneratorInfo getGeneratorServiceInfo(String serviceKey, boolean includeUITemplate);

  /**
   * Returns the mostly used generators
   * 
   * @param top the upper limit of results, e.g. top 3 or top 10
   * @return
   */
  Collection<GeneratorInfo> getMostlyUsedGenerators(int top);

  /**
   * Invokes a code generator for a information model Id
   * 
   * @param modelId information model Id
   * @param serviceKey target code generator
   * @param requestParams request params, can be null
   * @return actual generated output
   * @throws GenerationException if something goes wrong during code generation
   */
  GeneratedOutput generate(ModelId modelId, String serviceKey, Map<String, String> requestParams);

  /**
   * Invokes a generator for the given serviceKey
   * 
   * @param serviceKey
   * @param requestParams
   * @return actual generated output
   * @throws GenerationException if something goes wrong during code generation
   */
  GeneratedOutput generate(String serviceKey, Map<String, String> requestParams);
}
