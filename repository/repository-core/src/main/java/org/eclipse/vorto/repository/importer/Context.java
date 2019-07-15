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
package org.eclipse.vorto.repository.importer;

import java.util.Optional;
import org.eclipse.vorto.repository.core.IUserContext;

public class Context {

  private IUserContext user;
  private Optional<String> targetNamespace;
  
  public static Context create(IUserContext user, Optional<String> targetNamespace) {
    return new Context(user,targetNamespace);
  }
  
  protected Context(IUserContext user, Optional<String> targetNamespace) {
    this.user = user;
    this.targetNamespace = targetNamespace;
  }

  public IUserContext getUser() {
    return user;
  }

  public Optional<String> getTargetNamespace() {
    return targetNamespace;
  }
  
  
}
