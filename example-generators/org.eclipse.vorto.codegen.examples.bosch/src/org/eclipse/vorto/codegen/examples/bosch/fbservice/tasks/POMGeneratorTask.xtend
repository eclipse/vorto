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
package org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks

import org.eclipse.vorto.codegen.api.Generated
import org.eclipse.vorto.codegen.api.IGeneratedWriter
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class POMGeneratorTask extends AbstractGeneratorTask<FunctionblockModel> {
	protected String targetM2MVersion;
	
	new(FbModelWrapper model,String targetM2MVersion) {
		super(model)
		if(isEmptyVersion(targetM2MVersion)){
			this.targetM2MVersion = "LATEST";
		}else{
			this.targetM2MVersion = targetM2MVersion;
		}
	}
	
	override generate(FunctionblockModel model, InvocationContext context, IGeneratedWriter outputter) {
		var sqn = fetchGeneratedPOMString()
		outputter.write(new Generated("pom.xml",baseDirectoryFolder,sqn))
	}
	
	def String getBaseDirectoryFolder() {
		return '''com.bosch.« context.model.name.toLowerCase»-service'''
	}
	
    private def boolean isEmptyVersion(String version){
		if(version==null || version.trim()==""){
			return true;
		}
		return false;
	}
	
	private def String fetchGeneratedPOMString() {
		'''
		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
		  <modelVersion>4.0.0</modelVersion>
		  <groupId>com.bosch.functionblock.impl</groupId>
		  <artifactId>«context.javaPackageName».impl</artifactId>
		  <version>«context.model.version»</version>
		  <name>Driver for function block «context.functionBlockName»</name>
		  <packaging>bundle</packaging> 
		  
		<properties>
		      <msp.osgi.import.pkg>*,«context.javaPackageName»._«context.majorVersion»</msp.osgi.import.pkg>
		      <m2m.version>«targetM2MVersion»</m2m.version>
		</properties>
		<dependencies>
		      <dependency>
		      <groupId>com.bosch.ism</groupId>
		      <artifactId>ism-api</artifactId>
		      <version>${m2m.version}</version>
		   </dependency>
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
		      <groupId>«context.getGroupName»</groupId>
		      <artifactId>com.bosch.«context.functionBlockName.toLowerCase »-model</artifactId>
		      <version>«context.model.version»</version>
		   </dependency>
		   <dependency>
		      	<groupId>com.bosch.functionblock</groupId>
		      	<artifactId>«context.model.namespace».dummy-basedriver</artifactId>
		      	<version>1.0.0</version>
		   </dependency>
		   <dependency>
		      <groupId>org.slf4j</groupId>
		      <artifactId>slf4j-api</artifactId>
		      <version>1.7.1</version>
		   </dependency>
		</dependencies>
		<build>
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
		            <configuration>
		                <instructions>
		                    <Import-Package>${msp.osgi.import.pkg}</Import-Package>
		                </instructions>
		            </configuration>
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
		<!--This plugin's configuration is used to store Eclipse m2e settings only. It has no influence on the Maven build itself.-->
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
									<execute/>
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
	

}
