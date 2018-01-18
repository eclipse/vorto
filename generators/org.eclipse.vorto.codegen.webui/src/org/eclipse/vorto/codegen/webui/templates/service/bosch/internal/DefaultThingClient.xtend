package org.eclipse.vorto.codegen.webui.templates.service.bosch.internal

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class DefaultThingClient implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''DefaultThingClient.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch/internal'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch.internal;
		
		import java.util.concurrent.CompletableFuture;
		
		import org.apache.http.client.config.RequestConfig;
		import org.apache.http.client.methods.HttpGet;
		
		import com.example.iot.«element.name.toLowerCase».service.bosch.ThingClient;
		import com.example.iot.«element.name.toLowerCase».service.bosch.internal.model.ThingImpl;
		import com.example.iot.«element.name.toLowerCase».service.bosch.internal.model.ThingSearchResultImpl;
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.Thing;
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.ThingSearchResult;
		import com.google.gson.reflect.TypeToken;
		
		public class DefaultThingClient implements ThingClient {
		
			private String thingRestUrl;
		
			private RequestConfig requestConfig;
			
			private ThingsInvocationTemplate restTemplate;
			
			public DefaultThingClient(String thingRestUrl, ThingsInvocationTemplate template, RequestConfig config) {
				this.thingRestUrl = thingRestUrl;
				this.requestConfig = config;
				this.restTemplate = template;
			}
		
			@Override
			public CompletableFuture<ThingSearchResult> searchThings(String query) {
				HttpGet getAllThings = new HttpGet(String.format("%s/api/2/search/things?%s", this.thingRestUrl, query));
				
				if (requestConfig != null) {
					getAllThings.setConfig(requestConfig);
				}
		
				return restTemplate.execute(getAllThings,new TypeToken<ThingSearchResultImpl>(){}.getType());
				
			}
		
			@Override
			public CompletableFuture<Thing> getThing(String thingId) {
				HttpGet getThing = new HttpGet(String.format("%s/api/2/things/%s", this.thingRestUrl, thingId));
		
				if (requestConfig != null) {
					getThing.setConfig(requestConfig);
				}
				CompletableFuture<Thing> result = restTemplate.execute(getThing,new TypeToken<ThingImpl>(){}.getType());
				
				return result;
			}
		
			
		}
		
		'''
	}
	
}