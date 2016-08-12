package org.eclipse.vorto.codegen.examples.webui.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class DeviceInfoTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		return "DeviceInfo.java"
	}
	
	override getPath(InformationModel context) {
		return "webdevice.example/src/main/java/webdevice/example/model"
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
		package webdevice.example.model;
		
		public class DeviceInfo {
			
			private String displayName = "«context.displayname»";
			private String description = "«context.description»";
			private String namespace = "«context.namespace»";
			private String category = "«context.category»";
			private String version = "«context.version»";			
						
			public String getDisplayName() {
				return displayName;
			}
		
			public String getDescription() {
				return description;
			}
		
			public String getNamespace() {
				return namespace;
			}
		
			public String getCategory() {
				return category;
			}
		
			public String getVersion() {
				return version;
			}
		}
		'''
	}
	
}
