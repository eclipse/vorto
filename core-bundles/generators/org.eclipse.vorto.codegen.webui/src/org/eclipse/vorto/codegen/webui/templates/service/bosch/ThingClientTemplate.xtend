package org.eclipse.vorto.codegen.webui.templates.service.bosch

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingClientTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''ThingClient.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch;
		
		import java.util.concurrent.CompletableFuture;
		
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.Thing;
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.ThingSearchResult;
		
		public interface ThingClient {
							
			/**
			 * Searches Bosch IoT Things for the given query
			 * @param query
			 * @return
			 */
		    CompletableFuture<ThingSearchResult> searchThings(String query);
		        
		    /**
		     * Gets a Bosch IoT Thing for the given id
		     * @param thingId
		     * @return
		     */
		    CompletableFuture<Thing> getThing(String thingId);
		    
		    static ThingClientBuilder newBuilder() {
				return new ThingClientBuilder();
			}
		}
		
		'''
	}
	
}