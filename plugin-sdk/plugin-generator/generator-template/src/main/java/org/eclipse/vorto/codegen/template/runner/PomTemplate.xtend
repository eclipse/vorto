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
package org.eclipse.vorto.codegen.template.runner

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class PomTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'pom.xml'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-runner'
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		<?xml version="1.0" encoding="UTF-8"?>
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
			<parent>
				<groupId>com.mycompany</groupId>
				<artifactId>parent</artifactId>
				<version>0.0.1-SNAPSHOT</version>
			</parent>
		
			<artifactId>generator-runner</artifactId>
		
			<name>Code Generator Runner</name>
			<description>Runs all the code generators as an individual (micro) service</description>
		
			<dependencies>
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-web</artifactId>
					<exclusions>
						<exclusion>
							<groupId>org.slf4j</groupId>
							<artifactId>log4j-over-slf4j</artifactId>
						</exclusion>
						<exclusion>
							<groupId>ch.qos.logback</groupId>
							<artifactId>logback-classic</artifactId>
						</exclusion>
					</exclusions>
				</dependency>
		
				<dependency>
					<groupId>org.apache.httpcomponents</groupId>
					<artifactId>httpclient</artifactId>
				</dependency>
		
				<!-- Vorto Dependencies -->
				<dependency>
					<groupId>org.eclipse.vorto</groupId>
					<artifactId>generator-runner-spi</artifactId>
					<version>${vorto.version}</version>
				</dependency>
		
				<dependency>
					<groupId>com.mycompany</groupId>
					<artifactId>generator-«context.configurationProperties.getOrDefault("servicekey","myplatform").replaceAll(" ","").toLowerCase»</artifactId>
					<version>${project.version}</version>
				</dependency>
		
			</dependencies>
		
			<dependencyManagement>
				<dependencies>
					<dependency>
						<groupId>org.springframework.boot</groupId>
						<artifactId>spring-boot-dependencies</artifactId>
						<version>${spring.boot.version}</version>
						<type>pom</type>
						<scope>import</scope>
					</dependency>
				</dependencies>
			</dependencyManagement>
			<build>
				<plugins>
					<plugin>
						<groupId>org.springframework.boot</groupId>
						<artifactId>spring-boot-maven-plugin</artifactId>
		                <version>${spring.boot.version}</version>
						<executions>
							<execution>
								<goals>
									<goal>repackage</goal>
								</goals>
								 <configuration>
									<classifier>exec</classifier>
								 </configuration>
							</execution>
						</executions>
					</plugin>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-jar-plugin</artifactId>
						<version>2.4</version>
						<configuration>
							<archive>
								<manifest>
									<mainClass>com.mycompany.runner.GeneratorRunner</mainClass>
								</manifest>
							</archive>
						</configuration>
					</plugin>
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