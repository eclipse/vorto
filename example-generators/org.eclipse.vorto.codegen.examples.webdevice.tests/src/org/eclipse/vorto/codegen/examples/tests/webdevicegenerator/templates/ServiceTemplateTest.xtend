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
 package org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.templates

import org.eclipse.vorto.codegen.examples.tests.TestFunctionblockModelFactory
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.ServiceClassTemplate
import org.junit.Test

import static org.junit.Assert.assertEquals

class ServiceClassTemplateTest {

	@Test
	def testGeneration() {
		var fbProperty = TestFunctionblockModelFactory.createFBProperty();

		var result = new ServiceClassTemplate().getContent(fbProperty);
		assertEquals(fetchExpected, result);
	}

	private def String fetchExpected() {
		'''package org.eclipse.vorto.iot.fridge.service;

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

import org.eclipse.vorto.iot.fridge.model.Fridge;
import org.eclipse.vorto.iot.fridge.model.FridgeConfiguration;		

@Path("/Fridge")
public class FridgeService {	
	private static Logger logger = Logger.getLogger("Fridge"); 		
	private static Fridge fridgeinstance = new Fridge();

	@GET
	@Path("/instance")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInstance(){
		return Response.status(200).entity(fridgeinstance).header("Access-Control-Allow-Origin", "*").build();
	}		
								
	/**
	 * Turn device on
	 */
	@PUT
	@Path("/on")					 
	public void on() {
		//Please handle your operation here
		logger.info("on invoked");				
	}				
				
	/**
	 * Turn device off
	 */
	@PUT
	@Path("/Off")					 
	public void Off() {
		//Please handle your operation here
		logger.info("Off invoked");				
	}				
				
	/**
	 * Toggle device
	 */
	@PUT
	@Path("/Toggle")					 
	public void Toggle() {
		//Please handle your operation here
		logger.info("Toggle invoked");				
	}				
				

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/saveConfiguration")
	@SuppressWarnings("unchecked")
	public void saveConfiguration(Object configurationData)
			throws IllegalAccessException, InvocationTargetException {
		logger.info("saveConfiguration invoked: " + configurationData);
		Map<String, String> rawMap = (Map<String, String>) configurationData;

		FridgeConfiguration configuration = fridgeinstance.getConfiguration();
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
