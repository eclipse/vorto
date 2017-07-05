package org.eclipse.vorto.codegen.webui.templates.service

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class QueryTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''Query.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service;
		
		public interface Query {
			
			/**
			 * Query Expression
			 */
			String getValue();
		}
		'''
	}
	
}