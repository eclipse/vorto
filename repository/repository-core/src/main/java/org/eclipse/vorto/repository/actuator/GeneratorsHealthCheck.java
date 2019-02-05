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

import org.eclipse.vorto.repository.generation.GeneratorInfo;
import org.eclipse.vorto.repository.generation.impl.Generator;
import org.eclipse.vorto.repository.generation.impl.IGeneratorLookupRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.client.RestTemplate;

public class GeneratorsHealthCheck implements HealthIndicator {

   private final IGeneratorLookupRepository registeredGeneratorsRepository;
   private final RestTemplate restTemplate;

   public GeneratorsHealthCheck( final IGeneratorLookupRepository registeredGeneratorsRepository,
         final RestTemplate restTemplate ) {
      this.registeredGeneratorsRepository = registeredGeneratorsRepository;
      this.restTemplate = restTemplate;
   }

   @Override
   public Health health() {
      final Iterable<Generator> generators = this.registeredGeneratorsRepository.findAll();
      for ( final Generator generator : generators ) {
    	  try {
    		  restTemplate.getForEntity( generator.getGenerationInfoUrl() + "?includeConfigUI={includeConfigUI}",
    	                     GeneratorInfo.class, false );
    	  } catch (Exception exception) {
    		  return Health.down()
                      .withDetail( "Generator down.", String.format( "Generator Key: %s", generator.getKey() ) )
                      .build();
    	  }
      }
      return Health.up().build();
   }
}
