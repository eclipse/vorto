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
 package org.eclipse.vorto.codegen.examples.coap.common.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class PomFileTemplate implements IFileTemplate<InformationModel> {
	
	var String artefactId;
	var String mainClass;
	var String projectName;
	
	new (
		String artefactId,
		String mainClass, 
		String projectName){
			this.artefactId = artefactId;
			this.mainClass = mainClass;
			this.projectName = projectName;
			
		}
	
	override getContent(InformationModel model,InvocationContext invocationContext) {
		'''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
			<groupId>org.eclipse.vorto.examples</groupId>
			<artifactId>«artefactId»</artifactId>
			<version>«model.version»</version>
			<name>Sample project generated from «model.name»</name>
			<packaging>jar</packaging>
			 
			<properties>
				<app.main.class>«mainClass»</app.main.class>
				<!-- Use the latest version whenever possible. -->
				<jackson.version>2.5.0</jackson.version>
				<californium.version>1.0.1</californium.version>
				<shade.version>2.4.3</shade.version>
			</properties>
		
			<dependencies>
				<dependency>
					<groupId>org.eclipse.californium</groupId>
					<artifactId>californium-core</artifactId>
					<version>${californium.version}</version>
				</dependency>
		
				<dependency>
					<groupId>com.fasterxml.jackson.core</groupId>
					<artifactId>jackson-databind</artifactId>
					<version>${jackson.version}</version>
				</dependency>
			</dependencies>
		</project>
		'''
	}
	override getFileName(InformationModel context) {
		return projectName + "/pom.xml"
	}
	
	override getPath(InformationModel context) {
		return null;
	}

}
