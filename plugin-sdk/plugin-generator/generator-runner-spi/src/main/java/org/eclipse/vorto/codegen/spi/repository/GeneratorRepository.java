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
package org.eclipse.vorto.codegen.spi.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import org.eclipse.vorto.codegen.api.IGeneratorLookup;
import org.eclipse.vorto.codegen.spi.model.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GeneratorRepository {

  @Autowired
  private IGeneratorLookup generatorLookup;

  private Collection<Generator> generators = new ArrayList<Generator>();

  public Collection<Generator> list() {
    return generators;
  }

  public Optional<Generator> get(String key) {
    return generators.stream().filter(generator -> generator.getInfo().getKey().equals(key))
        .findFirst();
  }

  public void add(Generator generator) {
    generators.add(generator);
  }

  public IGeneratorLookup newGeneratorLookup() {
    return generatorLookup;
  }

}
