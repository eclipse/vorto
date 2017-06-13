package org.eclipse.vorto.codegen.webui.templates.service.bosch.internal

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class AsyncInvocationTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''AsyncInvocationTemplate.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch/internal'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch.internal;
		
		import java.io.IOException;
		import java.lang.reflect.Type;
		import java.util.concurrent.CompletableFuture;
		
		import org.apache.http.HttpHeaders;
		import org.apache.http.client.HttpClient;
		import org.apache.http.client.methods.HttpUriRequest;
		import org.apache.http.impl.client.HttpClients;
		import org.apache.http.util.EntityUtils;
		
		import com.google.gson.Gson;
		import com.google.gson.reflect.TypeToken;
		
		public class AsyncInvocationTemplate {
		
			private Gson gson;
			
			public AsyncInvocationTemplate(Gson gson) {
				this.gson = gson;
			}
			
			public <Result> CompletableFuture<Result> execute(final HttpUriRequest request, Type type) {
				preSend(request);
		
				return CompletableFuture.supplyAsync(() -> {
					try {
						final HttpClient httpClient = HttpClients.createDefault();
						return httpClient.execute(request, response -> {
							
							int statusCode = response.getStatusLine().getStatusCode();
							
							if (statusCode >= 200 && statusCode <= 299) {
								if (type.equals(Void.class)) {
									return null;
								}
								String json = EntityUtils.toString(response.getEntity(), "UTF-8");
								return gson.fromJson(json, type);
							} else if (statusCode == 400) {
								throw new IllegalArgumentException("Error executing async call, bad request.");
							} else if (statusCode == 401) {
								throw new RuntimeException("Error executing async call, not able to authenticate with the server");
							} else if (statusCode == 403) {
								throw new RuntimeException("Error executing async call, not able to autorize with the server");					
							} else if (statusCode == 404) {
								return null;
							} else {
								throw new RuntimeException("Error while executing async call, server respond with a status of:  " + response.getStatusLine().getStatusCode());
							}
						});
					} catch (IOException e) {
						throw new RuntimeException("Error executing async call to remote REST endpoint", e);
					}
				});
			}
			
			public <Result> CompletableFuture<Result> execute(HttpUriRequest request) {
				return execute(request,new TypeToken<Result>(){}.getType());
			}
		
			protected void preSend(HttpUriRequest request) {
				request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
				request.addHeader(HttpHeaders.ACCEPT, "application/json");
			}
		}
		
		'''
	}
	
}