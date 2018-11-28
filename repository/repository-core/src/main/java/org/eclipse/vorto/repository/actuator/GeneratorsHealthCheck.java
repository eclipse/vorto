/*
 * Copyright (c) 2018 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.eclipse.vorto.repository.actuator;

import org.eclipse.vorto.repository.generation.GeneratorInfo;
import org.eclipse.vorto.repository.generation.impl.Generator;
import org.eclipse.vorto.repository.generation.impl.IGeneratorLookupRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
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
         final ResponseEntity<GeneratorInfo> generatorInfoResponseEntity = restTemplate
               .getForEntity( generator.getGenerationInfoUrl() + "?includeConfigUI={includeConfigUI}",
                     GeneratorInfo.class, false );

         if ( !generatorInfoResponseEntity.getStatusCode().is2xxSuccessful() ) {
            return Health.down()
                         .withDetail( "Generator down.", String.format( "Generator Key: %s", generator.getKey() ) )
                         .build();
         }
      }
      return Health.up().build();
   }
}
