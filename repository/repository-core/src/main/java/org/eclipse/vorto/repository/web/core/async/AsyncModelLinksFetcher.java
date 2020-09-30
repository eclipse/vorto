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
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.web.api.v1.dto.ModelLink;

/**
 * Fetches the given model's {@link ModelLink}s asynchronously, and populates the
 * target {@link Collection}.
 */
public class AsyncModelLinksFetcher extends AsyncModelTaskRunner {

  private final ModelId id;
  private final Collection<ModelLink> target;

  public AsyncModelLinksFetcher(ModelId id, Collection<ModelLink> target) {
    this.id = id;
    this.target = target;
  }

  @Override
  public void run() {
    super.run();
    target.addAll(factory.getRepositoryByModelWithoutSessionHelper(id).getLinks(id));
  }
}
