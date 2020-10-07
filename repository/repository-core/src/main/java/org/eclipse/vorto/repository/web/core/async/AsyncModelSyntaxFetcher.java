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

import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.Callable;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Fetches the give model's syntax asynchronously.
 */
public class AsyncModelSyntaxFetcher implements Callable<String> {

  private ModelId id;
  private SecurityContext context;
  private RequestAttributes attributes;
  private IModelRepositoryFactory factory;

  public AsyncModelSyntaxFetcher(ModelId id, SecurityContext context, RequestAttributes attributes,
      IModelRepositoryFactory factory) {
    this.id = id;
    this.context = context;
    this.attributes = attributes;
    this.factory = factory;
  }

  @Override
  public String call() {
    SecurityContextHolder.setContext(context);
    RequestContextHolder.setRequestAttributes(attributes);

    return Base64
        .getEncoder()
        .encodeToString(
            factory
                .getRepositoryByModelWithoutSessionHelper(id)
                .getFileContent(id, Optional.empty())
                .map(FileContent::getContent)
                .orElse(new byte[0])
        );
  }
}
