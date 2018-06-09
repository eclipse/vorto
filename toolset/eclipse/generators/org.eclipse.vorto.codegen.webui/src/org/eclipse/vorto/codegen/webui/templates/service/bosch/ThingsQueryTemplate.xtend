package org.eclipse.vorto.codegen.webui.templates.service.bosch

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingsQueryTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''ThingsQuery.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch;
		
		import java.io.UnsupportedEncodingException;
		import java.net.URLEncoder;
		
		import com.example.iot.«element.name.toLowerCase».service.Query;
		
		public class ThingsQuery implements Query {
		
			private StringBuilder builder = new StringBuilder();
			
			public ThingsQuery() {
				withFilter("eq(attributes/_modelId,\"«element.namespace».«element.name»:«element.version»\")");
			}
			
			public void withFilter(String filter) {
				if (builder.length() > 0) {
					builder.append("&");
				}
				builder.append("filter=");
				builder.append(encode(filter));
			}
			
			private String encode(String param) {
				try {
					return URLEncoder.encode(param,"utf-8");
				} catch (UnsupportedEncodingException e) {
					throw new IllegalArgumentException("invalid url");
				}
			}
			
			public void withOptions(String options) {
				if (builder.length() > 0) {
					builder.append("&");
				}
				builder.append("option=");
				builder.append(encode(options));
			}
		
			public String getValue() {
				return builder.toString();
			} 
			
		}

		'''
	}
	
}