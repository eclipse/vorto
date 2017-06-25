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
package org.eclipse.vorto.codegen.kura.templates.cloud.bosch

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.kura.templates.Utils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * @author Alexander Edelmann
 */
class ThingClientFactoryTemplate implements IFileTemplate<InformationModel>{
	
	override getFileName(InformationModel context) {
		'''ThingClientFactory.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.javaPackageBasePath»/cloud/bosch'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package «Utils.javaPackage».cloud.bosch;
		
		import java.net.URL;
		
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;
		
		import com.bosch.cr.integration.IntegrationClient;
		import com.bosch.cr.integration.client.IntegrationClientImpl;
		import com.bosch.cr.integration.client.configuration.AuthenticationConfiguration;
		import com.bosch.cr.integration.client.configuration.IntegrationClientConfiguration;
		import com.bosch.cr.integration.client.configuration.ProxyConfiguration;
		import com.bosch.cr.integration.client.configuration.PublicKeyAuthenticationConfiguration;
		import com.bosch.cr.integration.client.configuration.TrustStoreConfiguration;
		
		public class ThingClientFactory {
		   
		    // Insert your keystore passwords here
		    private static final URL KEYSTORE_LOCATION = ThingClientFactory.class.getResource("/secret/CRClient.jks");
		    private static final String KEYSTORE_PASSWORD = "bosch123"; 
		    private static final String ALIAS = "CR";   // Please change if necessary
		    private static final String ALIAS_PASSWORD = "bosch123"; 
		
		    private static final URL TRUSTSTORE_LOCATION = ThingClientFactory.class.getResource("/secret/bosch-iot-cloud.jks");
		    private static final String TRUSTSTORE_PASSWORD = "jks";
		
		    // optionally configure a proxy server
		    public static final String PROXY_HOST = "localhost";
		    public static final int PROXY_PORT = 8080;
		    
		    private static final Logger LOGGER = LoggerFactory.getLogger(ThingClientFactory.class);
		    
		    public static IntegrationClient getThingIntegrationClient(String solutionId,String endpointUrl) {
		    	
		    	final String CLIENT_ID = solutionId + ":iotconsole_local";
		    	
		         // Build an authentication configuration
		        final AuthenticationConfiguration authenticationConfiguration = PublicKeyAuthenticationConfiguration
		                .newBuilder().clientId(CLIENT_ID)
		                .keyStoreLocation(KEYSTORE_LOCATION)
		                .keyStorePassword(KEYSTORE_PASSWORD).alias(ALIAS)
		                .aliasPassword(ALIAS_PASSWORD).build();
		
		        // Optionally configure a proxy server
		        final ProxyConfiguration proxy = ProxyConfiguration.newBuilder()
		              .proxyHost(PROXY_HOST)
		              .proxyPort(PROXY_PORT)
		              .build();
		         
		        //Configure a truststore that contains trusted certificates
		        final TrustStoreConfiguration trustStore = TrustStoreConfiguration
		                .newBuilder().location(TRUSTSTORE_LOCATION)
		                .password(TRUSTSTORE_PASSWORD).build();
		
		        /**
		         * Provide required configuration (authentication configuration and CR
		         * URI), optional proxy configuration can be added when needed
		         */
		        final IntegrationClientConfiguration integrationClientConfiguration = IntegrationClientConfiguration
		                .newBuilder()
		                .authenticationConfiguration(authenticationConfiguration)
		                .centralRegistryEndpointUrl(endpointUrl)
		                .trustStoreConfiguration(trustStore)
		//                .proxyConfiguration(proxy)
		                .build();
		
		        LOGGER.info("Creating CR Integration Client for ClientID: {}", CLIENT_ID);
		
		        // Create a new integration client object to start interacting service
		        return IntegrationClientImpl
		                .newInstance(integrationClientConfiguration);
		    }
		}
		
		'''
	}
	
}
