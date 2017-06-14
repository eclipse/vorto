package org.eclipse.vorto.codegen.webui.templates.service.bosch.internal.model

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class FeatureImplTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''FeatureImpl.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch/internal/model'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch.internal.model;
		
		import java.util.HashMap;
		import java.util.Map;
		
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.Feature;
		
		public class FeatureImpl implements Feature {
		
			private String id;
			
			private Map<String,Object> content = new HashMap<String, Object>();
			
			private Map<String,Object> properties = new HashMap<String, Object>();
			
			public static final String FAULT = "fault";
		
			public static final String CONFIGURATION = "configuration";
		
			public static final String STATUS = "status";
			
			public FeatureImpl(String id) {
				this.id = id;
				this.content.put("properties", properties);
			}
			
			@SuppressWarnings("unchecked")
			public FeatureImpl(String id, Map<String,Object> content) {
				this.id = id;
				this.content = content;
				this.properties.putAll((Map<String,Object>)this.content.get("properties"));
			}
			
			protected FeatureImpl() {
				
			}
		
			public String getId() {
				return id;
			}
		
			public void setId(String id) {
				this.id = id;
			}
		
			public Map<String, Object> getContent() {
				return content;
			}
			
			public Map<String, Object> getProperties() {
				return properties;
			}
		
			@Override
			public String getThingTypeId() {
				return (String)this.properties.get(ThingImpl.ATTRIBUTE_MODELID);
			}
		
			@Override
			public boolean hasThingType() {
				return this.properties.containsKey(ThingImpl.ATTRIBUTE_MODELID);
			}
		}
		
		'''
	}
	
}
