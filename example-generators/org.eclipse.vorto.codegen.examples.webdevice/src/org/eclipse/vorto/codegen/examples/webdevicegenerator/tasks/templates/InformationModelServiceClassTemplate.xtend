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
package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates

import org.eclipse.vorto.codegen.api.tasks.ITemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil

class InformationModelServiceClassTemplate implements ITemplate<InformationModel> {
	
	override getContent(InformationModel model) {
		return '''
		package «ModuleUtil.getInfoModelServicePackage(model)»;
		
		import javax.ws.rs.GET;
		import javax.ws.rs.Path;
		import javax.ws.rs.Produces;
		import javax.ws.rs.core.MediaType;
		import javax.ws.rs.core.Response;
		
		import «ModuleUtil.getInfoModelModelPackage(model)».«model.name.toFirstUpper»Model;
		
		@Path("/informationmodel")
		public class «model.name.toFirstUpper»Service {
			
			private «model.name.toFirstUpper»Model infomodelInstance = new «model.name.toFirstUpper»Model();
			
			@GET
			@Path("/instance")
			@Produces(MediaType.APPLICATION_JSON)
			public Response getInstance(){
				return Response.status(200).entity(infomodelInstance).header("Access-Control-Allow-Origin", "*").build();
			}
		}'''
	}
	
}