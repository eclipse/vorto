/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.codegen.webui.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class PomTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''pom.xml'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		<?xml version="1.0" encoding="UTF-8"?>
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
		
			<groupId>com.example.iot.«element.name.toLowerCase»</groupId>
			<artifactId>«element.name.toLowerCase»-solution</artifactId>
			<version>0.0.1-SNAPSHOT</version>
			<packaging>jar</packaging>
		
			<name>«element.name» Solution</name>
			<description>Generated dashboard application for «element.name» devices</description>
		
			<parent>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-parent</artifactId>
				<version>1.4.6.RELEASE</version>
			</parent>
			
			<repositories>
				<repository>
					<id>bosch-releases</id>
					<name>bosch-releases</name>
					<url>https://maven.bosch-si.com/content/repositories/bosch-releases/</url>
				</repository>
			</repositories>
		
			<properties>
				<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
				<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
				<java.version>1.8</java.version>
				<vorto.version>0.10.0.M1</vorto.version>
			</properties>
		
			<dependencies>
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-security</artifactId>
				</dependency>
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-web</artifactId>
				</dependency>
				
				«IF context.configurationProperties.getOrDefault("history","true").equalsIgnoreCase("true")»
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-data-jpa</artifactId>
				</dependency>
				<dependency>
					<groupId>com.h2database</groupId>
					<artifactId>h2</artifactId>
				</dependency>
				«ENDIF»
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-websocket</artifactId>
				</dependency>
				<dependency>
					<groupId>org.springframework.security.oauth</groupId>
					<artifactId>spring-security-oauth2</artifactId>
				</dependency>
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-test</artifactId>
					<scope>test</scope>
				</dependency>
				<dependency>
					<groupId>org.eclipse.vorto</groupId>
					<artifactId>repository-java-client</artifactId>
					<version>${vorto.version}</version>
				</dependency>
				«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
				<dependency>
					<groupId>com.bosch.cr</groupId>
					<artifactId>cr-integration-client</artifactId>
					<version>2.4.1</version>
				</dependency>
				<dependency>
					<groupId>com.bosch.im</groupId>
					<artifactId>im-api2-client</artifactId>
					<version>1.0-beta2</version>
				</dependency>
				«ENDIF»
				<dependency>
					<groupId>org.webjars</groupId>
					<artifactId>webjars-locator</artifactId>
				</dependency>
				<dependency>
					<groupId>org.webjars</groupId>
					<artifactId>jquery</artifactId>
					<version>2.1.1</version>
				</dependency>
				<dependency>
					<groupId>org.webjars</groupId>
					<artifactId>angularjs</artifactId>
					<version>1.5.9</version>
				</dependency>
				<dependency>
					<groupId>org.webjars.bower</groupId>
					<artifactId>angular-ui-router</artifactId>
					<version>0.3.2</version>
				</dependency>
				<dependency>
					<groupId>org.webjars</groupId>
					<artifactId>angular-ui-bootstrap</artifactId>
					<version>0.14.3</version>
				</dependency>
				<dependency>
					<groupId>org.webjars</groupId>
					<artifactId>angular-leaflet-directive</artifactId>
					<version>0.8.2</version>
				</dependency>
				<dependency>
					<groupId>org.webjars</groupId>
					<artifactId>leaflet</artifactId>
					<version>1.0.0</version>
				</dependency>
				<dependency>
					<groupId>org.webjars</groupId>
					<artifactId>angular-nvd3</artifactId>
					<version>1.0.5</version>
				</dependency>
				<dependency>
					<groupId>org.webjars</groupId>
					<artifactId>justgage</artifactId>
					<version>1.1.0</version>
				</dependency>
				<dependency>
					<groupId>org.webjars</groupId>
					<artifactId>sockjs-client</artifactId>
					<version>0.3.4</version>
				</dependency>
				<dependency>
					<groupId>org.webjars</groupId>
					<artifactId>stomp-websocket</artifactId>
					<version>2.3.3</version>
				</dependency>
				
				<dependency>
					<groupId>org.webjars.bower</groupId>
					<artifactId>adminlte</artifactId>
					<version>2.3.11</version>
				</dependency>
				<dependency>
					<groupId>org.webjars</groupId>
					<artifactId>font-awesome</artifactId>
					<version>4.2.0</version>
				</dependency>
				«IF context.configurationProperties.getOrDefault("swagger","true").equalsIgnoreCase("true")»
				<!-- Swagger -->
				<dependency>
					<groupId>io.swagger</groupId>
					<artifactId>swagger-annotations</artifactId>
					<version>1.5.6</version>
				</dependency>
				<dependency>
					<groupId>io.swagger</groupId>
					<artifactId>swagger-models</artifactId>
					<version>1.5.6</version>
				</dependency>
				
				<dependency>
					<groupId>io.springfox</groupId>
					<artifactId>springfox-swagger2</artifactId>
					<version>2.3.0</version>
					<exclusions>
						<exclusion>
							<groupId>io.swagger</groupId>
							<artifactId>swagger-annotations</artifactId>
						</exclusion>
						<exclusion>
							<groupId>io.swagger</groupId>
							<artifactId>swagger-models</artifactId>
						</exclusion>
					</exclusions>
				</dependency>
				
				<dependency>
					<groupId>org.webjars.bower</groupId>
					<artifactId>angular-swagger-ui</artifactId>
					<version>0.2.7</version>
					<exclusions>
						<exclusion>
							<groupId>org.webjars.bower</groupId>
							<artifactId>jquery</artifactId>
						</exclusion>
						<exclusion>
							<groupId>org.webjars.bower</groupId>
							<artifactId>bootstrap-less-only</artifactId>
						</exclusion>
						<exclusion>
							<groupId>org.webjars.bower</groupId>
							<artifactId>angular</artifactId>
						</exclusion>
					</exclusions>
				</dependency>
				«ENDIF»
			</dependencies>
		
			<build>
				<plugins>
					<plugin>
						<groupId>org.springframework.boot</groupId>
						<artifactId>spring-boot-maven-plugin</artifactId>
					</plugin>
				</plugins>
				<pluginManagement>
					<plugins>
						<!--This plugin's configuration is used to store Eclipse m2e settings 
							only. It has no influence on the Maven build itself. -->
						<plugin>
							<groupId>org.eclipse.m2e</groupId>
							<artifactId>lifecycle-mapping</artifactId>
							<version>1.0.0</version>
							<configuration>
								<lifecycleMappingMetadata>
									<pluginExecutions>
										<pluginExecution>
											<pluginExecutionFilter>
												<groupId>
													org.apache.maven.plugins
												</groupId>
												<artifactId>
													maven-antrun-plugin
												</artifactId>
												<versionRange>
													[1.7,)
												</versionRange>
												<goals>
													<goal>run</goal>
												</goals>
											</pluginExecutionFilter>
											<action>
												<ignore></ignore>
											</action>
										</pluginExecution>
									</pluginExecutions>
								</lifecycleMappingMetadata>
							</configuration>
						</plugin>
						<plugin>
							<groupId>org.springframework.boot</groupId>
							<artifactId>spring-boot-maven-plugin</artifactId>
							<executions>
								<execution>
									<goals>
										<goal>repackage</goal>
									</goals>
								</execution>
							</executions>
						</plugin>
						<plugin>
							<groupId>org.apache.maven.plugins</groupId>
							<artifactId>maven-jar-plugin</artifactId>
							<configuration>
								<archive>
									<manifest>
										<mainClass>com.example.iot.«element.name.toLowerCase».«element.name»Application</mainClass>
									</manifest>
								</archive>
							</configuration>
						</plugin>
					</plugins>
				</pluginManagement>
			</build>
		
		
		</project>
		'''
	}
	
}
