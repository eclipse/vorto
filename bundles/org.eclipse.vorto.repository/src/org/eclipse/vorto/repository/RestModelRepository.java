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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
<<<<<<< HEAD
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
=======
import org.apache.http.message.BasicHeader;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
>>>>>>> Fixed the problem where an xmi can't be converted to a model because of missing dependencies.
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.repository.CheckInModelException;
import org.eclipse.vorto.core.api.repository.IModelQuery;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.model.ModelId;
import org.eclipse.vorto.core.model.ModelType;
import org.eclipse.vorto.repository.function.ModelToDsl;
import org.eclipse.vorto.repository.function.ModelToXmi;
import org.eclipse.vorto.repository.function.ModelViewToModelResource;
import org.eclipse.vorto.repository.function.StringToModelResourceResult;
import org.eclipse.vorto.repository.function.StringToUploadResult;
import org.eclipse.vorto.repository.function.XmiToModel;
import org.eclipse.vorto.repository.model.ModelView;
import org.eclipse.vorto.repository.model.UploadResult;

import com.google.common.base.Function;
import com.google.common.base.Functions;

public class RestModelRepository extends Observable implements IModelRepository {

	private static final String FILE_PARAMETER_NAME = "file";
	private static final String FILE_DOWNLOAD_FORMAT = "/file/%s/%s/%s";
	private static final String MODELID_RESOURCE_FORMAT = "/%s/%s/%s";
	private static final String CHECKIN_FORMAT = "%s";

	private Function<Model, byte[]> modelToXMIConverter = new ModelToXmi();
	private Function<String, UploadResult> uploadResponseConverter = new StringToUploadResult();
	private Function<ModelView, ModelResource> modelViewToModelResource = new ModelViewToModelResource();
	private Function<String, ModelView> contentConverters = new StringToModelResourceResult();
	
<<<<<<< HEAD
	private RestClient httpClient;
=======
	// A function, that given a modeltype, returns a function that converts from XMI to DSL
	private Function<ModelType, Function<byte[], byte[]>> xmiToDslConverter = Functions.forMap(initializeContentConverters());
	
	private ConnectionInfo connectionUrlSupplier;
>>>>>>> Fixed the problem where an xmi can't be converted to a model because of missing dependencies.

	public RestModelRepository(ConnectionInfo connectionUrlSupplier) {
		Objects.requireNonNull(connectionUrlSupplier);
		this.httpClient = new RestClient(connectionUrlSupplier);
	}

	public IModelQuery newQuery() {
		return new RestModelQuery(httpClient,null, modelViewToModelResource);
	}

	@Override
	public List<ModelResource> search(String expression) {
		return new ArrayList<ModelResource>(
				new RestModelQuery(httpClient,null, modelViewToModelResource).newQuery(expression).list());
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
			
			return xmiToDslConverter.apply(modelId.getModelType()).apply(xmiContent);
		} catch (Exception e) {
			throw new RuntimeException("Error downloading modelContent for resource", e);
		}
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

		return String.format(FILE_DOWNLOAD_FORMAT, modelId.getNamespace(),
				modelId.getName().toLowerCase(), modelId.getVersion());
	}

	private String getUrlForModel(ModelId modelId) {
		Objects.requireNonNull(modelId.getModelType(), "modelType should not be null");
		Objects.requireNonNull(modelId.getName(), "name should not be null");
		Objects.requireNonNull(modelId.getNamespace(), "namespace should not be null");
		Objects.requireNonNull(modelId.getVersion(), "version should not be null");

		return String.format(MODELID_RESOURCE_FORMAT, modelId.getNamespace(),
				modelId.getName().toLowerCase(), modelId.getVersion());
	}

	private String getUrlForCheckin(String handleId) {
		return String.format(CHECKIN_FORMAT,handleId);
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
			
			UploadResult uploadResult = httpClient.executePost("/", fileToUpload,uploadResponseConverter);

			if (uploadResult.statusOk()) {
				httpClient.executePut(getUrlForCheckin(uploadResult.getHandleId()));
			} else {
				throw new CheckInModelException(uploadResult.getErrorMessage());
			}

			setChanged();
			notifyObservers(uploadResult);
		} catch (Exception e) {
			throw new CheckInModelException("Error in uploading file to remote repository", e);
		}
	}

<<<<<<< HEAD
=======
	/**
	 * TODO: Currently the repo only supports form based authentication. That way a REST controller authenticate would need to be invoked 
	 * and session needs to be passed along.
	 * @return
	 */
	private Header createSecurityHeader() {
		return new BasicHeader("Authorization", "Basic " + createAuth());
	}

	private String createAuth() {
		return new String(Base64.encodeBase64(
				(connectionUrlSupplier.getUserName() + ":" + connectionUrlSupplier.getPassword()).getBytes()));
	}
	
	private Map<ModelType, Function<byte[], byte[]>> initializeContentConverters() {
		Map<ModelType, Function<byte[], byte[]>> converters = new HashMap<ModelType, Function<byte[], byte[]>>();
		
		Function<Model, byte[]> modelToDsl = new ModelToDsl();

		converters.put(ModelType.InformationModel,
				Functions.compose(modelToDsl, new XmiToModel<InformationModel>(InformationModel.class)));
		converters.put(ModelType.Functionblock,
				Functions.compose(modelToDsl, new XmiToModel<FunctionblockModel>(FunctionblockModel.class)));
		converters.put(ModelType.Datatype, 
				Functions.compose(modelToDsl, new XmiToModel<Type>(Type.class)));

		return converters;
	}
>>>>>>> Fixed the problem where an xmi can't be converted to a model because of missing dependencies.
}
