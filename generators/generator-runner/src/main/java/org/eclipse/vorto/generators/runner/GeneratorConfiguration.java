/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
    addGenerator(Generator.create("/generators/bosch.properties", BoschIoTSuiteGenerator.class));
    addGenerator(Generator.create("/generators/ditto.properties", EclipseDittoGenerator.class));
    addGenerator(Generator.create("/generators/hono.properties", EclipseHonoGenerator.class));
    addGenerator(Generator.create("/generators/generator_template.properties",
        CodeGeneratorTemplateGenerator.class));
  }
}
