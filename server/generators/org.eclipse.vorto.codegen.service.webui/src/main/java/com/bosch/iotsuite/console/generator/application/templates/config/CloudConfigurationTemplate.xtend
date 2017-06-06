package com.bosch.iotsuite.console.generator.application.templates.config

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import com.bosch.iotsuite.console.generator.application.templates.TemplateUtils

class CloudConfigurationTemplate implements IFileTemplate<InformationModel> {
	
	
	override getFileName(InformationModel context) {
		'''CloudConfiguration.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/config'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».config;
		
		import java.util.concurrent.TimeUnit;
		
		import org.springframework.beans.factory.annotation.Value;
		import org.springframework.context.annotation.Bean;
		import org.springframework.context.annotation.Configuration;
		import org.springframework.context.annotation.Profile;
		import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
		import org.springframework.security.core.Authentication;
		import org.springframework.security.core.context.SecurityContextHolder;
		
		import com.bosch.cr.integration.IntegrationClient;
		import com.bosch.cr.integration.client.IntegrationClientImpl;
		import com.bosch.cr.integration.client.configuration.AuthenticationConfiguration;
		import com.bosch.cr.integration.client.configuration.IntegrationClientConfiguration;
		import com.bosch.cr.integration.client.configuration.ProxyConfiguration;
		import com.bosch.cr.integration.client.configuration.ProxyConfiguration.ProxyOptionalSettable;
		import com.bosch.cr.integration.client.configuration.PublicKeyAuthenticationConfiguration;
		import com.bosch.cr.integration.client.configuration.TrustStoreConfiguration;
		import com.bosch.cr.integration.things.ThingIntegration;
		import com.bosch.im.api2.jwt.IAuthorizationToken;
		import com.bosch.iotsuite.data.permissions.model.IMUserInfo;
		import com.bosch.iotsuite.management.things.AuthorizationTokenProvider;
		import com.bosch.iotsuite.management.things.ThingClient;
		
		@Configuration
		@Profile("cloud")
		public class CloudConfiguration {
			
			@Value("${bosch.things.endpointUrl}")
			private String thingEndpointUrl;
			
			@Value("${bosch.things.apiToken}")
			private String thingApiToken;
				
			@Value("${http.proxyHost}")
			private String proxyHost;
			
			@Value("${http.proxyPort}")
			private int proxyPort;
			
			@Bean
			public ThingClient thingClient() {
				return ThingClient.newBuilder()
						.withEndpointUrl(thingEndpointUrl)
						.withCredentialsProvider(authorizationProvider())
						.build();
			}
				
			@Bean
			public AuthorizationTokenProvider authorizationProvider() {
				return new AuthorizationTokenProvider() {
					
					@Override
					public String getJwtToken() {
						Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
						if (autentication instanceof UsernamePasswordAuthenticationToken) {
							UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) autentication;
							IMUserInfo userAccount = (IMUserInfo) token.getDetails();
							return userAccount.getAuthorizationToken().getJwt();
						} else {
							throw new RuntimeException("User is not yet authenticated.");
						}
					}
					
					@Override
					public String getApiToken() {
						return thingApiToken;
					}
				};
			}
			
			
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
				 				
				final IntegrationClientConfiguration integrationClientConfiguration =
				   IntegrationClientConfiguration.newBuilder()
				      .authenticationConfiguration(authenticationConfiguration)
				      .centralRegistryEndpointUrl(wsEndpointUrl)
				      .trustStoreConfiguration(trustStore)
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
		}
		'''
	}
}