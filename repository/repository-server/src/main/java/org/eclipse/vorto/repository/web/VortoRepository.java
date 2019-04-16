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
package org.eclipse.vorto.repository.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */

@SpringBootApplication
@EnableJpaRepositories("org.eclipse.vorto.repository")
@EntityScan("org.eclipse.vorto.repository")
@ComponentScan("org.eclipse.vorto.repository")
public class VortoRepository {

  public static void main(String[] args) {
    SpringApplication.run(VortoRepository.class, args);
  }
}
