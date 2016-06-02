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
package org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.template

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.BaseDriverUtil
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class PomTemplate implements IFileTemplate<FunctionblockModel> {
	
	override getContent(FunctionblockModel model,InvocationContext context) {
		'''<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.bosch.functionblock</groupId>
 	<artifactId>«model.namespace».dummy-basedriver</artifactId>
 	<version>1.0.0</version>
	<name>Dummy Base Driver</name>
	<packaging>bundle</packaging> 
  
	<dependencies>
		<dependency>
			<groupId>org.osgi</groupId>
			<artifactId>org.osgi.core</artifactId>
			<version>4.3.1</version>
		</dependency>
		<dependency>
			<groupId>org.osgi</groupId>
			<artifactId>org.osgi.compendium</artifactId>
			<version>4.3.1</version>
		</dependency>
		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-api</artifactId>
			<version>1.7.1</version>
		</dependency>
	</dependencies>

	<build>
		<finalName>com.bosch.functionblock.dummy.basedriver-${version}</finalName>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<source>1.7</source>
					<target>1.7</target>
					<encoding>UTF-8</encoding>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.felix</groupId>
				<artifactId>maven-bundle-plugin</artifactId>
				<version>2.3.7</version>
				<extensions>true</extensions>
				<executions>
					<execution>
						<id>bundle-manifest</id>
						<phase>process-classes</phase>
						<goals>
							<goal>manifest</goal>
						</goals>
					</execution>
				</executions>
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
										<groupId>org.apache.felix</groupId>
										<artifactId>
											maven-bundle-plugin
										</artifactId>
										<versionRange>
											[2.3.7,)
										</versionRange>
										<goals>
											<goal>manifest</goal>
										</goals>
									</pluginExecutionFilter>
									<action>
										<execute />
									</action>
								</pluginExecution>
							</pluginExecutions>
						</lifecycleMappingMetadata>
					</configuration>
				</plugin>
			</plugins>
		</pluginManagement>
	</build>	
</project>
'''
	}
	
	override getFileName(FunctionblockModel context) {
		return "pom.xml"
	}
	
	override getPath(FunctionblockModel context) {
		return new BaseDriverUtil(context).moduleRootDirectory;
	}
	
}
