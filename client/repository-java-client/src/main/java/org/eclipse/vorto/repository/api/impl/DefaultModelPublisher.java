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
package org.eclipse.vorto.repository.api.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.eclipse.vorto.repository.api.IModelPublisher;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.upload.ModelPublishException;
import org.eclipse.vorto.repository.api.upload.UploadModelResponse;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;

import com.google.gson.reflect.TypeToken;

public class DefaultModelPublisher extends ImplementationBase implements IModelPublisher {

	private String username;
	private String password;
	
	public DefaultModelPublisher(HttpClient httpClient, RequestContext requestContext,String username, String password) {
		super(httpClient, requestContext);
		this.username = username;
		this.password = password;
	}

	@Override
	public ModelId publish(ModelType type, String content) throws ModelPublishException {

		String uploadModelsUrl = String.format("%s/rest/secure", getRequestContext().getBaseUrl());
		HttpPost query = new HttpPost(uploadModelsUrl);
		HttpEntity entity = MultipartEntityBuilder.create()
				.addPart("fileName", new StringBody("vortomodel" + type.getExtension(), ContentType.DEFAULT_TEXT))
				.addPart("fileDescription", new StringBody("", ContentType.DEFAULT_TEXT))
				.addPart("file", new ByteArrayBody(content.getBytes(), ContentType.APPLICATION_OCTET_STREAM,
						"vortomodel" + type.getExtension()))
				.build();

		query.setEntity(entity);

		try {
			CompletableFuture<UploadModelResponse> response = execute(query, new TypeToken<UploadModelResponse>() {
			}.getType());
			List<UploadModelResult> result = response.get().getObj();
			if (response.get().getIsSuccess()) {
				String checkinModelUrl = String.format("%s/rest/secure/%s", getRequestContext().getBaseUrl(),
						result.get(0).getHandleId());
				HttpPut checkInModel = new HttpPut(checkinModelUrl);
				CompletableFuture<ModelId> checkedInResult = execute(checkInModel,new TypeToken<ModelId>() {
				}.getType());
				return (ModelId) checkedInResult.get();

			} else {
				throw new ModelPublishException(result.get(0));
			}
		} catch (Throwable ex) {
			if (!(ex instanceof ModelPublishException)) {
				throw new RuntimeException(ex);
			} else {
				throw ((ModelPublishException)ex);
			}
			
		}
	}

	public <Result> CompletableFuture<Result> execute(final HttpUriRequest request, Type type) {

		return CompletableFuture.supplyAsync(() -> {
			try {
				request.addHeader(HttpHeaders.AUTHORIZATION,
						"Basic " + Base64.getEncoder().encodeToString((this.username+":"+this.password).getBytes()));
				return httpClient.execute(request, response -> {

					int statusCode = response.getStatusLine().getStatusCode();

					if (statusCode >= 200 && statusCode <= 299) {
						if (type.equals(Void.class)) {
							return null;
						}
						String json = EntityUtils.toString(response.getEntity(), "UTF-8");
						return gson.fromJson(json, type);
					} else {
						throw new RuntimeException(
								"Error while executing async call, server respond with a status of:  "
										+ response.getStatusLine().getStatusCode());
					}
				});
			} catch (IOException e) {
				throw new RuntimeException("Error executing async call to remote REST endpoint", e);
			}
		});
	}

	public <Result> CompletableFuture<Result> execute(HttpUriRequest request) {
		return execute(request, new TypeToken<Result>() {
		}.getType());
	}

	@Override
	public void uploadModelImage(ModelId modelId, String imageBas64) throws ModelPublishException {
		String uploadImageUrl = String.format("%s/rest/model/image?namespace=%s&name=%s&version=%s", getRequestContext().getBaseUrl(),modelId.getNamespace(),modelId.getName(),modelId.getVersion());
		HttpPost uploadImage = new HttpPost(uploadImageUrl);
		HttpEntity entity = MultipartEntityBuilder.create()
				.addPart("fileName", new StringBody("vortomodel.png", ContentType.DEFAULT_TEXT))
				.addPart("fileDescription", new StringBody("", ContentType.DEFAULT_TEXT))
				.addPart("file", new ByteArrayBody(Base64.getDecoder().decode(imageBas64.getBytes()), ContentType.APPLICATION_OCTET_STREAM,
						"vortomodel.png"))
				.build();
		
		
		uploadImage.setEntity(entity);
		
		try {
			execute(uploadImage, new TypeToken<Void>() {}.getType());
		} catch (Throwable ex) {
			if (!(ex instanceof ModelPublishException)) {
				throw new RuntimeException(ex);
			} else {
				throw ((ModelPublishException)ex);
			}
			
		}
		
	}
}
