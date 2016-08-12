package org.eclipse.vorto.codegen.examples.mqtt.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class PomTemplate implements IFileTemplate<FunctionblockModel> {

	override getFileName(FunctionblockModel context) {
		return "pom.xml"
	}
	
	override getPath(FunctionblockModel context) {
		return "mqtt.example.client"
	}
	
	override getContent(FunctionblockModel context,InvocationContext invocationContext) {
		'''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
			<parent>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-parent</artifactId>
				<version>1.3.0.RELEASE</version>
			</parent>
			<groupId>mqtt.example</groupId>
			<artifactId>mqtt.example.client</artifactId>
			<version>1.0.0-SNAPSHOT</version>
			<dependencies>
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter</artifactId>
				</dependency>
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-web</artifactId>
				</dependency>
				<dependency>
					<groupId>org.springframework.integration</groupId>
					<artifactId>spring-integration-mqtt</artifactId>
				</dependency>
				<dependency>
					<groupId>java.example</groupId>
					<artifactId>java.example.model</artifactId>
					<version>1.0.0-SNAPSHOT</version>
				</dependency>
			</dependencies>
		</project>
		'''
	}
}
