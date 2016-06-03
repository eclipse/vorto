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

class IEventMappingTemplate extends AbstractSourceTemplate implements IFileTemplate<FunctionblockModel> {
	
	override getFileName(FunctionblockModel ctx) {
		return "IEventMapping.java";
	}
		
	override getContent(FunctionblockModel context,InvocationContext invocationContext) {
		'''
package «new FbModelWrapper(context).javaPackageName».api.mapping;

import org.osgi.service.event.Event;

import com.bosch.ism.ICustomEvent;

/**
 * Do not modify !! 
 * Maps an event from a base driver to a custom based event, which can be processed by the M2M platform. 
 *
 * @param <Properties> FunctionBlock Properties Object as defined in the Function Block Model
 */
public interface IEventMapping<Properties> {

	ICustomEvent map(Event baseDriverEvent, EventMappingContext<Properties> context);
	
	@SuppressWarnings("serial")
	public class MappingProblem extends RuntimeException {
		
		public MappingProblem(String msg, Throwable t) {
			super(msg,t);
		}
		
		public MappingProblem(String msg) {
			super(msg);
		}
	}
}
		'''
	}
	
	override getSubPath() {
		return "api/mapping";
	}
	
	
	
}
