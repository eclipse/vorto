package org.eclipse.vorto.codegen.webui.templates.service.bosch

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingClientBuilderTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''ThingClientBuilder.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch;
				
		import org.apache.http.HttpHost;
		import org.apache.http.client.config.RequestConfig;
		import org.springframework.security.oauth2.client.OAuth2ClientContext;
		
		import com.example.iot.«element.name.toLowerCase».service.bosch.internal.DefaultThingClient;
		import com.example.iot.«element.name.toLowerCase».service.bosch.internal.ThingsInvocationTemplate;
		
		public class ThingClientBuilder {
		
			private String endpointUrl;
				
			private RequestConfig config;
			
			private String apiToken;
			
			private OAuth2ClientContext oauth2context;
			
			public ThingClientBuilder() {
				this.endpointUrl = "https://things.apps.bosch-iot-cloud.com";
			}
			
			public ThingClientBuilder withEndpointUrl(final String endpointUrl) {
				this.endpointUrl = endpointUrl;
				return this;
			}
			
			public ThingClientBuilder withApiToken(final String apiToken) {
				this.apiToken = apiToken;
				return this;
			}
			
			public ThingClientBuilder withProxy(final String proxyHost, int proxyPort) {
				this.config = RequestConfig.custom().setProxy(new HttpHost(proxyHost, proxyPort)).build();
				return this;
			}
			
			public ThingClient build() {	
				return new DefaultThingClient(this.endpointUrl,new ThingsInvocationTemplate(apiToken, oauth2context), config);
			}
			
			public ThingClientBuilder withOAuth2ClientContext(OAuth2ClientContext oauth2context) {
				this.oauth2context = oauth2context;
				return this;
			}
		}
		
		'''
	}
	
}