package org.eclipse.vorto.codegen.bosch.things.javaclient.templates

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingIntegrationUtilTemplate implements ITemplate<InformationModel> {
    
    override getContent(InformationModel element, InvocationContext context) {
        var properties = context.configurationProperties
        var endpoint = properties.get("things_endpoint") ?: 'wss://events.apps.bosch-iot-cloud.com'
        var solutionid = properties.get("solutionId") ?: '[Your Solution Id here]'
        var aliaspassword = properties.get("alias_password") ?: '[Your alias password here]' // Please change default if necessary
        var keystorepassword = properties.get("keystore_password") ?: '[Your keystore password here]' // Please change default if necessary
        
'''
package com.example.things;

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
    // WS-URL of Bosch IoT Things service in cloud
    private static final String BOSCH_IOT_THINGS_WS_ENDPOINT_URL = "«endpoint»";

    // Insert your Solution ID here
    private static final String SOLUTION_ID = "«solutionid»";
    private static final String CLIENT_ID = SOLUTION_ID + ":iotconsole_local";

    // Insert your keystore passwords here
    private static final URL KEYSTORE_LOCATION = ThingClientFactory.class.getResource("/keystory.jks [Your Keystore file here]");
    private static final String KEYSTORE_PASSWORD = "«keystorepassword»"; 
    private static final String ALIAS = "CR";   // Please change if necessary
    private static final String ALIAS_PASSWORD = "«aliaspassword»"; 

    // Currently necessary for accepting bosch self signed certificates
    private static final URL TRUSTSTORE_LOCATION = ThingClientFactory.class.getResource("/truststore.jks [Your trust store file here]");
    private static final String TRUSTSTORE_PASSWORD = "jks";

    // optionally configure a proxy server
    public static final String PROXY_HOST = "localhost";
    public static final int PROXY_PORT = 8080;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ThingClientFactory.class);
    
    public static IntegrationClient getThingIntegrationClient() {
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
                .centralRegistryEndpointUrl(BOSCH_IOT_THINGS_WS_ENDPOINT_URL)
                .trustStoreConfiguration(trustStore)
                .proxyConfiguration(proxy)
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
