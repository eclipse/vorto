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
package org.eclipse.vorto.repository.web;

import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.service.IModelRepository.ContentType;
import org.eclipse.vorto.repository.web.util.RestCallback;
import org.eclipse.vorto.repository.web.util.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;

/**
 * 
 * @author Alexander Edelmann
 *
 */
@Path("/model")
@Component
public class ModelRepositoryController {

	@Autowired
	private IModelRepository modelRepository;
	
	private static final RestTemplate restTemplate = new RestTemplate();
	
	
	@GET
	@Path("/query={queryExpression:.*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchByExpression(
			@PathParam("queryExpression") final String queryExpression) {		
			
		return restTemplate.execute(new RestCallback() {

			@Override
			public Response execute() throws Exception {
				return Response.ok(modelRepository.search(queryExpression)).build();
			}
			
		});
	}
	
	/**
	 * Uploads a new model, stores it as a temporary file and validates its content
	 * 
	 * @param inputstream
	 * @return validation result and upload handle 
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadModel(final FormDataMultiPart form, @FormDataParam("file") final FormDataContentDisposition contentDisposition) {
	
		Objects.requireNonNull(form, "File must not be null");

		return restTemplate.execute(new RestCallback() {
			@Override
			public Response execute() throws Exception {
					
					return Response.ok(modelRepository.upload(form.getField("file").getValueAs(byte[].class),contentDisposition.getFileName())).build();
			}
		});
	}
	
	@PUT
	@Path("/{handleId:.+}")
	public Response checkin(@PathParam("handleId") final String handleId) {
	
		return restTemplate.execute(new RestCallback() {
			@Override
			public Response execute() throws Exception {
					modelRepository.checkin(handleId);
			        return Response.ok().build();
			}
		});
	}
	 	
	/**
	 * Downloads the actual file content for the given model Id
	 * 
	 * @param namespace
	 * @param name
	 * @param version
	 * @param response
	 */
	@Path("/file/{namespace}/{name}/{version:.+}")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadModelById(
			final @PathParam("namespace") String namespace,
			final @PathParam("name") String name,
			final @PathParam("version") String version,
			final @QueryParam("output") String output,
			final @Context HttpServletResponse response) {

		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		return restTemplate.execute(new RestCallback() {

			@Override
			public Response execute() {
				final ModelId modelId = new ModelId(name,namespace,version);
				final ModelResource modelResource = modelRepository.getById(modelId);
				
				final ContentType contentType = getContentType(output);
				byte[] modelContent = modelRepository.getModelContent(modelId,contentType);
				
				if (modelContent != null && modelContent.length > 0) {
					return Response
							.ok(modelContent,
									MediaType.APPLICATION_OCTET_STREAM)
							.header("content-disposition",
									"attachment; filename = "+getFileName(modelResource, contentType))
							.build();
				} else {
					return Response.status(Response.Status.NOT_FOUND).build();
				}
			}

		});

	}
	
	private String getFileName(ModelResource modelResource, ContentType contentType) {
		if (contentType == ContentType.XMI) {
			return modelResource.getId().getName()+".xmi";
		} else {
			return modelResource.getId().getName()+modelResource.getModelType().getExtension();
		}
	}
	
	private ContentType getContentType(String output) {
		if (output == null || output.isEmpty()) {
			return ContentType.DSL;
		} else {
			return ContentType.valueOf(output.toUpperCase());
		}
	}
	
	/**
	 * Gets the details about a model resource
	 * 
	 * @param namespace
	 * @param name
	 * @param version
	 * @param response
	 */
	@Path("/{namespace}/{name}/{version:.+}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getModelResource(
			final @PathParam("namespace") String namespace,
			final @PathParam("name") String name,
			final @PathParam("version") String version,
			final @Context HttpServletResponse response) {

		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		return restTemplate.execute(new RestCallback() {

			@Override
			public Response execute() {
				final ModelId modelId = new ModelId(name,namespace,version);
				return Response.ok(modelRepository.getById(modelId)).build();
			}

		});

	}
	
	@Path("/{namespace}/{name}/{version:.+}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteModelResource(
			final @PathParam("namespace") String namespace,
			final @PathParam("name") String name,
			final @PathParam("version") String version,
			final @Context HttpServletResponse response) {

		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		return restTemplate.execute(new RestCallback() {

			@Override
			public Response execute() {
				final ModelId modelId = new ModelId(name,namespace,version);
				modelRepository.removeModel(modelId);
				return Response.status(Response.Status.OK).build();
				
			}

		});

	}
}
