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
package org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.template.device

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.BaseDriverUtil
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class AbstractDummyDeviceTemplate implements ITemplate<FunctionblockModel> {
	
	override getContent(FunctionblockModel model,InvocationContext context) {
		'''package «BaseDriverUtil.getDevicePackage»;

import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosch.functionblock.dummy.api.device.IDummyDevice;

public abstract class AbstractDummyDevice implements IDummyDevice {

	protected final Logger logger = LoggerFactory
			.getLogger(this.getClass().getName());
	
	protected BundleContext bundleContext;
	protected String serialNo;

	public AbstractDummyDevice(BundleContext bundleContext, String serialNo) {
		this.bundleContext = bundleContext;
		this.serialNo = serialNo;
	}

	@Override
	public String getSerialNo() {
		return serialNo;
	}

	/**
	 * Get OSGI event admin
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected EventAdmin getEventAdmin() {
		ServiceReference ref = this.bundleContext
				.getServiceReference(EventAdmin.class.getName());
		return (EventAdmin) this.bundleContext.getService(ref);
	}

	@Override
	public void sendEvent(String eventName, Map<String, ?> payload) {
		Event event = new Event(this.getEventTopic(eventName), payload);
		this.getEventAdmin().sendEvent(event);
		logger.trace("Sent event with topic: {}, payload: {}", this.getEventTopic(eventName), payload);		
	}
}'''
	}
	
}
