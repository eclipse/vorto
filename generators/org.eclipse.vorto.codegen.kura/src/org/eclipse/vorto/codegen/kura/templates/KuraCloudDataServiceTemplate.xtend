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
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.model.ModelIdFactory
import org.eclipse.vorto.core.api.model.model.ModelType

/**
 * 
 * @author Erle Czar Mantos - Robert Bosch (SEA) Pte. Ltd.
 *
 */
class KuraCloudDataServiceTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''KuraCloudDataService.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.getJavaPackageBasePath(context)»'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
package «Utils.getJavaPackage(element)»;

import java.util.Date;
import java.util.Objects;

import org.eclipse.kura.KuraException;
import org.eclipse.kura.cloud.CloudClient;
import org.eclipse.kura.cloud.CloudClientListener;
import org.eclipse.kura.cloud.CloudService;
import org.eclipse.kura.message.KuraPayload;
import org.osgi.service.component.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

«FOR reference : element.references»
«var modelId = ModelIdFactory.newInstance(ModelType.Functionblock,reference)»
import «Utils.getJavaPackage(element)».model.«modelId.name»;
«ENDFOR»

import «Utils.getJavaPackage(element)».cloud.IDataService;

public class KuraCloudDataService implements IDataService, CloudClientListener {
	
	private static final Logger logger = LoggerFactory.getLogger(KuraCloudDataService.class);
	
	private final String APP_ID = "BLE_APP_V1";
	private CloudClient cloudClient;
	private String topic = "data";
	
	public KuraCloudDataService(CloudService cloudService, «element.name»Configuration configuration) {
		this.topic = Objects.requireNonNull(configuration.publishTopic);
		try {
			cloudClient = cloudService.newCloudClient(this.APP_ID);
			cloudClient.addCloudClientListener(this);
		} catch (KuraException e1) {
			logger.error("Error starting component", e1);
			throw new ComponentException(e1);
		}
	}

	«FOR fbProperty : element.properties»
	@Override
	public void publish«fbProperty.name.toFirstUpper»(String resourceId, «fbProperty.type.name» data) {
		// send accelerometer to iot cloud backend
		KuraPayload payload = new KuraPayload();
		payload.setTimestamp(new Date());
		payload.addMetric("«fbProperty.type.name.toLowerCase»", data);
	
		try {
			if (!payload.metricNames().isEmpty()) {
				cloudClient.publish(topic + "/" + resourceId, payload, 0, false);
			}
			
		} catch (Exception e) {
			logger.error("Problem sending data to cloud", e);
		}
	}
	
	«ENDFOR»
	@Override
	public void onConnectionEstablished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionLost() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onControlMessageArrived(String arg0, String arg1, KuraPayload arg2, int arg3, boolean arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageArrived(String arg0, String arg1, KuraPayload arg2, int arg3, boolean arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageConfirmed(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessagePublished(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
}
'''
	}
	
}