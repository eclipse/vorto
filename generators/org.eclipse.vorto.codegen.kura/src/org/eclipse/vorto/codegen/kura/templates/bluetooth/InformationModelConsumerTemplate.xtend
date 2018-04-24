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
package org.eclipse.vorto.codegen.kura.templates.bluetooth

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.kura.templates.Utils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * 
 * @author Erle Czar Mantos - Robert Bosch (SEA) Pte. Ltd.
 *
 */
class InformationModelConsumerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«context.name»Consumer.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.getJavaPackageBasePath(context)»'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
package «Utils.getJavaPackage(element)»;

import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import «Utils.getJavaPackage(element)».cloud.IDataService;
import «Utils.getJavaPackage(element)».model.«element.name»;

public class «element.name»Consumer implements Consumer<Optional<«element.name»>> {

	private static final Logger logger = LoggerFactory.getLogger(«element.name»Consumer.class);
	
	private «element.name»Configuration configuration;
	private IDataService dataService;
	
	public «element.name»Consumer(«element.name»Configuration configuration, IDataService dataService) {
		this.configuration = configuration;
		this.dataService = dataService;
	}

	@Override
	public void accept(Optional<«element.name»> «element.name.toLowerCase») {
		if («element.name.toLowerCase».isPresent()) {
			«element.name» my«element.name» = «element.name.toLowerCase».get();
			
			«FOR fbProperty : element.properties»
			if (configuration.enable«fbProperty.name.toFirstUpper») {
				dataService.publish«fbProperty.name.toFirstUpper»(my«element.name».getResourceId(), my«element.name».get«fbProperty.name.toFirstUpper»());
			}
			
			«ENDFOR»
		} else {
			logger.info("No «element.name» information available.");
		}
	}
}
'''
	}
	
}