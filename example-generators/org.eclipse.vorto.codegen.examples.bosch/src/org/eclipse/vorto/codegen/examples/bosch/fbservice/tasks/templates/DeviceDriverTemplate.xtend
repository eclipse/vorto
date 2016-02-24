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
package org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates

import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper

class DeviceDriverTemplate {
		
	public static def String generate(FbModelWrapper context) {
		'''
		package «context.javaPackageName».internal.osgidriver;
		import org.osgi.framework.BundleContext;
		import org.osgi.framework.Constants;
		import org.osgi.framework.InvalidSyntaxException;
		import org.osgi.framework.ServiceReference;
		
		import com.bosch.functionblock.dummy.api.device.IDummyDevice;
		
		import «context.javaPackageName».internal.«context.functionBlockName»Service;
		
		import «context.javaPackageName».api.osgidriver.AbstractDeviceDriver;
		
		public class «context.functionBlockName»DeviceDriver extends AbstractDeviceDriver<IDummyDevice,«context.model.name»Service> {
			
			public «context.functionBlockName»DeviceDriver(BundleContext bundleContext) throws InvalidSyntaxException {
				super(bundleContext);
			}
			
			@Override
			protected int getMatchValue() {
				return 10;
			}

			@Override
			protected String getDeviceDriverFilter() {
				return "(" + Constants.OBJECTCLASS + "=«getDeviceFullQualifiedName(context)»)";
			}	
					
			@Override
			protected «context.model.name»Service newInstance(BundleContext context, ServiceReference<IDummyDevice> reference) {
				return new «context.model.name»Service(context, reference);		
			}
		}
		
		'''
	}

	def static getDeviceFullQualifiedName(FbModelWrapper context) {
		return "com.bosch.functionblock.dummy.internal.device." + context.functionBlockName.toLowerCase + ".Dummy" + context.functionBlockName + "Device";
	}	
}
	