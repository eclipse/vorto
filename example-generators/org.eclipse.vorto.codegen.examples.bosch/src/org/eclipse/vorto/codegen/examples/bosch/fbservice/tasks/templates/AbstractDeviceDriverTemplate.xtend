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

package org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class AbstractDeviceDriverTemplate extends AbstractSourceTemplate implements IFileTemplate<FunctionblockModel> {
	
	override getFileName(FunctionblockModel ctx) {
		return "AbstractDeviceDriver.java";
	}
		
	override getContent(FunctionblockModel context,InvocationContext invocationContext) {
		'''
package «new FbModelWrapper(context).javaPackageName».api.osgidriver;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.device.Device;
import org.osgi.service.device.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosch.ism.IFunctionBlockInstanceService;

/**
 * Do not modify, unless you absolutely must!
 *  
 * @param <DeviceDriver> Driver that is used for the communication to the device
 * @param <DeviceService> Function Block Service Implementation that implements the behavior as defined in the Function Block Model
 */
public abstract class AbstractDeviceDriver<DeviceDriver, DeviceService extends IFunctionBlockInstanceService>
		implements Driver {
	protected static final Logger logger = LoggerFactory
			.getLogger(AbstractDeviceDriver.class);

	protected final BundleContext context;
	protected final Filter filter;
	
	/**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            the OSGi Bundle Context.
	 * @throws InvalidSyntaxException
	 *             if the FILTER_STRING has an Invalid Syntax.
	 */
	public AbstractDeviceDriver(final BundleContext bundleContext)
			throws InvalidSyntaxException {
		this.context = bundleContext;
		filter = bundleContext.createFilter(getDeviceDriverFilter());
	}

	/**
	 * Match method will be triggered when a new service is registered within
	 * OSGI
	 * 
	 * @see org.osgi.service.device.Driver#match(org.osgi.framework.ServiceReference)
	 */
	@SuppressWarnings("rawtypes")
	public int match(final ServiceReference reference)
			throws Exception {
		logger.trace("checking if ServiceReference '{}' matches filter {}",
				reference, getDeviceDriverFilter());

		if (filter.match(reference)) {
			logger.trace("ServiceReference matches filter, returning '{}'",
					getMatchValue());
			return getMatchValue();
		} else {
			logger.trace("ServiceReference '{}' not matching filter", reference);
			return Device.MATCH_NONE;
		}
	}

	protected abstract int getMatchValue();

	protected abstract String getDeviceDriverFilter();

	/**
	 * Will be called when the match result is the highest to be found Creates a
	 * Device Service
	 * 
	 * @see org.osgi.service.device.Driver#attach(org.osgi.framework.ServiceReference)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String attach(final ServiceReference reference) {
		logger.trace("attaching new Device Service for ServiceReference '{}'",
				reference);
		newInstance(context, reference);
		return null;
	}

	protected abstract DeviceService newInstance(BundleContext context,
			ServiceReference<DeviceDriver> reference);
}
		'''
	}
	
	override getSubPath() {
		return "api/osgidriver";
	}
	
	
	
}
