package org.eclipse.vorto.codegen.examples.mqtt.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class IClientHandlerTemplate implements IFileTemplate<FunctionblockModel> {

	override getFileName(FunctionblockModel context) {
		return "IClientHandler.java"
	}
	
	override getPath(FunctionblockModel context) {
		return "mqtt.example.client/src/main/java/mqtt/example"
	}
	
	override getContent(FunctionblockModel context,InvocationContext invocationContext) {
		'''
		package mqtt.example;

		public interface IClientHandler {

			void onDeviceStatusReceived(Object deviceStatus);

		}
		'''
	}
}