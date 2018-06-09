package org.eclipse.vorto.codegen.webui.templates.service.sample

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class SampleDataServiceTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''SampleDataService.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/sample'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.sample;
		
		import java.util.ArrayList;
		import java.util.Collections;
		import java.util.Date;
		import java.util.List;
		
		import org.eclipse.vorto.repository.api.ModelId;
		
		import com.example.iot.«element.name.toLowerCase».model.*;
		import com.example.iot.«element.name.toLowerCase».service.DataService;
		import com.example.iot.«element.name.toLowerCase».service.Query;
		
		/**
		 * Implement this Service to fetch thing data 
		 */
		public class SampleDataService implements DataService {
		
			@Override
			public Query newQuery() {
				return new Query() {
		
					@Override
					public String getValue() {
						return "*";
					}};
			}
		
			@Override
			public List<«element.name»> queryThings(Query query) {
				return Collections.emptyList();
			}
		
			@Override
			public «element.name» getThing(String thingId) {
				return null;
			}
		
			@Override
			public void registerAsyncCallback(String thingId, DataCallback callback) {
			}
		
			@Override
			public void deregisterAsyncCallback(String thingId) {
			}
		}
		
		'''
	}
	
}