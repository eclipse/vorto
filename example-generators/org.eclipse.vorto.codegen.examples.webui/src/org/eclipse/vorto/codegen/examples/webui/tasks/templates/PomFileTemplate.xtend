/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *
 *******************************************************************************/
 package org.eclipse.vorto.codegen.examples.webui.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.examples.webui.tasks.ModuleUtil
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class PomFileTemplate implements IFileTemplate<InformationModel> {
	
	override getContent(InformationModel model,InvocationContext invocationContext) {
		'''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
		  <modelVersion>4.0.0</modelVersion>
		  <parent>
		  		<groupId>org.springframework.boot</groupId>
		  		<artifactId>spring-boot-starter-parent</artifactId>
		  		<version>1.3.0.RELEASE</version>
		  </parent>
		  <groupId>org.eclipse.vorto.examples</groupId>
		  <artifactId>«ModuleUtil.getArtifactId(model)»</artifactId>
		  <version>«model.version»</version>
		  <name>Sample project generated from «model.name»</name>
		  <packaging>war</packaging>
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
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-websocket</artifactId>
				</dependency>
				<dependency>
					<groupId>org.springframework</groupId>
					<artifactId>spring-messaging</artifactId>
				</dependency>
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-tomcat</artifactId>
					<scope>provided</scope>
				</dependency>
				<dependency>
					<groupId>java.example</groupId>
					<artifactId>java.example.model</artifactId>
					<version>1.0.0-SNAPSHOT</version>
				</dependency>
		  </dependencies>
		</project>'''
	}
	
	override getFileName(InformationModel context) {
		return "webdevice.example/pom.xml"
	}
	
	override getPath(InformationModel context) {
		return null;
	}

}
