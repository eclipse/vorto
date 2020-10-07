/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.web.core.async;

import java.util.Collection;
import java.util.Map;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.web.api.v1.dto.ModelMappingDTO;
import org.eclipse.vorto.repository.web.api.v1.dto.ModelMinimalInfoDTO;

/**
 * Fetches model mappings asynchronously, and populates the target {@link Collection}.
 */
public class AsyncModelMappingsFetcher extends AsyncModelTaskRunner {

  private final Map.Entry<String, String> entry;
  private final Collection<ModelMinimalInfoDTO> target;

  public AsyncModelMappingsFetcher(Collection<ModelMinimalInfoDTO> target,
      Map.Entry<String, String> entry) {
    this.target = target;
    this.entry = entry;
  }

  @Override
  public void run() {
    super.run();
    ModelId id = ModelId.fromPrettyFormat(entry.getKey());
    target.add(
        ModelMappingDTO.fromModelInfo(
            entry.getValue(),
            factory
                .getRepositoryByModelWithoutSessionHelper(id)
                .getByIdWithPlatformMappings(id)
        )
    );
  }
}
