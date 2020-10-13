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

import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Base class for asynchronous model fetching operations of type {@link Runnable}.<br/>
 * Provides some out-of-the-box fields common to all asynchronous tasks. <br/>
 * Usage:
 * <ul>
 *   <li>
 *     Implementors <b>must</b> invoke {@code super.run()} in their {@code run} implementation, in
 *     order to propagate the {@link SecurityContext}, {@link RequestAttributes} and
 *     {@link IModelRepositoryFactory} to the running thread.
 *   </li>
 *   <li>
 *     Initialization of this or all inheriting classes <b>must</b> populate all 3 fields as
 *     non-{@code null} values, i.e. all {@code with...} methods must be chain-invoked with valid
 *     values.
 *   </li>
 * </ul>
 */
public abstract class AsyncModelTaskRunner extends Thread {

  protected SecurityContext context;
  protected RequestAttributes attributes;
  protected IModelRepositoryFactory factory;

  public AsyncModelTaskRunner with(SecurityContext context) {
    this.context = context;
    return this;
  }

  public AsyncModelTaskRunner with(RequestAttributes attributes) {
    this.attributes = attributes;
    return this;
  }

  public AsyncModelTaskRunner with(IModelRepositoryFactory factory) {
    this.factory = factory;
    return this;
  }

  protected void setContextAndAttributes() {
    SecurityContextHolder.setContext(context);
    RequestContextHolder.setRequestAttributes(attributes, true);
  }

  @Override
  public void run() {
    setContextAndAttributes();
  }
}
