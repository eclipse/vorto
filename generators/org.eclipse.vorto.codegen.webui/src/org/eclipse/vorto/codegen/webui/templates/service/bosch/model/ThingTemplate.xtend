package org.eclipse.vorto.codegen.webui.templates.service.bosch.model

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''Thing.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch/model'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch.model;
		
		import java.text.DateFormat;
		import java.text.SimpleDateFormat;
		import java.util.Date;
		import java.util.Map;
		
		import com.example.iot.«element.name.toLowerCase».service.bosch.ThingBuilder;
		
		public interface Thing extends ThingTypeAware {
		
			static final DateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
			/**
			 * 
			 * @return
			 */
			String getId();
			
			/**
			 * 
			 * @return
			 */
			String getName();
			
			/**
			 * 
			 * @return
			 */
			Date getCreatedOn();
			
			/**
			 * 
			 * @return
			 */
			String getDeviceId();
			
			/**
			 * 
			 * @return
			 */
			String getCreator();
			
			/**
			 * 
			 * @return
			 */
			Map<String,Object> listAttributes();
			
			/**
			 * 
			 * @return
			 */
			Map<String,AclEntry> listPermissions();
			
			/**
			 * 
			 * @return
			 */
			Map<String,Feature> listFeatures();
			
			static ThingBuilder newBuilder() {
				return new ThingBuilder();
			}
			
		}
		
		'''
	}

}