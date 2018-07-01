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
package org.eclipse.vorto.repository;

import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.Observable;

import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.repository.Attachment;
import org.eclipse.vorto.core.api.repository.GeneratorResource;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.api.repository.RepositoryException;
import org.eclipse.vorto.core.api.repository.UploadResult;
import org.eclipse.vorto.repository.function.ModelViewToModelResource;
import org.eclipse.vorto.repository.function.StringToGeneratorList;
import org.eclipse.vorto.repository.function.StringToModelResourceResult;
import org.eclipse.vorto.repository.function.StringToSearchResult;
import org.eclipse.vorto.repository.model.ModelView;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class RestModelRepository extends Observable implements IModelRepository {

	private static final String FILE_DOWNLOAD_FORMAT = "file/%s/%s/%s";
	private static final String MODELID_RESOURCE_FORMAT = "%s/%s/%s";
	private static final String CHECKIN_FORMAT = "%s";

	private Function<ModelView, ModelResource> modelViewToModelResource = new ModelViewToModelResource();
	private Function<String, ModelView> contentConverters = new StringToModelResourceResult();
	private Function<String, ModelResource> stringToModelResource = Functions.compose(modelViewToModelResource,
			contentConverters);
	private Function<String, List<ModelView>> searchResultConverter = new StringToSearchResult();
	private Function<String, List<GeneratorResource>> searchGeneratorResultConverter = new StringToGeneratorList();

	private Function<String, byte[]> stringToByteArray = new Function<String, byte[]>() {
		public byte[] apply(String input) {
			return input.getBytes();
		}
	};

	private RestClient httpClient;

	public RestModelRepository(ConnectionInfo connectionUrlSupplier) {
		Objects.requireNonNull(connectionUrlSupplier);
		this.httpClient = new RestClient(connectionUrlSupplier);
	}

	@Override
	public List<ModelResource> search(String expression) {
		try {
			String searchExpr = null;
			if (Strings.isNullOrEmpty(expression)) {
				searchExpr = "-Mapping";
			} else {
				searchExpr = expression + " -Mapping";
			}
			searchExpr = searchExpr.replaceAll(" ", "%20");
			List<ModelView> result = httpClient.executeGet(("model/query=" + searchExpr), searchResultConverter);

			// Convert the searchResult in result to return type
			return Lists.transform(result, modelViewToModelResource);
		} catch (RepositoryException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Error querying remote repository with queryString = '" + expression + "'", e);
		}
	}

	@Override
	public byte[] downloadContent(ModelId modelId) {
		Objects.requireNonNull(modelId, "modelId should not be null");
		Objects.requireNonNull(modelId.getModelType(), "modelType should not be null");
		Objects.requireNonNull(modelId.getName(), "name should not be null");
		Objects.requireNonNull(modelId.getNamespace(), "namespace should not be null");
		Objects.requireNonNull(modelId.getVersion(), "version should not be null");

		String url = getUrlForModelDownload(modelId);
		try {
			return httpClient.executeGet(url, stringToByteArray);
		} catch (RepositoryException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Error downloading modelContent for resource", e);
		}
	}

	@Override
	public ModelResource getModel(ModelId modelId) {
		Objects.requireNonNull(modelId, "modelId should not be null");
		Objects.requireNonNull(modelId.getName(), "name should not be null");
		Objects.requireNonNull(modelId.getNamespace(), "namespace should not be null");
		Objects.requireNonNull(modelId.getVersion(), "version should not be null");

		String url = getUrlForModel(modelId);
		try {
			return httpClient.executeGet(url, stringToModelResource);
		} catch (RepositoryException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Error getting model info for resource", e);
		}
	}

	private String getUrlForModelDownload(ModelId modelId) {
		return "model/"
				+ String.format(FILE_DOWNLOAD_FORMAT, modelId.getNamespace(), modelId.getName(), modelId.getVersion());
	}

	private String getUrlForModel(ModelId modelId) {
		return "model/" + String.format(MODELID_RESOURCE_FORMAT, modelId.getNamespace(), modelId.getName(),
				modelId.getVersion());
	}

	@Override
	public UploadResult upload(String name, byte[] model) {
		throw new UnsupportedOperationException("Not implemented upload(..).");
	}

	@Override
	public void commit(String handleId) {
		throw new UnsupportedOperationException("Not implemented commit(..)");
	}

	@Override
	public List<GeneratorResource> listGenerators() {
		try {
			List<GeneratorResource> result = httpClient.executeGet("generation-router/platform",
					searchGeneratorResultConverter);
			return result;
		} catch (RepositoryException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Error querying remote repository with listGenerators request.", e);
		}
	}

	@Override
	public Attachment generateCode(ModelId model, String serviceKey) {
		try {
			String url = "generation-router/" + model.getNamespace() + "/" + model.getName() + "/" + model.getVersion()
					+ "/" + URLEncoder.encode(serviceKey, "utf-8");
			Attachment result = httpClient.executeGetAttachment(url);
			return result;
		} catch (RepositoryException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Error querying remote repository with generateCode request.", e);
		}
	}

}
