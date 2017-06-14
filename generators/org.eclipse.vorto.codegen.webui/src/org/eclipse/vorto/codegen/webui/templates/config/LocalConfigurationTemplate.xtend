/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.webui.templates.config

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class LocalConfigurationTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''LocalConfiguration.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/config'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».config;
		
		import java.util.concurrent.TimeUnit;
		
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.beans.factory.annotation.Value;
		import org.springframework.context.annotation.Bean;
		import org.springframework.context.annotation.Configuration;
		import org.springframework.context.annotation.Profile;
		import org.springframework.security.oauth2.client.OAuth2ClientContext;
		import com.example.iot.«element.name.toLowerCase».service.DataService;
		
		«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
		import com.bosch.cr.integration.IntegrationClient;
		import com.bosch.cr.integration.client.IntegrationClientImpl;
		import com.bosch.cr.integration.client.configuration.AuthenticationConfiguration;
		import com.bosch.cr.integration.client.configuration.IntegrationClientConfiguration;
		import com.bosch.cr.integration.client.configuration.ProxyConfiguration;
		import com.bosch.cr.integration.client.configuration.ProxyConfiguration.ProxyOptionalSettable;
		import com.bosch.cr.integration.client.configuration.PublicKeyAuthenticationConfiguration;
		import com.bosch.cr.integration.client.configuration.TrustStoreConfiguration;
		import com.bosch.cr.integration.things.ThingIntegration;
		import com.example.iot.applewatch.service.bosch.BoschThingsDataService;
		import com.example.iot.applewatch.service.bosch.ThingClient;
		«ELSE»
		import com.example.iot.applewatch.service.sample.SampleDataService;
		«ENDIF»
				
		@Configuration
		public class LocalConfiguration {
			
			«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
			@Value("${bosch.things.endpointUrl}")
			private String thingEndpointUrl;
			
			@Value("${bosch.things.apiToken}")
			private String thingApiToken;
				
			@Value("${http.proxyHost}")
			private String proxyHost;
			
			@Value("${http.proxyPort}")
			private int proxyPort;
			
			@Value("${bosch.things.solutionid}")
			private String thingsSolutionId;
			
			@Value("${bosch.things.keystore.password}")
			private String keystorePassword;
			
			@Value("${bosch.things.alias}")
			private String alias;
			
			@Value("${bosch.things.alias.password}")
			private String aliasPassword;
			
			@Value("${bosch.things.keystoreLocation}")
			private String keystoreLocation;
			
			@Value("${bosch.things.trustStoreLocation}")
			private String trustStoreLocation;
			
			@Value("${bosch.things.trustStorePassword}")
			private String trustStorePassword;
			
			@Value("${bosch.things.wsEndpointUrl}")
			private String wsEndpointUrl;
			
			@Autowired
			private OAuth2ClientContext oauth2context;
			
			@Bean
			public DataService dataService() {
				return new BoschThingsDataService(thingClient(),getThingIntegration());
			}
			
			@Bean
			public ThingClient thingClient() {
				return ThingClient.newBuilder()
					.withProxy(proxyHost, proxyPort)
					.withEndpointUrl(thingEndpointUrl)
					.withApiToken(thingApiToken)
					.withOAuth2ClientContext(oauth2context)
					.build();
			}
			
			@Bean
			public ThingIntegration getThingIntegration() {
				/* build an authentication configuration */
				AuthenticationConfiguration authenticationConfiguration =
				   PublicKeyAuthenticationConfiguration.newBuilder()
				      .clientId(thingsSolutionId+":"+"iotconsole_local")
				      .keyStoreLocation(LocalConfiguration.class.getResource(keystoreLocation))
				      .keyStorePassword(keystorePassword)
				      .alias(alias)
				      .aliasPassword(aliasPassword)
				      .build();
				 
				/* configure a truststore that contains trusted certificates */
				TrustStoreConfiguration trustStore =
				   TrustStoreConfiguration.newBuilder().location(LocalConfiguration.class.getResource(trustStoreLocation)).password(trustStorePassword).build();
				 
				ProxyOptionalSettable proxyConfig = ProxyConfiguration.newBuilder().proxyHost(proxyHost).proxyPort(proxyPort);
				
				final IntegrationClientConfiguration integrationClientConfiguration =
				   IntegrationClientConfiguration.newBuilder()
				      .authenticationConfiguration(authenticationConfiguration)
				      .centralRegistryEndpointUrl(wsEndpointUrl)
				      .trustStoreConfiguration(trustStore)
				      .proxyConfiguration(proxyConfig.build())
				      .build();
						
				final IntegrationClient integrationClient = IntegrationClientImpl.newInstance(integrationClientConfiguration);
				 
				try {
					/* create a subscription for this client */
					integrationClient.subscriptions().create().get(30, TimeUnit.SECONDS);
			
					/*
					 * start consuming events that were triggered by the subscription,
					 * usually this method is called after all handlers are registered.
					 */
					integrationClient.subscriptions().consume().get(30, TimeUnit.SECONDS);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			
				return integrationClient.things();
			}
			«ELSE»
			@Bean
			public DataService dataService() {
				return return new SampleDataService();
			}
			«ENDIF»
		}
		'''
	}
	
}
