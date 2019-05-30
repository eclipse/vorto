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
package org.eclipse.vorto.repository.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

/**
 * TODO: As generators do not implement a /info endpoint anymore, this health check for generators need to be thought through again
 *
 */
public class GeneratorsHealthCheck implements HealthIndicator {

   public GeneratorsHealthCheck() {
   }

   @Override
   public Health health() {
      return Health.up().build();
   }
}
