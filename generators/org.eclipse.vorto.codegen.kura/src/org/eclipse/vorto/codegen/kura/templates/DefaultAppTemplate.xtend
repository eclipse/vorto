/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.codegen.kura.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.kura.templates.cloud.TypeMapper
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.model.ModelIdFactory
import org.eclipse.vorto.core.api.model.model.ModelType

class DefaultAppTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«context.name»App.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.getJavaPackageBasePath(context)»'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
package «Utils.getJavaPackage(element)»;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

«FOR reference : element.references»
«var modelId = ModelIdFactory.newInstance(ModelType.Functionblock,reference)»
import «Utils.getJavaPackage(element)».model.«modelId.name»;
«ENDFOR»
import «Utils.getJavaPackage(element)».cloud.IDataService;
«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
import «Utils.getJavaPackage(element)».cloud.bosch.BoschDataService;
«ELSE»
import org.eclipse.kura.cloud.CloudService;
«ENDIF»

public class «element.name»App {

	private static final Logger s_logger = LoggerFactory.getLogger(«element.name»App.class);
    private static final String APP_ID = "«Utils.getJavaPackage(element)»";
    
    private ScheduledExecutorService m_worker;
   	private ScheduledFuture<?> m_handle;
   	
   	private IDataService dataService;
   	
   	private String thingId = "INSERT THING ID HERE";

	«IF !context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
	private CloudService cloudService;
	
	public void setCloudService(CloudService cloudService) {
		this.cloudService = cloudService;
	}
	
	public void unsetCloudService(CloudService cloudService) {
		this.cloudService = null;
	}
	«ENDIF»

   	public «element.name»App() {
   		m_worker = Executors.newSingleThreadScheduledExecutor();
«IF context.configurationProperties.getOrDefault("boschcloud", "false").equalsIgnoreCase("true")»
		dataService = new BoschDataService("","wss://events.apps.bosch-iot-cloud.com");
«ELSE»
		dataService = new KuraCloudDataService(cloudService);
«ENDIF»
   	}
   	
    protected void activate(ComponentContext componentContext) {
        s_logger.info("Bundle " + APP_ID + " has started!");
        
     // cancel a current worker handle if one if active
   		if (m_handle != null) {
   			m_handle.cancel(true);
   		}

   		// schedule a new worker
   		m_handle = m_worker.scheduleAtFixedRate(new Runnable() {
   			@Override
   			public void run() {
   				Thread.currentThread().setName(getClass().getSimpleName());
   				
   				«FOR fbProperty : element.properties»
   				«fbProperty.type.name» «fbProperty.name» = new «fbProperty.type.name»();
   				«FOR statusProperty : fbProperty.type.functionblock.status.properties»
   					«fbProperty.name».set«TypeMapper.checkKeyword(statusProperty.name).toFirstUpper»(«TypeMapper.getRandomValue(statusProperty.type)»);
   				«ENDFOR»
   				dataService.publish«fbProperty.name.toFirstUpper»(thingId, «fbProperty.name»);
   				
   		    	«ENDFOR»
   			}
   		}, 0, 300, TimeUnit.SECONDS);
    }

    protected void deactivate(ComponentContext componentContext) {
        s_logger.info("Bundle " + APP_ID + " has stopped!");
    }
}

'''
	}
	
}