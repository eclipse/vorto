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
package org.eclipse.vorto.codegen.template.parent

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class PomTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'pom.xml'
	}
	
	override getPath(InformationModel context) {
		'generator-parent'
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		<?xml version="1.0" encoding="UTF-8"?>
		<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xmlns="http://maven.apache.org/POM/4.0.0"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
			<modelVersion>4.0.0</modelVersion>
		
			<groupId>com.mycompany</groupId>
			<artifactId>parent</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		
			<name>Vorto Generator Parent</name>
		
			<packaging>pom</packaging>
		
			<properties>
				<xtext.version>2.9.0</xtext.version>
				<vorto.version>0.10.0.M8</vorto.version>
				<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
				<maven.compiler.source>1.8</maven.compiler.source>
				<maven.compiler.target>1.8</maven.compiler.target>
				<spring.boot.version>1.5.12.RELEASE</spring.boot.version>
			</properties>
		
			<modules>
				<module>generator-«context.configurationProperties.getOrDefault("servicekey","myplatform").replaceAll(" ","").toLowerCase»</module>
				<module>generator-runner</module>
			</modules>
			
			<build>
				<pluginManagement>
					<plugins>
						<plugin>
							<groupId>org.codehaus.mojo</groupId>
							<artifactId>build-helper-maven-plugin</artifactId>
							<version>1.7</version>
							<executions>
								<execution>
									<id>add-source</id>
									<phase>generate-sources</phase>
									<goals>
										<goal>add-source</goal>
									</goals>
									<configuration>
										<sources>
											<source>${basedir}/src/main/xtend-gen</source>
										</sources>
									</configuration>
								</execution>
							</executions>
						</plugin>
						<plugin>
							<groupId>org.eclipse.xtend</groupId>
							<artifactId>xtend-maven-plugin</artifactId>
							<version>${xtext.version}</version>
							<executions>
								<execution>
									<goals>
										<goal>compile</goal>
									</goals>
									<configuration>
										<outputDirectory>${basedir}/src/main/xtend-gen</outputDirectory>
									</configuration>
								</execution>
							</executions>
						</plugin>
						<plugin>
							<artifactId>maven-clean-plugin</artifactId>
							<version>2.4.1</version>
							<configuration>
								<filesets>
									<fileset>
										<directory>${basedir}/src/main/xtend-gen</directory>
									</fileset>
								</filesets>
							</configuration>
						</plugin>
						<plugin>
							<artifactId>maven-war-plugin</artifactId>
							<configuration>
								<failOnMissingWebXml>false</failOnMissingWebXml>
							</configuration>
						</plugin>
					</plugins>
				</pluginManagement>
				<plugins>
					<plugin>
						<groupId>org.eclipse.xtend</groupId>
						<artifactId>xtend-maven-plugin</artifactId>
						<dependencies>
							<dependency>
								<groupId>org.eclipse.platform</groupId>
								<artifactId>org.eclipse.equinox.common</artifactId>
								<version>3.10.0</version>
							</dependency>
						</dependencies>
					</plugin>
				</plugins>
			</build>
			
		
			<profiles>
				<profile>
					<id>doclint-java8-disable</id>
					<activation>
						<jdk>[1.8,)</jdk>
					</activation>
					<properties>
						<javadoc.opts>-Xdoclint:none</javadoc.opts>
					</properties>
				</profile>
			</profiles>
		</project>
		'''
	}
	
}