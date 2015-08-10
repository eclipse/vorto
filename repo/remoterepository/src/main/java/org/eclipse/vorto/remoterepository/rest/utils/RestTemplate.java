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
package org.eclipse.vorto.remoterepository.rest.utils;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.vorto.remoterepository.rest.ModelResource;
import org.eclipse.vorto.remoterepository.rest.exceptions.ModelNotFoundException;
import org.eclipse.vorto.remoterepository.rest.exceptions.ModelTypeNotFound;
import org.eclipse.vorto.remoterepository.rest.exceptions.ResourceNotFoundException;

public class RestTemplate {
	
	private Log log = LogFactory.getLog(RestTemplate.class);

	public Response execute(RestCallback callback) {
		try {
			return callback.execute();
		} catch (ModelNotFoundException ex) {
			log.error(ex);
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (ModelTypeNotFound ex) {
			log.error(ex);
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (ResourceNotFoundException ex) {
			log.error(ex);
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (Exception ex) {
			log.error(ex);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}
}
