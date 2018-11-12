/**
 * Copyright (c) 2015-2017 Bosch Software Innovations GmbH and others.
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.eclipse.vorto.repository.api.IModelRepository;
import org.eclipse.vorto.repository.api.ModelContent;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelQuery;
import org.eclipse.vorto.repository.api.attachment.Attachment;
import org.eclipse.vorto.repository.api.exception.ModelQueryException;
import org.eclipse.vorto.repository.client.RepositoryClientException;

import com.google.gson.reflect.TypeToken;

public class DefaultModelRepository extends ImplementationBase implements IModelRepository {
	
	private static final String REST_SEARCH_BASE = "api/v1/search/models";
	private static final String REST_MODEL_BASE = "api/v1/models";
	private static final String REST_ATTACHMENT_BASE = "api/v1/attachments";

	public DefaultModelRepository(HttpClient httpClient, RequestContext requestContext) {
		super(httpClient, requestContext);
	}

	@Override
	public CompletableFuture<Collection<ModelInfo>> search(ModelQuery query) {
		String expression = "*";
		if (query.getExpression() != null) {
			try {
				expression = URLEncoder.encode(query.getExpression().trim(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				throw new ModelQueryException("Error encoding the query", e);
			}
		}
		
		String url = String.format("%s/%s?expression=%s", getRequestContext().getBaseUrl(), String.format(REST_SEARCH_BASE), expression);
		return requestAndTransform(url, transformToType(new TypeToken<ArrayList<ModelInfo>>() {}.getType()));
	}

	@Override
	public CompletableFuture<ModelInfo> getById(ModelId modelId) {
		String url = String.format("%s/%s/%s", getRequestContext().getBaseUrl(),String.format(REST_MODEL_BASE), modelId.getPrettyFormat());
		return requestAndTransform(url, transformToClass(ModelInfo.class));
	}

	@Override
	public CompletableFuture<ModelContent> getContent(ModelId modelId) {
		String url = String.format("%s/%s/%s/content", getRequestContext().getBaseUrl(),String.format(REST_MODEL_BASE), modelId.getPrettyFormat());
		return requestAndTransform(url, transformToClass(ModelContent.class));
	}

	@Override
	public CompletableFuture<ModelContent> getContent(ModelId modelId,String targetPlatformKey) {
		String url = String.format("%s/%s/%s/content/%s", getRequestContext().getBaseUrl(),String.format(REST_MODEL_BASE), modelId.getPrettyFormat(),targetPlatformKey);
		return requestAndTransform(url, transformToClass(ModelContent.class));
	}

	@Override
	public CompletableFuture<ModelContent> getContent(ModelId modelId, ModelId mappingModelId) {
		String url = String.format("%s/%s/%s/content/mappings/%s", getRequestContext().getBaseUrl(),String.format(REST_MODEL_BASE), modelId.getPrettyFormat(),mappingModelId.getPrettyFormat());
		return requestAndTransform(url, transformToClass(ModelContent.class));
	}
	
	@Override
	public CompletableFuture<List<Attachment>> getAttachments(ModelId modelId) {
		String url = String.format("%s/%s/%s", getRequestContext().getBaseUrl(), String.format(REST_ATTACHMENT_BASE), modelId.getPrettyFormat());
		return requestAndTransform(url, transformToType(new TypeToken<ArrayList<Attachment>>() {}.getType()));
	}
	
	@Override
	public CompletableFuture<byte[]> getAttachment(ModelId modelId, String filename) {
		String url = String.format("%s/%s/%s/files/%s", getRequestContext().getBaseUrl(), String.format(REST_ATTACHMENT_BASE), modelId.getPrettyFormat(), filename);
		return requestAndTransform(url, (response) -> {
			try {
				return IOUtils.toByteArray(response.getEntity().getContent());
			} catch (UnsupportedOperationException | IOException e) {
				throw new RepositoryClientException("Error while getting attachment + '" + filename + "' with url '" + url + "'", e);
			}
		});
	}
}
