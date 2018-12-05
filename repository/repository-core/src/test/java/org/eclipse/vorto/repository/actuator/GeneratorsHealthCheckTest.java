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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.vorto.repository.generation.GeneratorInfo;
import org.eclipse.vorto.repository.generation.impl.Generator;
import org.eclipse.vorto.repository.generation.impl.IGeneratorLookupRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GeneratorsHealthCheckTest {

   private final RestTemplate restTemplate = Mockito.mock( RestTemplate.class );
   private final IGeneratorLookupRepository registeredGeneratorsRepository = Mockito
         .mock( IGeneratorLookupRepository.class );
   private final GeneratorsHealthCheck generatorsHealthCheck = new GeneratorsHealthCheck(
         registeredGeneratorsRepository, restTemplate );

   private final ResponseEntity<GeneratorInfo> generatorInfoResponseEntitySuccess = Mockito
         .mock( ResponseEntity.class );

   private final ResponseEntity<GeneratorInfo> generatorInfoResponseEntityFailure = Mockito
         .mock( ResponseEntity.class );

   @Before
   public void setUp() {
      Mockito.when( generatorInfoResponseEntitySuccess.getStatusCode() ).thenReturn( HttpStatus.OK );
      Mockito.when( generatorInfoResponseEntityFailure.getStatusCode() ).thenReturn( HttpStatus.NOT_FOUND );
   }

   @Test
   public void testNoGeneratorsRegisteredExpectStatusUp() {
      Mockito.when( registeredGeneratorsRepository.findAll() ).thenReturn( new ArrayList<>() );
      assertEquals( generatorsHealthCheck.health(), Health.up().build() );
   }

   @Test
   public void testGeneratorsAvailableExpectStatusUp() {
      final List<Generator> generators = new ArrayList<>();
      generators.add( new Generator() );
      generators.add( new Generator() );

      Mockito.when( registeredGeneratorsRepository.findAll() ).thenReturn( generators );

      Mockito.when( restTemplate
            .getForEntity( Matchers.anyString(), Matchers.eq( GeneratorInfo.class ), Matchers.eq( false ) ) )
             .thenReturn( generatorInfoResponseEntitySuccess );

      assertEquals( generatorsHealthCheck.health(), Health.up().build() );
   }

   @Test
   public void testGeneratorNotAvailableExpectStatusDown() {
      final List<Generator> generators = new ArrayList<>();
      generators.add( createGenerator( "https://gen-one", "genOne" ) );
      generators.add( createGenerator( "https://gen-two", "genTwo" ) );

      Mockito.when( registeredGeneratorsRepository.findAll() ).thenReturn( generators );

      Mockito.when( restTemplate
            .getForEntity( Matchers.eq( "https://gen-one/info?includeConfigUI={includeConfigUI}" ),
                  Matchers.eq( GeneratorInfo.class ), Matchers.eq( false ) ) )
             .thenReturn( generatorInfoResponseEntitySuccess );

      Mockito.when( restTemplate
            .getForEntity( Matchers.eq( "https://gen-two/info?includeConfigUI={includeConfigUI}" ),
                  Matchers.eq( GeneratorInfo.class ), Matchers.eq( false ) ) )
             .thenReturn( generatorInfoResponseEntityFailure );

      final Health health = generatorsHealthCheck.health();
      assertEquals( health.getStatus(), Health.down().build().getStatus() );

      final Map<String, Object> healthDetails = health.getDetails();
      assertEquals( healthDetails.size(), 1 );
      assertTrue( healthDetails.containsKey( "Generator down." ) );
      assertEquals( healthDetails.get( "Generator down." ), "Generator Key: genTwo" );
   }

   private Generator createGenerator( final String baseUrl, final String key ) {
      final Generator generator = new Generator();
      generator.setBaseUrl( baseUrl );
      generator.setKey( key );
      return generator;
   }
}
