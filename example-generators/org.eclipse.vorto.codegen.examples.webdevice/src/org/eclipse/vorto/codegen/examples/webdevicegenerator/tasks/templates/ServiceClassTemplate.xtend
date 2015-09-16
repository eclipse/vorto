/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
 package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates

import org.eclipse.vorto.codegen.api.tasks.ITemplate
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty

class ServiceClassTemplate implements ITemplate<FunctionblockProperty> {
		
	override getContent(FunctionblockProperty fbProperty) {
		var FunctionblockModel model = fbProperty.getType()
		
		return '''
		package «ModuleUtil.getServicePackage(model)»;

		import java.lang.reflect.InvocationTargetException;
		import java.util.HashMap;
		import java.util.Map;
		import java.util.Map.Entry;
		import java.util.logging.Logger;
		
		import javax.ws.rs.Consumes;
		import javax.ws.rs.GET;
		import javax.ws.rs.POST;
		import javax.ws.rs.PUT;
		import javax.ws.rs.Path;
		import javax.ws.rs.Produces;
		import javax.ws.rs.core.MediaType;
		import javax.ws.rs.core.Response;
		
		import org.apache.commons.beanutils.BeanUtils;
		
		import «ModuleUtil.getModelPackage(model)».«fbProperty.name.toFirstUpper»;
		import «ModuleUtil.getModelPackage(model)».«fbProperty.name.toFirstUpper»Configuration;		
		
		@Path("/«fbProperty.name»")
		public class «ModuleUtil.getCamelCase(fbProperty.name)»Service {	
			private static Logger logger = Logger.getLogger("«fbProperty.name»"); 		
			private static «fbProperty.name.toFirstUpper» «fbProperty.name.toFirstLower»instance = new «fbProperty.name.toFirstUpper»();

			@GET
			@Path("/instance")
			@Produces(MediaType.APPLICATION_JSON)
			public Response getInstance(){
				return Response.status(200).entity(«fbProperty.name.toFirstLower»instance).header("Access-Control-Allow-Origin", "*").build();
			}		
										
			«FOR operation : model.functionblock.operations»				
			/**
			 * «operation.description»
			 */
			@PUT
			@Path("/«operation.name»")					 
			public void «operation.name»() {
				//Please handle your operation here
				logger.info("«operation.name» invoked");				
			}				
						
			«ENDFOR»	

			@POST
			@Consumes(MediaType.APPLICATION_JSON)
			@Path("/saveConfiguration")
			@SuppressWarnings("unchecked")
			public void saveConfiguration(Object configurationData)
					throws IllegalAccessException, InvocationTargetException {
				logger.info("saveConfiguration invoked: " + configurationData);
				Map<String, String> rawMap = (Map<String, String>) configurationData;
		
				«ModuleUtil.getCamelCase(fbProperty.name)»Configuration configuration = «fbProperty.name.toFirstLower»instance.getConfiguration();
				BeanUtils.populate(configuration, getMapWithoutKeyPrefix(rawMap));
		
			}
		
			private Map<String, String> getMapWithoutKeyPrefix(
					Map<String, String> rawMap) {
				Map<String, String> mapWithoutKeyPrefix = new HashMap<String, String>();
		
				String prefix = this.getInstance().getClass().getSimpleName()
						+ "_configuration_id_";
				for (Entry<String, String> entry : rawMap.entrySet()) {
					String newKey = entry.getKey().substring(prefix.length());
					mapWithoutKeyPrefix.put(newKey, entry.getValue());
				}
				return mapWithoutKeyPrefix;
			}							
		}'''
	}
	
}