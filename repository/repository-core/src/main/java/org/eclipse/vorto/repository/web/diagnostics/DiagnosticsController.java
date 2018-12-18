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
package org.eclipse.vorto.repository.web.diagnostics;

import java.util.Collection;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.IDiagnostics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/{tenant}/diagnostics")
public class DiagnosticsController {

  @Autowired
  private IDiagnostics modelDiagnostics;

  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Collection<Diagnostic> diagnose() {
    return modelDiagnostics.diagnoseAllModels();
  }

}
