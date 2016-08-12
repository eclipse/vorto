package org.eclipse.vorto.codegen.examples.javabean.tasks.template

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class PomFileTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		return "pom.xml";
	}
	
	override getPath(InformationModel context) {
		return "java.example.model";
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
			<groupId>java.example</groupId>
			<artifactId>java.example.model</artifactId>
			<version>1.0.0-SNAPSHOT</version>
		</project>
		'''
	}
	
}