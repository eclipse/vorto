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
 package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates

import org.eclipse.vorto.codegen.api.tasks.ITemplate
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class PomFileTemplate implements ITemplate<InformationModel> {
	
	override getContent(InformationModel model) {
		'''<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
		  <modelVersion>4.0.0</modelVersion>
		  <groupId>org.eclipse.vorto.examples</groupId>
		  <artifactId>«ModuleUtil.getArtifactId(model)»</artifactId>
		  <version>«model.version»</version>
		  <name>Sample project generated from «model.name»</name>
		  <packaging>war</packaging> 
		  <dependencies>
				<dependency>
					<groupId>junit</groupId>
					<artifactId>junit</artifactId>
					<version>4.12</version>
					<scope>test</scope>
				</dependency>	
				<dependency>
					<groupId>com.sun.jersey</groupId>
					<artifactId>jersey-server</artifactId>
					<version>1.11</version>
				</dependency>
				<dependency>
					<groupId>com.sun.jersey</groupId>
					<artifactId>jersey-servlet</artifactId>
					<version>1.11</version>
				</dependency>
				<dependency>
					<groupId>com.sun.jersey</groupId>
					<artifactId>jersey-json</artifactId>
					<version>1.11</version>
				</dependency>	
				<dependency>
					<groupId>commons-beanutils</groupId>
					<artifactId>commons-beanutils</artifactId>
					<version>1.8.0</version>
				</dependency>								
		  </dependencies>

		  <build>
				<finalName>«ModuleUtil.getArtifactId(model)»</finalName>
				<plugins>
					<plugin>
						<artifactId>maven-compiler-plugin</artifactId>
						<configuration>
							<source>1.7</source>
							<target>1.7</target>
						</configuration>
					</plugin>
					<plugin>
						<groupId>org.eclipse.jetty</groupId>
						<artifactId>jetty-maven-plugin</artifactId>
						<version>9.2.3.v20140905</version>
						<configuration>
							<scanIntervalSeconds>10</scanIntervalSeconds>
							<webApp>
								<contextPath>/«ModuleUtil.getArtifactId(model)»</contextPath>
							</webApp>
							<stopPort>9966</stopPort>
							<stopKey>jettystop</stopKey>
							<stopWait>10</stopWait>
						</configuration>
					</plugin>
				</plugins>
		  </build>
		</project>'''
	}

}
