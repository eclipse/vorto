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
package org.eclipse.vorto.codegen.service.bosch.fbservice.tasks.templates

import org.eclipse.vorto.codegen.service.bosch.common.FbModelWrapper
import org.eclipse.vorto.core.api.model.functionblock.Operation

class FbServiceClassTemplate {
		
	public static def String generate(FbModelWrapper context) {
		'''
		package «context.javaPackageName».internal;
		
		import java.util.Dictionary;
		import java.util.HashSet;
		import java.util.Set;

		import org.osgi.framework.BundleContext;
		import org.osgi.framework.ServiceReference;
		
		import «getPackage(context) + context.functionBlockName»;
		«FOR op : context.model.functionblock.operations»
			import «getPackage(context) + createCamelCasedClassName(op.name)»;
			«IF op.returnType != null»
				import «getPackage(context) + createReplyClassName(op.name)»;
			«ENDIF»
		«ENDFOR»
		import «getPackage(context)»«context.model.name»Properties;
		«FOR event : context.model.functionblock.events»
			import «context.javaPackageName + ".internal.mapping." + event.name.toFirstUpper»Mapping;
		«ENDFOR»
		import com.bosch.functionblock.dummy.api.device.IDummyDevice;
		
		import «context.javaPackageName».api.AbstractDeviceService;
		import «context.javaPackageName».api.mapping.EventMappingsConfiguration;
		import com.bosch.ism.InformationModelConstants;
		
		public class «context.model.name»Service extends AbstractDeviceService<IDummyDevice, «context.model.name»Properties> implements «context.model.name» {
		
			public «context.model.name»Service(final BundleContext context, final ServiceReference<IDummyDevice> reference) {
				super(context, reference, «context.model.name»Properties.class);
			}
			
			@Override
			protected Dictionary<String, String> addInformationModelInstanceProperties(Dictionary<String, String> properties) {
				properties.put(InformationModelConstants.DESCRIPTION,"«context.model.description»");
				properties.put(InformationModelConstants.CATEGORY, "«context.model.category»");
				properties.put(org.osgi.service.device.Constants.DEVICE_CATEGORY, "«context.functionBlockName»FB");
				return properties;
			}
				
			@Override
			protected Class<«context.functionBlockName»> getFunctionBlockModelClass(){
				return «context.functionBlockName».class;
			}	
						
			@Override
			protected Set<String> getEventTopics() {
				Set<String> topics = new HashSet<>();
				«FOR event : context.model.functionblock.events»
				topics.add(getDriverEventTopic("«event.name»"));
				«ENDFOR»
				return topics;
			}

			//Event topic sent from device driver
			private String getDriverEventTopic(String eventName) {
				return "DeviceDriver/«context.model.name»/"  + eventName;		
			}	
			
			@Override
			protected void registerMappingRules(EventMappingsConfiguration configuration) {
				«FOR event : context.model.functionblock.events»
				configuration.registerMapping(getDriverEventTopic("«event.name»"),new «event.name.toFirstUpper»Mapping());
				«ENDFOR»
			}
		
			«FOR operation : context.model.functionblock.operations»
			/**
			 * «operation.description»
			 *
			 «IF !operation.params.empty»
			 * @param «createCamelCasedClassName(operation.name)» part1
			 «ENDIF»
			 «IF operation.returnType != null»
			 * @return «createReplyClassName(operation.name)»
			 «ENDIF»
			 */
			 public «createReturnType(operation)» «operation.name»(«createOperationParameter(operation)») {
			 	logger.trace("method «operation.name» is invoked");
			 	//IInformationModelInstance infoModelInstance = getInformationModelInstance();
			 	deviceDriver.sendCommand("«operation.name»", null);
			 	//TODO: Please implement this method.
			 	«IF operation.returnType != null»return null;«ENDIF»
			 }
			 «ENDFOR»
		}'''
	}
	
	def static createReturnType(Operation operation) {
		'''«IF operation.returnType != null»«createCamelCasedClassName(operation.name)»Reply«ELSE»void«ENDIF»'''
	}
	
	def static createOperationParameter(Operation operation) {
		'''«IF !operation.params.empty»«createCamelCasedClassName(operation.name)» part1«ENDIF»'''
	}
			
	def static getPackage(FbModelWrapper context) {
		context.javaPackageName.toString() + '''._«context.majorVersion».''';
	}
		
	def static createCamelCasedClassName(String methodName) {
		Character.toString(methodName.charAt(0)).toUpperCase() + methodName.substring(1);
	}
	
	def static createReplyClassName(String methodName) {
		createCamelCasedClassName(methodName) + "Reply"
	}
}