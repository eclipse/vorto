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
