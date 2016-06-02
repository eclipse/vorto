package org.eclipse.vorto.codegen.ui.wizard.generation.templates.server

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext

class MicroServicePomTemplate implements IFileTemplate<IGeneratorProjectContext> {
	
	override getFileName(IGeneratorProjectContext context) {
		return "pom.xml"
	}
	
	override getPath(IGeneratorProjectContext context) {
		return context.packageFolders+"/service"
	}
	
	override getContent(IGeneratorProjectContext context,InvocationContext invocationContext) {
		'''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<modelVersion>4.0.0</modelVersion>
			<parent>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-parent</artifactId>
				<version>1.3.0.RELEASE</version>
			</parent>
			<groupId>«context.packageName»</groupId>
			<artifactId>«context.packageName».«context.generatorName.toLowerCase».service</artifactId>
			<version>1.0.0-SNAPSHOT</version>
			<packaging>jar</packaging>
		
			<properties>
				<vorto.version>0.4.0-SNAPSHOT</vorto.version>
			</properties>
			
			<dependencies>
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter</artifactId>
					<exclusions>
						<exclusion>
							<groupId>org.springframework.boot</groupId>
							<artifactId>spring-boot-starter-logging</artifactId>
						</exclusion>
					</exclusions>
				</dependency>
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-web</artifactId>
					<exclusions>
						<exclusion>
							<groupId>org.slf4j</groupId>
							<artifactId>log4j-over-slf4j</artifactId>
						</exclusion>
					</exclusions>
				</dependency>
				<dependency>
					<groupId>«context.packageName»</groupId>
					<artifactId>«context.packageName».«context.generatorName.toLowerCase»</artifactId>
					<version>${project.version}</version>
				</dependency>
				<dependency>
					<groupId>org.eclipse.vorto</groupId>
					<artifactId>org.eclipse.vorto.codegen.service</artifactId>
					<version>${vorto.version}</version>
				</dependency>		
				<dependency>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-tomcat</artifactId>
					<scope>provided</scope>
				</dependency>
			</dependencies>
			<build>
				<finalName>vorto-«context.generatorName.toLowerCase»</finalName>
				<plugins>
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
									<mainClass>«context.packageName».«context.generatorName.toLowerCase».service.«context.generatorName»GeneratorMicroService</mainClass>
								</manifest>
							</archive>
						</configuration>
					</plugin>
				</plugins>
			</build>
		</project>
		'''
	}
	
}
