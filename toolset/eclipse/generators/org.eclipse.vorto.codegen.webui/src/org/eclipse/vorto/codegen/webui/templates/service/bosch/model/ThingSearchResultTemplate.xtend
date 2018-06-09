package org.eclipse.vorto.codegen.webui.templates.service.bosch.model

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingSearchResultTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''ThingSearchResult.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch/model'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch.model;
		
		import java.util.Collection;
		
		public interface ThingSearchResult {
		
			int getNextPageOffset();
			
			Collection<Thing> getThings();
				
		}
		
		'''
	}

}