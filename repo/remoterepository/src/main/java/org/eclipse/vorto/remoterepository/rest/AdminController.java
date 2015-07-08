/*******************************************************************************
/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/

package org.eclipse.vorto.remoterepository.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.eclipse.vorto.remoterepository.rest.utils.RestCallback;
import org.eclipse.vorto.remoterepository.rest.utils.RestTemplate;
import org.eclipse.vorto.remoterepository.service.indexing.IIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller for various administration stuff like reindexing the models
 *
 */
@Path("/admin")
@Component
public class AdminController {

	@Autowired
	private IIndexService indexService;

	private static final RestTemplate restTemplate = new RestTemplate();

	/**
	 * ReIndex all models in vorto repository
	 */
	@Path("/reindexall")
	@GET
	public Response reIndexAll() {
		return restTemplate.execute(new RestCallback() {

			@Override
			public Response execute() throws Exception {
				indexService.indexAll();
				return Response.ok().build();
			}
		});
	}
}
