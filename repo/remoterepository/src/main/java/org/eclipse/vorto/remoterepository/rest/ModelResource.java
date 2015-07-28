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
package org.eclipse.vorto.remoterepository.rest;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelFactory;
import org.eclipse.vorto.remoterepository.model.ModelId;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.eclipse.vorto.remoterepository.rest.model.SearchResult;
import org.eclipse.vorto.remoterepository.rest.utils.RestCallback;
import org.eclipse.vorto.remoterepository.rest.utils.RestTemplate;
import org.eclipse.vorto.remoterepository.service.IModelRepoService;
import org.eclipse.vorto.remoterepository.service.converter.IModelConverterService;
import org.eclipse.vorto.remoterepository.service.search.IModelQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/**
 * Main REST controller for uploading/downloading/searching models
 * 
 *
 */
@Path("/model")
@Component
public class ModelResource {

	private static final String SUFFIX_DATATYPE = ".datatype";
	private static final String SUFFIX_INFOMODEL = ".infomodel";
	private static final String SUFFIX_FBMODEL = ".fbmodel";

	private Log log = LogFactory.getLog(ModelResource.class);

	@Autowired
	private IModelRepoService modelRepoService;

	@Autowired
	private IModelConverterService modelConverterService;

	private static final RestTemplate restTemplate = new RestTemplate();

	/**
	 * Handles REST API of the form
	 * {@code /services/<model>/<namespace>/<name>/<version> }
	 * 
	 * @param model
	 * @param namespace
	 * @param name
	 * @param version
	 * @param response
	 * @throws NoSuchRequestHandlingMethodException
	 */
	@Path("/{model}/{namespace}/{name}/{version:.+}")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadModelById(final @PathParam("model") String model,
			final @PathParam("namespace") String namespace,
			final @PathParam("name") String name,
			final @PathParam("version") String version,
			final @Context HttpServletResponse response) {

		Objects.requireNonNull(model, "model must not be null");
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		return restTemplate.execute(new RestCallback() {

			@Override
			public Response execute() {
				ModelId modelId = ModelFactory.newModelId(getModelType(model),
						namespace, version, name);

				log.info("Request = " + modelId.toString());

				ModelContent modelContent = modelRepoService
						.getModelContent(modelId);
				if (modelContent.getContent() != null) {
					return Response
							.ok(modelContent.getContent(),
									MediaType.APPLICATION_OCTET_STREAM)
							.header("content-disposition",
									"attachment; filename = "
											+ suggestedFileName(modelId))
							.build();
				} else {
					log.info("Model with id " + modelId.toString()
							+ " not found.");
					return Response.status(Response.Status.NOT_FOUND).build();
				}
			}

		});

	}

	private String suggestedFileName(ModelId modelId) {
		if (modelId.getModelType() == ModelType.FUNCTIONBLOCK) {
			return modelId.getName() + SUFFIX_FBMODEL;
		} else if (modelId.getModelType() == ModelType.INFORMATIONMODEL) {
			return modelId.getName() + SUFFIX_INFOMODEL;
		}

		return modelId.getName() + SUFFIX_DATATYPE;
	}

	/**
	 * Handles REST queries of the form :
	 * http://localhost:8080/services/query={query expression}
	 * 
	 * Please refer to {@link IModelQuery} for documentation on {query
	 * expression}
	 * 
	 * @param queryString
	 *            The query expression
	 * @return
	 */
	@GET
	@Path("/query={queryString:.*}")
	@Produces(MediaType.APPLICATION_JSON)
	public SearchResult<ModelView> searchByExpression(
			@PathParam("queryString") String queryString) {
		log.info("Query : " + queryString);

		IModelQuery query = modelRepoService
				.newQueryFromExpression(queryString);

		return SearchResult.newResult(query.list());
	}

	/**
	 * Saves {@code file} to Repository, using the {@code model},
	 * {@code namespace}, {@code name}, {@code version} coordinate
	 * 
	 * @param model
	 *            The model type
	 * @param namespace
	 *            The namespace of the model
	 * @param name
	 *            The name of the model
	 * @param version
	 *            The version of the model
	 * @param multiPartFile
	 *            The file to be saved
	 * @param response
	 * @return
	 */
	@POST
	@Path("/{model}/{namespace}/{name}/{version:.+}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadModels(final @PathParam("model") String model,
			final @PathParam("namespace") String namespace,
			final @PathParam("name") String name,
			final @PathParam("version") String version,
			final @Context HttpServletRequest request,
			HttpServletResponse response) {

		Objects.requireNonNull(model, "model must not be null");
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		return restTemplate.execute(new RestCallback() {

			@Override
			public Response execute() throws Exception {
				if (ServletFileUpload.isMultipartContent(request)) {
					ModelId modelId = ModelFactory.newModelId(
							getModelType(model), namespace, version, name);
					ModelContent modelCtn = new ModelContent(modelId,
							getModelType(model),
							extractFileFromUploadContent(request));
					modelRepoService.saveModel(modelCtn);
					return Response.ok().build();

				} else {
					return Response.noContent().build();
				}
			}
		});
	}

	/**
	 * Saves the uploaded model to repository
	 * 
	 * @param fileInputStream
	 * @return
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadModel(
			final @FormDataParam("file") InputStream fileInputStream) {

		return restTemplate.execute(new RestCallback() {
			@Override
			public Response execute() throws Exception {
				if (fileInputStream != null) {
					ModelContent modelContent = modelConverterService
							.convertToModelContent(IOUtils
									.toByteArray(fileInputStream));
					log.info("Uploading model ["
							+ modelContent.getModelId().toString() + "]");
					modelRepoService.saveModel(modelContent);
					return Response.ok().build();
				} else {
					return Response.noContent().build();
				}
			}
		});
	}

	private byte[] extractFileFromUploadContent(HttpServletRequest request)
			throws Exception {
		final FileItemFactory factory = new DiskFileItemFactory();
		final ServletFileUpload fileUpload = new ServletFileUpload(factory);
		@SuppressWarnings("unchecked")
		final List<FileItem> items = fileUpload.parseRequest(request);
		final FileItem item = (FileItem) items.iterator().next();
		return item.get();

	}

	private ModelType getModelType(String model) {
		if (model.equalsIgnoreCase(ModelType.FUNCTIONBLOCK.name())) {
			return ModelType.FUNCTIONBLOCK;
		} else if (model.equalsIgnoreCase(ModelType.INFORMATIONMODEL.name())) {
			return ModelType.INFORMATIONMODEL;
		} else if (model.equalsIgnoreCase(ModelType.DATATYPE.name())) {
			return ModelType.DATATYPE;
		}
		return null;
	}

}
