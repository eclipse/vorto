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
package org.eclipse.vorto.generators.runner;

import org.eclipse.vorto.codegen.bosch.BoschIoTSuiteGenerator;
import org.eclipse.vorto.codegen.ditto.EclipseDittoGenerator;
import org.eclipse.vorto.codegen.hono.EclipseHonoGenerator;
import org.eclipse.vorto.codegen.spi.config.AbstractGeneratorConfiguration;
import org.eclipse.vorto.codegen.spi.model.Generator;
import org.eclipse.vorto.codegen.template.CodeGeneratorTemplateGenerator;
import org.springframework.stereotype.Component;

@Component
public class GeneratorConfiguration extends AbstractGeneratorConfiguration {

  @Override
  protected void doSetup() {
    addGenerator(Generator.create("/generators/bosch.properties", BoschIoTSuiteGenerator.class,new BoschGeneratorConfigUI()));
    addGenerator(Generator.create("/generators/ditto.properties", EclipseDittoGenerator.class));
    addGenerator(Generator.create("/generators/hono.properties", EclipseHonoGenerator.class));
    addGenerator(Generator.create("/generators/generator_template.properties",
        CodeGeneratorTemplateGenerator.class));
  }
  
  
}
