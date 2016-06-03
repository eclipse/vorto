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

class EventMappingContextTemplate extends AbstractSourceTemplate implements IFileTemplate<FunctionblockModel> {
	
	override getFileName(FunctionblockModel ctx) {
		return "EventMappingContext.java";
	}
		
	override getContent(FunctionblockModel context,InvocationContext invocationContext) {
		'''
package «new FbModelWrapper(context).javaPackageName».api.mapping;

import com.bosch.ism.IInformationModelInstance;

/**
 * Do not modify, unless you absolutely must! 
 *
 * @param <Properties> FunctionBlock Properties Object as defined in the Function Block Model
 */
public class EventMappingContext<Properties> {
	private String eventTopic;
	private IInformationModelInstance informationModelInstance;
	
	private Properties properties;
	
	public EventMappingContext(String eventTopic,
			IInformationModelInstance informationModelInstance) {
		super();
		this.eventTopic = eventTopic;
		this.informationModelInstance = informationModelInstance;
	}

	public String getEventTopic() {
		return eventTopic;
	}

	public IInformationModelInstance getInformationModelInstance() {
		return informationModelInstance;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
}
		'''
	}
	
	override getSubPath() {
		return "api/mapping";
	}
	
	
	
}
