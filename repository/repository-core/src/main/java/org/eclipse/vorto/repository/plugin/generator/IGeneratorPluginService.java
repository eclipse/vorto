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
package org.eclipse.vorto.repository.plugin.generator;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IUserContext;

/**
 * This service is a facade service to communicate to all registered Vorto Generators
 * 
 * @author Alexander Edelmann
 *
 */
public interface IGeneratorPluginService {

  /**
   * Gets a list of currently registered code generator services
   * 
   * @return a list of service keys of generators
   */
  Set<String> getKeys();

  /**
   * Gets more information about the generator, such as description, rating, creator etc.
   * 
   * @param pluginKey
   * @param includeUITemplate
   * @return
   */
  GeneratorPluginConfiguration getPluginInfo(String pluginKey, boolean includeUITemplate);

  /**
   * Returns the mostly used generators
   * 
   * @param top the upper limit of results, e.g. top 3 or top 10
   * @return
   */
  Collection<GeneratorPluginConfiguration> getMostlyUsed(int top);

  /**
   * Invokes a code generator for a information model Id
   * 
   * @param modelId information model Id
   * @param serviceKey target code generator
   * @param requestParams request params, can be null
   * @return actual generated output
   * @throws GenerationException if something goes wrong during code generation
   */
  GeneratedOutput generate(IUserContext userContext, ModelId modelId, String serviceKey, Map<String, String> requestParams);

}
