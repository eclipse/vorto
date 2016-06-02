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

class EventMappingsConfigurationTemplate extends AbstractSourceTemplate implements IFileTemplate<FunctionblockModel> {
	
	override getFileName(FunctionblockModel ctx) {
		return "EventMappingsConfiguration.java";
	}
		
	override getContent(FunctionblockModel context,InvocationContext invocationContext) {
		'''
package «new FbModelWrapper(context).javaPackageName».api.mapping;

import java.util.HashMap;
import java.util.Map;

import com.bosch.ism.ICustomEventBuilderFactory;

/**
 * Do not modify, unless you absolutely must!
 *
 */
public class EventMappingsConfiguration {
	
	private static EventMappingsConfiguration INSTANCE = null;
	
	private Map<String, IEventMapping<?>> rules = new HashMap<>();

	private ICustomEventBuilderFactory customEventBuilderFactory;
	
	private EventMappingsConfiguration() {	
	}
	
	public static EventMappingsConfiguration getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EventMappingsConfiguration();
		}
		return INSTANCE;
	}
		
	@SuppressWarnings("unchecked")
	public <Property> IEventMapping<Property> getMappingByTopic(String topic, Class<Property> propertyClass) {
		IEventMapping<?> mapping =  rules.get(topic);
		
		if (mapping instanceof AbstractEventMapping) {
			((AbstractEventMapping<?>)mapping).setCustomEventBuilderFactory(customEventBuilderFactory);
		}
		
		return (IEventMapping<Property>) mapping;
	}

	public void setCustomEventBuilderFactory(ICustomEventBuilderFactory customEventBuilderFactory) {
		this.customEventBuilderFactory = customEventBuilderFactory;
	}
	
	public void registerMapping(String topicName, IEventMapping<?> mapping) {
		this.rules.put(topicName, mapping);
	}
	
}
		'''
	}
	
	override getSubPath() {
		return "api/mapping";
	}
	
	
	
}
