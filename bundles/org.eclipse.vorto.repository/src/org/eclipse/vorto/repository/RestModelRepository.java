/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.repository;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.repository.IModelQuery;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelAlreadyExistException;
import org.eclipse.vorto.core.api.repository.ModelContent;
import org.eclipse.vorto.core.model.ModelId;
import org.eclipse.vorto.core.model.ModelType;
import org.eclipse.vorto.repository.function.ContentToModelContent;

import com.google.common.base.Function;
import com.google.common.io.ByteStreams;

public class RestModelRepository extends Observable implements IModelRepository {

	private static final String FILE_PARAMETER_NAME = "file";

	private static final String MODEL_ID_SERVICE_FORMAT = "%s/rest/model/%s/%s/%s/%s";

	private static final String UPLOAD_FILE_URL_FORMAT = "%s/rest/model/upload";

	private static final String UPLOAD_MSG = "Uploaded model with id: %s";

	private static final String RESPONSE_MSG = "Response Status Code : %s %s";

	private ConnectionInfoSupplier connectionUrlSupplier;
	private Map<ModelType, Function<Content, ModelContent>> contentConverters = initializeContentConverters();

	public RestModelRepository(ConnectionInfoSupplier connectionUrlSupplier) {
		this.connectionUrlSupplier = Objects
				.requireNonNull(connectionUrlSupplier);
	}

	private Map<ModelType, Function<Content, ModelContent>> initializeContentConverters() {
		Map<ModelType, Function<Content, ModelContent>> converters = new HashMap<ModelType, Function<Content, ModelContent>>();

		converters.put(ModelType.INFORMATIONMODEL,
				new ContentToModelContent<InformationModel>(
						InformationModel.class, ModelType.INFORMATIONMODEL));
		converters.put(ModelType.FUNCTIONBLOCK,
				new ContentToModelContent<FunctionblockModel>(
						FunctionblockModel.class, ModelType.FUNCTIONBLOCK));
		converters.put(ModelType.DATATYPE, new ContentToModelContent<Type>(
				Type.class, ModelType.DATATYPE));

		return converters;
	}

	public IModelQuery newQuery() {
		return new RestModelQuery(connectionUrlSupplier);
	}

	@Override
	public ModelContent getModelContentForResource(ModelId modelId) {
		Objects.requireNonNull(modelId, "modelId should not be null");

		String url = getUrlForModelId(modelId);

		try {
			return contentConverters.get(modelId.getModelType()).apply(
					Request.Get(url).execute().returnContent());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Error getting modelContent for resource", e);
		}
	}

	private String getUrlForModelId(ModelId modelId) {
		Objects.requireNonNull(modelId.getModelType(),
				"modelType should not be null");
		Objects.requireNonNull(modelId.getName(), "name should not be null");
		Objects.requireNonNull(modelId.getNamespace(),
				"namespace should not be null");
		Objects.requireNonNull(modelId.getVersion(),
				"version should not be null");

		return String.format(MODEL_ID_SERVICE_FORMAT,
				connectionUrlSupplier.connectionUrl(), modelId.getModelType()
						.name().toLowerCase(), modelId.getNamespace(),
				modelId.getName(), modelId.getVersion());
	}

	private String getUrlForUpload() {
		return String.format(UPLOAD_FILE_URL_FORMAT,
				connectionUrlSupplier.connectionUrl());
	}

	@Override
	public void checkIn(ModelId modelId, InputStream file)
			throws ModelAlreadyExistException {
		Objects.requireNonNull(modelId);
		Objects.requireNonNull(file);

		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addBinaryBody(FILE_PARAMETER_NAME,
					ByteStreams.toByteArray(file),
					ContentType.APPLICATION_OCTET_STREAM, "");
			HttpEntity fileToUpload = builder.build();

			HttpResponse response = Request.Post(getUrlForUpload())
					.body(fileToUpload).execute().returnResponse();

			notifyObservers(String.format(UPLOAD_MSG, modelId));
			notifyObservers(String.format(RESPONSE_MSG, response
					.getStatusLine().getStatusCode(), response.getEntity()
					.getContentLength()));
		} catch (Exception e) {
			throw new RuntimeException(
					"Error in uploading file to remote repository", e);
		}
	}
}
