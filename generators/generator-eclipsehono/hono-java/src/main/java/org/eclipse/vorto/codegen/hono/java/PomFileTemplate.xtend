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
package org.eclipse.vorto.codegen.hono.java

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class PomFileTemplate implements IFileTemplate<InformationModel> {
	
	override getContent(InformationModel model,InvocationContext invocationContext) {
		'''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
			<groupId>device.«model.name.toLowerCase»</groupId>
			<artifactId>«model.name.toLowerCase»-app</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		
			<name>«model.displayname» Hono Client</name>
			
			<properties>
				<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
				<maven.compiler.source>1.8</maven.compiler.source>
				<maven.compiler.target>1.8</maven.compiler.target>
			</properties>
		
			<dependencies>
				<dependency>
					<groupId>org.eclipse.paho</groupId>
					<artifactId>org.eclipse.paho.client.mqttv3</artifactId>
					<version>1.2.0</version>
				</dependency>
		
				<dependency>
					<groupId>com.google.code.gson</groupId>
					<artifactId>gson</artifactId>
					<version>2.8.2</version>
				</dependency>
		
				<dependency>
				    <groupId>log4j</groupId>
				    <artifactId>log4j</artifactId>
				    <version>1.2.17</version>
				</dependency>
			</dependencies>
		
		</project>
		'''
	}
	
	override getFileName(InformationModel context) {
		'''pom.xml'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-app'''
	}

}
