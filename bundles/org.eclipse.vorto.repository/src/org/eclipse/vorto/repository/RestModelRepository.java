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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.repository.IModelQuery;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelAlreadyExistException;
import org.eclipse.vorto.core.model.ModelId;
import org.eclipse.vorto.core.model.ModelType;
import org.eclipse.vorto.repository.function.ModelToXmi;
import org.eclipse.vorto.repository.function.StringToUploadResult;
import org.eclipse.vorto.repository.function.XmiToModel;
import org.eclipse.vorto.repository.model.UploadResult;

import com.google.common.base.Function;

public class RestModelRepository extends Observable implements IModelRepository {

	private static final String FILE_PARAMETER_NAME = "file";

	private static final String MODEL_ID_SERVICE_FORMAT = "%s/rest/model/%s/%s/%s/%s";

	private static final String UPLOAD_FILE_URL_FORMAT = "%s/rest/model/upload";

	private Function<Model, byte[]> modelToByteArrayConverter = new ModelToXmi();
	
	private Function<String, UploadResult> uploadResponseConverter = new StringToUploadResult();

	private ConnectionInfoSupplier connectionUrlSupplier;
	private Map<ModelType, Function<byte[], Model>> contentConverters = initializeContentConverters();

	public RestModelRepository(ConnectionInfoSupplier connectionUrlSupplier) {
		this.connectionUrlSupplier = Objects
				.requireNonNull(connectionUrlSupplier);
	}

	private Map<ModelType, Function<byte[], Model>> initializeContentConverters() {
		Map<ModelType, Function<byte[], Model>> converters = new HashMap<ModelType, Function<byte[], Model>>();

		converters.put(ModelType.INFORMATIONMODEL,
				new XmiToModel<InformationModel>(InformationModel.class));
		converters.put(ModelType.FUNCTIONBLOCK,
				new XmiToModel<FunctionblockModel>(FunctionblockModel.class));
		converters.put(ModelType.DATATYPE, 
				new XmiToModel<Type>(Type.class));

		return converters;
	}

	public IModelQuery newQuery() {
		return new RestModelQuery(connectionUrlSupplier);
	}

	@Override
	public Model getModel(ModelId modelId) {
		Objects.requireNonNull(modelId, "modelId should not be null");

		String url = getUrlForModelId(modelId);

		try {
			return contentConverters.get(modelId.getModelType()).apply(
					Request.Get(url).execute().returnContent().asBytes());
		} catch (Exception e) {
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
	public void saveModel(Model model) throws ModelAlreadyExistException {
		Objects.requireNonNull(model);

		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addBinaryBody(FILE_PARAMETER_NAME,
					modelToByteArrayConverter.apply(model),
					ContentType.APPLICATION_OCTET_STREAM, "");
			HttpEntity fileToUpload = builder.build();

			Content responseContent = Request.Post(getUrlForUpload())
					.body(fileToUpload).execute().returnContent();

			UploadResult uploadResult = uploadResponseConverter
					.apply(responseContent.asString());

			setChanged();
			notifyObservers(uploadResult);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error in uploading file to remote repository", e);
		}
	}
}
