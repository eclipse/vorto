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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHeader;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.repository.IModelQuery;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.CheckInModelException;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.model.ModelId;
import org.eclipse.vorto.core.model.ModelType;
import org.eclipse.vorto.repository.function.ModelToXmi;
import org.eclipse.vorto.repository.function.ModelViewToModelResource;
import org.eclipse.vorto.repository.function.StringToModelResourceResult;
import org.eclipse.vorto.repository.function.StringToUploadResult;
import org.eclipse.vorto.repository.model.ModelView;
import org.eclipse.vorto.repository.model.UploadResult;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.serializer.ISerializer;

import com.google.common.base.Function;

public class RestModelRepository extends Observable implements IModelRepository {

	private static final String FILE_PARAMETER_NAME = "file";

	private static final String FILE_DOWNLOAD_FORMAT = "%s/rest/model/file/%s/%s/%s";
	private static final String MODELID_RESOURCE_FORMAT = "%s/rest/model/%s/%s/%s";
	private static final String MODEL_RESOURCE_FORMAT = "%s/rest/model";
	private static final String CHECKIN_FORMAT = "%s/rest/model/%s";

	private Function<Model, byte[]> modelToXMIConverter = new ModelToXmi();
	private Function<String, UploadResult> uploadResponseConverter = new StringToUploadResult();
	private Function<ModelView, ModelResource> modelViewToModelResource = new ModelViewToModelResource();
	private Function<String, ModelView> contentConverters = new StringToModelResourceResult();

	private ConnectionInfo connectionUrlSupplier;

	public RestModelRepository(ConnectionInfo connectionUrlSupplier) {
		this.connectionUrlSupplier = Objects.requireNonNull(connectionUrlSupplier);
	}

	public IModelQuery newQuery() {
		return new RestModelQuery(connectionUrlSupplier, modelViewToModelResource);
	}

	@Override
	public List<ModelResource> search(String expression) {
		return new ArrayList<ModelResource>(
				new RestModelQuery(connectionUrlSupplier, modelViewToModelResource).newQuery(expression).list());
	}

	/**
	 * First downloads the model content XMI and then converts the XMI
	 * to the DSL representation
	 */
	@Override
	public byte[] downloadContent(ModelId modelId) {
		Objects.requireNonNull(modelId, "modelId should not be null");

		String url = getUrlForModelDownload(modelId);
		try {
			byte[] xmiContent = Request.Get(url).execute().returnContent().asBytes();

			return convertToDslContent(modelId, xmiContent);
		} catch (Exception e) {
			throw new RuntimeException("Error downloading modelContent for resource", e);
		}
	}

	private byte[] convertToDslContent(ModelId modelId, byte[] xmiContent) {
		XMIResource resource = new XMIResourceImpl(URI.createURI("dummyResource.xmi"));
		try {
			resource.load(new ByteArrayInputStream(xmiContent), null);
			ISerializer serializer = getSerializerForModel(modelId);
			String dsl = serializer.serialize(resource.getContents().get(0));
			return dsl.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private ISerializer getSerializerForModel(ModelId modelId) {
		URI modelUri = null;
		if (modelId.getModelType() == ModelType.Datatype) {
			modelUri = URI.createURI("model.type");
		} else if (modelId.getModelType() == ModelType.Functionblock) {
			modelUri = URI.createURI("model.fbmodel");
		} else {
			modelUri = URI.createURI("model.infomodel");
		}
		IResourceServiceProvider rsp = IResourceServiceProvider.Registry.INSTANCE
				.getResourceServiceProvider(modelUri);
		return rsp.get(ISerializer.class);
	}

	@Override
	public ModelResource getModel(ModelId modelId) {
		Objects.requireNonNull(modelId, "modelId should not be null");

		String url = getUrlForModel(modelId);
		try {
			ModelView modelView = contentConverters.apply(Request.Get(url).execute().returnContent().asString());
			return modelViewToModelResource.apply(modelView);
		} catch (Exception e) {
			throw new RuntimeException("Error getting model info for resource", e);
		}
	}

	private String getUrlForModelDownload(ModelId modelId) {
		Objects.requireNonNull(modelId.getModelType(), "modelType should not be null");
		Objects.requireNonNull(modelId.getName(), "name should not be null");
		Objects.requireNonNull(modelId.getNamespace(), "namespace should not be null");
		Objects.requireNonNull(modelId.getVersion(), "version should not be null");

		return String.format(FILE_DOWNLOAD_FORMAT, connectionUrlSupplier.getUrl(), modelId.getNamespace(),
				modelId.getName().toLowerCase(), modelId.getVersion());
	}

	private String getUrlForModel(ModelId modelId) {
		Objects.requireNonNull(modelId.getModelType(), "modelType should not be null");
		Objects.requireNonNull(modelId.getName(), "name should not be null");
		Objects.requireNonNull(modelId.getNamespace(), "namespace should not be null");
		Objects.requireNonNull(modelId.getVersion(), "version should not be null");

		return String.format(MODELID_RESOURCE_FORMAT, connectionUrlSupplier.getUrl(), modelId.getNamespace(),
				modelId.getName().toLowerCase(), modelId.getVersion());
	}

	private String getUrlForUpload() {
		return String.format(MODEL_RESOURCE_FORMAT, connectionUrlSupplier.getUrl());
	}

	private String getUrlForCheckin(String handleId) {
		return String.format(CHECKIN_FORMAT, connectionUrlSupplier.getUrl(), handleId);
	}

	/**
	 * 1. Convert the model to XMI representation
	 * 2. Uploads the XMI to the repository to verify correct file. A handle is returned.
	 * 3. Checks in the uploaded model by its handle.
	 */
	@Override
	public void saveModel(Model model) throws CheckInModelException {
		Objects.requireNonNull(model);

		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addBinaryBody(FILE_PARAMETER_NAME, modelToXMIConverter.apply(model),
					ContentType.APPLICATION_OCTET_STREAM, "");
			HttpEntity fileToUpload = builder.build();

			Content responseContent = Request.Post(getUrlForUpload()).addHeader(createSecurityHeader())
					.body(fileToUpload).execute().returnContent();

			UploadResult uploadResult = uploadResponseConverter.apply(responseContent.asString());

			if (uploadResult.statusOk()) {
				Request.Put(getUrlForCheckin(uploadResult.getHandleId())).addHeader(createSecurityHeader());
			} else {
				throw new CheckInModelException(uploadResult.getErrorMessage());
			}

			setChanged();
			notifyObservers(uploadResult);
		} catch (Exception e) {
			throw new CheckInModelException("Error in uploading file to remote repository", e);
		}
	}

	private Header createSecurityHeader() {
		return new BasicHeader("Authorization", "Basic " + createAuth());
	}

	private String createAuth() {
		return new String(Base64.encodeBase64(
				(connectionUrlSupplier.getUserName() + ":" + connectionUrlSupplier.getPassword()).getBytes()));
	}
}
