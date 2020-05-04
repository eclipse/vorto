/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.codegen.template.runner

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class GeneratorConfigurationTemplate implements IFileTemplate<InformationModel>{
	
	override getFileName(InformationModel context) {
		'GeneratorConfiguration.java'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-runner/src/main/java/com/mycompany/runner'
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.mycompany.runner;
		
		import com.mycompany.plugin.«context.configurationProperties.getOrDefault("servicekey","Myplatform").replace(" ","").toLowerCase.toFirstUpper»Generator;
		import org.eclipse.vorto.codegen.spi.config.AbstractGeneratorConfiguration;
		import org.eclipse.vorto.codegen.spi.model.Generator;
		import org.springframework.stereotype.Component;
		
		@Component
		public class GeneratorConfiguration extends AbstractGeneratorConfiguration {
			
			@Override
			protected void doSetup() {
				addGenerator(Generator.create("/generators/«context.configurationProperties.getOrDefault("servicekey","Myplatform").replace(" ","").toLowerCase».properties", «context.configurationProperties.getOrDefault("servicekey","MyPlatform").replace(" ","").toLowerCase.toFirstUpper»Generator.class));
			}
		}
		'''
	}
	
}