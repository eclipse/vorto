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
package org.eclipse.vorto.repository.workflow.model;

import java.util.Map;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;

public interface IWorkflowFunction {

  // TODO #2529 this should be overloaded without IUserContext, and a workspace ID not tied to the
  // user context should be added instead to the signature, then implementors should be refactored
  @Deprecated
  void execute(ModelInfo model, IUserContext user, Map<String,Object> context);
}
