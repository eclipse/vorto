/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.codegen.template.plugin

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class PomTemplate implements IFileTemplate<InformationModel> {
	
	private String serviceKey;
	
	new (String serviceKey) {
		this.serviceKey = serviceKey;
	}
	
	override getFileName(InformationModel context) {
		'pom.xml'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-'+serviceKey.toLowerCase
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
			<parent>
				<groupId>com.mycompany</groupId>
				<artifactId>parent</artifactId>
				<version>0.0.1-SNAPSHOT</version>
			</parent>
		
			<artifactId>generator-«serviceKey.toLowerCase»</artifactId>
		
			<name>«serviceKey.toFirstUpper» Generator</name>
		
			<dependencies>
				<dependency>
					<groupId>org.eclipse.vorto</groupId>
					<artifactId>org.eclipse.vorto.core</artifactId>
					<version>${vorto.version}</version>
				</dependency>
				
				<dependency>
					<groupId>org.eclipse.vorto</groupId>
					<artifactId>generator-api</artifactId>
					<version>${vorto.version}</version>
				</dependency>
				<dependency>
					<groupId>org.eclipse.xtend</groupId>
					<artifactId>org.eclipse.xtend.lib</artifactId>
					<version>${xtext.version}</version>
				</dependency>
			</dependencies>
			
			<build>
				<plugins>
					<plugin>
						<groupId>org.eclipse.xtend</groupId>
						<artifactId>xtend-maven-plugin</artifactId>
					</plugin>
				</plugins>
			</build>
		</project>
		'''
	}
	
}
