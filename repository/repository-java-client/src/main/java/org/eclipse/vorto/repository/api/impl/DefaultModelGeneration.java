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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.eclipse.vorto.repository.api.IModelGeneration;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.generation.GeneratedOutput;
import org.eclipse.vorto.repository.api.generation.GeneratorInfo;
import org.eclipse.vorto.repository.client.RepositoryClientException;

import com.google.gson.reflect.TypeToken;

public class DefaultModelGeneration extends ImplementationBase implements IModelGeneration {
	
	private static final String REST_BASE = "api/v1/generators";
	
	public DefaultModelGeneration(HttpClient httpClient, RequestContext context) {
		super(httpClient, context);
	}

	@Override
	public CompletableFuture<Set<String>> getAvailableGeneratorKeys() {
		return getAllGenerators(generators -> {
			return generators.stream().map(generator -> generator.getKey()).collect(Collectors.toSet());
		});
	}
	
	public CompletableFuture<List<GeneratorInfo>> getAvailableGenerators() {
		return getAllGenerators(Function.identity());
	}
	
	@Override
	public CompletableFuture<GeneratorInfo> getInfo(String generatorKey) {
		Objects.requireNonNull(generatorKey);
		
		return getAllGenerators(generators -> 
			generators.stream().filter(generator -> generator.getKey().equals(generatorKey)).findFirst().orElse(null)
		);
	}
	
	private <K> CompletableFuture<K> getAllGenerators(Function<List<GeneratorInfo>, K> converter) {
		Objects.requireNonNull(converter);
		
		String getAllGeneratorsUrl = String.format("%s/%s", getRequestContext().getBaseUrl(),String.format(REST_BASE));
		
		return requestAndTransform(getAllGeneratorsUrl, 
				converter.compose(transformToType(new TypeToken<ArrayList<GeneratorInfo>>() {}.getType())), 
				() -> converter.apply(Collections.emptyList()));
	}

	@Override
	public CompletableFuture<GeneratedOutput> generate(ModelId modelId, String generatorKey,
			Map<String, String> invocationParams) {
		Objects.requireNonNull(modelId);
		Objects.requireNonNull(generatorKey);
		
		String generateUrl = getGeneratorUrl(getRequestContext().getBaseUrl(), modelId, generatorKey, invocationParams);
		
		return requestAndTransform(generateUrl, this::getGeneratedOutput);
	}
	
	private GeneratedOutput getGeneratedOutput(HttpResponse response) {
		try {
			String contentDisposition = response.getFirstHeader("Content-Disposition").getValue();
			String filename = contentDisposition.substring(contentDisposition.indexOf("filename = ") + "filename = ".length());
			long length = response.getEntity().getContentLength();
			byte[] content = IOUtils.toByteArray(response.getEntity().getContent());
			return new GeneratedOutput(content, filename, length);
		} catch (IOException e) {
			throw new RepositoryClientException("Error in converting response to GeneratedOutput", e);
		}
	}

	private String getGeneratorUrl(String baseUrl, ModelId modelId, String generatorKey,
			Map<String, String> invocationParams) {
		try {
			StringBuilder url = new StringBuilder();
			
			url.append(baseUrl)
			 	.append("/"+String.format(REST_BASE)+"/")
			 	.append(generatorKey)
			 	.append("/models/")
			 	.append(modelId.getPrettyFormat())
			 	.append(URLEncoder.encode(generatorKey, "utf-8"));
			
			if (invocationParams != null && !invocationParams.isEmpty()) {
				StringJoiner joiner = new StringJoiner("&");
				invocationParams.forEach((key, value) -> {
					joiner.add(key + "=" + value);
				});
				url.append("?").append(joiner.toString());
			}
			
			return url.toString();
		} catch (UnsupportedEncodingException e) {
			throw new RepositoryClientException("Error in generating URL for the generator", e);
		}
	}
}
