package org.eclipse.vorto.codegen.webui.templates.service

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class DataServiceTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''DataService.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service;
		
		import java.util.List;
		
		import com.example.iot.«element.name.toLowerCase».model.«element.name»;
		
		public interface DataService {
			
			/**
			 * creates a new query for the retrieval of things
			 */
			Query newQuery();
		
		    /**
		     * queries things from the configured IoT Cloud backend
		     */
			List<«element.name»> queryThings(Query query);
		    
		    /**
		     * gets a specific thing from the IoT Cloud backend
		     */
			«element.name» getThing(String thingId);
			
			void registerAsyncCallback(String thingId, DataCallback callback);
			
			void deregisterAsyncCallback(String thingId);
			
			public interface DataCallback {
				void onChange(«element.name» thing);
			}
		
		}
		'''
	}
	
}