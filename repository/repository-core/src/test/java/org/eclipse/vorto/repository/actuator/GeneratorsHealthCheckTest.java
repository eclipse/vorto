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
import java.util.Map;

import org.apache.http.HttpStatus;
import org.eclipse.vorto.repository.TestConfig;
import org.eclipse.vorto.repository.generation.impl.Generator;
import org.eclipse.vorto.repository.generation.impl.IGeneratorLookupRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.client.WireMock;

@RunWith( SpringRunner.class )
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@ContextConfiguration( classes = { TestConfig.class, GeneratorsHealthCheck.class, RestTemplate.class },
                       loader = AnnotationConfigContextLoader.class )
@AutoConfigureWireMock( port = 8888 )
public class GeneratorsHealthCheckTest {

   private final String infoEndpointResponse = "{\n" +
         "	\"key\": \"boschiotsuite\",\n" +
         "	\"name\": \"Bosch IoT Suite\",\n" +
         "	\"description\": \"Generates device code that either runs on the Bosch IoT Gateway SW or connects directly to the Bosch IoT Hub.\",\n"
         +
         "	\"organisation\": \"Eclipse Vorto Team\"\n" +
         "}";

   @MockBean
   private IGeneratorLookupRepository registeredGeneratorsRepository;

   @Autowired
   private GeneratorsHealthCheck generatorsHealthCheck;

   private final ArrayList<Generator> generators = new ArrayList<>();

   @Before
   public void setUp() {
      final Generator testGeneratorOne = new Generator( "test-generator-one",
            "http://localhost:8888/test-generator-one", "test" );
      final Generator testGeneratorTwo = new Generator( "test-generator-two",
            "http://localhost:8888/test-generator-two", "test" );

      generators.add( testGeneratorOne );
      generators.add( testGeneratorTwo );

      Mockito.when( registeredGeneratorsRepository.findAll() ).thenReturn( generators );
      createTestGeneratorOneInfoEndpointStub();
   }

   @Test
   public void testApplicationHealthyExpectStatusUp() {
      createTestGeneratorTwoInfoEndpointSuccessStub();
      assertEquals( Health.up().build(), generatorsHealthCheck.health() );
   }

   @Test
   public void testGeneratorNotFoundExpectStatusDown() {
      createTestGeneratorTwoInfoEndpointFailureStub( HttpStatus.SC_NOT_FOUND );
      final Health health = generatorsHealthCheck.health();
      assertEquals( health.getStatus(), Health.down().build().getStatus() );

      final Map<String, Object> healthDetails = health.getDetails();
      assertEquals( healthDetails.size(), 1 );
      assertTrue( healthDetails.containsKey( "Generator down." ) );
      assertEquals( "Generator Key: test-generator-two", healthDetails.get( "Generator down." ) );
   }

   @Test
   public void testGeneratorInternalServerErrorExpectStatusDown() {
      createTestGeneratorTwoInfoEndpointFailureStub( HttpStatus.SC_INTERNAL_SERVER_ERROR );
      final Health health = generatorsHealthCheck.health();
      assertEquals( health.getStatus(), Health.down().build().getStatus() );

      final Map<String, Object> healthDetails = health.getDetails();
      assertEquals( healthDetails.size(), 1 );
      assertTrue( healthDetails.containsKey( "Generator down." ) );
      assertEquals( "Generator Key: test-generator-two", healthDetails.get( "Generator down." ) );
   }

   private void createTestGeneratorOneInfoEndpointStub() {
      WireMock.stubFor( WireMock.get( "/test-generator-one/info?includeConfigUI=false" )
                                .willReturn( WireMock.aResponse()
                                                     .withBody( infoEndpointResponse )
                                                     .withHeader( HttpHeaders.CONTENT_TYPE,
                                                           MediaType.APPLICATION_JSON_VALUE )
                                                     .withStatus( HttpStatus.SC_OK ) ) );
   }

   private void createTestGeneratorTwoInfoEndpointSuccessStub() {
      WireMock.stubFor( WireMock.get( "/test-generator-two/info?includeConfigUI=false" )
                                .willReturn( WireMock.aResponse().withStatus( HttpStatus.SC_OK ) ) );
   }

   private void createTestGeneratorTwoInfoEndpointFailureStub( final int httpStatus ) {
      WireMock.stubFor( WireMock.get( "/test-generator-two/info?includeConfigUI=false" )
                                .willReturn( WireMock.aResponse().withStatus( httpStatus ) ) );
   }
}
