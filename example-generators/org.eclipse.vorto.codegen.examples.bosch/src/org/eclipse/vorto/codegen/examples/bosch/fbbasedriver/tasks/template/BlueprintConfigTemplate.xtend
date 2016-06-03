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
package org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.template

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.BaseDriverUtil
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class BlueprintConfigTemplate implements ITemplate<FunctionblockModel> {
	
	override getContent(FunctionblockModel model,InvocationContext invocationContext) {
		'''<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0" xmlns:ext="http://aries.apache.org/blueprint/xmlns/blueprint-ext/v1.0.0" default-activation="eager">
 	<service id="serviceReg" ref="baseDriverService" interface="org.osgi.service.cm.ManagedServiceFactory">
		<service-properties>
			<entry key="service.pid" value="«BaseDriverUtil.getDriverPackage()».DummyBaseDriver" />
		</service-properties>
	</service>

	<bean id="baseDriverService"
		class="«BaseDriverUtil.getDriverPackage()».DummyBaseDriver">
		<argument ref="blueprintBundleContext" />
	</bean>
</blueprint>'''
	}
	
}
