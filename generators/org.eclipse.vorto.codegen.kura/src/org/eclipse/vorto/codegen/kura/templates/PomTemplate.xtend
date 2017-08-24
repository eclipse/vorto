package org.eclipse.vorto.codegen.kura.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class PomTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		"pom.xml"
	}
	
	override getPath(InformationModel context) {
		'''«Utils.basePath»'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.example.kura</groupId>
	<artifactId>com.example.kura.«element.name.toLowerCase»</artifactId>
	<version>1.0.0-SNAPSHOT</version>
	<packaging>jar</packaging>
	
	<name>Kura Bundle for «element.name»</name>
	<description>Kura Bundle for «element.name»</description>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
	</properties>
	
	<repositories>
		<repository>
			<id>bosch-releases</id>
			<name>bosch-releases</name>
			<url>https://maven.bosch-si.com/content/repositories/bosch-releases/</url>
		</repository>
	</repositories>

	<dependencies>
		
		<dependency>
			<groupId>com.bosch.cr</groupId>
			<artifactId>cr-integration-api</artifactId>
			<version>3.3.0</version>
		</dependency>
		
		<dependency>
			<groupId>com.bosch.cr</groupId>
			<artifactId>cr-integration-client</artifactId>
			<version>2.4.1</version>
		</dependency>
		
		<dependency>
			<groupId>com.bosch.cr</groupId>
			<artifactId>cr-integration-client-osgi</artifactId>
			<version>2.4.1</version>
		</dependency>
		
		<dependency>
			<groupId>com.bosch.cr</groupId>
			<artifactId>cr-json</artifactId>
			<version>1.6.0</version>
		</dependency>
		
	</dependencies>

	<build>
		<plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-dependency-plugin</artifactId>
        <version>3.0.1</version>
        <configuration>
        	<outputDirectory>${basedir}/lib</outputDirectory>
        	<overWriteReleases>false</overWriteReleases>
          <overWriteSnapshots>false</overWriteSnapshots>
          <overWriteIfNewer>true</overWriteIfNewer>
        </configuration>
      </plugin>
    </plugins>
	</build>

</project>
		'''
	}
	
}