package org.eclipse.vorto.codegen.protobuf.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.model.Model
import org.eclipse.vorto.codegen.api.InvocationContext

class ProtobufMetaTemplate implements IFileTemplate<Model> {
	
	override getFileName(Model context) {
		'''meta.proto'''
	}
	
	override getPath(Model context) {
		'''org/eclipse/vorto'''
	}
	
	override getContent(Model element, InvocationContext context) {
		'''
		syntax = "proto3";
		
		package org.eclipse.vorto;
		
		message Meta {
		    enum Type {
		        UNDEFINED = 0;
		        DATATYPE = 1;
		        FUNCTION_BLOCK = 2;
		        INFORMATION_MODEL = 3;
		    }
		    string namespace = 1;
		    string name = 2;
		    string version = 3;
		    Type type = 4;
		    string display_name = 5;
		    string description = 6;
		    string category = 7;
		}
		'''
	}
	
}