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
package org.eclipse.vorto.repository.init;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "repo.config")
public class PredefinedTenants {
  private List<PredefinedTenant> predefinedTenants;

  public List<PredefinedTenant> getPredefinedTenants() {
    return predefinedTenants;
  }

  public void setPredefinedTenants(List<PredefinedTenant> predefinedTenants) {
    this.predefinedTenants = predefinedTenants;
  }
}
