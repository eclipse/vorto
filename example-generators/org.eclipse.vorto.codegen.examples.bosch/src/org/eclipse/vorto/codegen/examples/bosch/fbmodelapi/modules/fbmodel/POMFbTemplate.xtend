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
package org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.modules.fbmodel

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class POMFbTemplate implements IFileTemplate<FunctionblockModel> {
	
	override getContent(FunctionblockModel context, InvocationContext invocationContext) {
		var wrappedfbm= new FbModelWrapper(context)
		'''
<project xmlns="http://maven.apache.org/POM/4.0.0" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	   
	  <modelVersion>4.0.0</modelVersion>
	  <groupId>« wrappedfbm.getGroupName() »</groupId>
	  <artifactId>com.bosch.« wrappedfbm.functionBlockName.toLowerCase »-model</artifactId>
	  <version>« context.version »</version>
	  <name>« context.name »</name>
	  <description>« context.description »</description>
	  <packaging>bundle</packaging>
	  
	  <parent>
	      <groupId>com.bosch.msp</groupId>
	      <artifactId>msp-functionblock-parent</artifactId>
	      <version>3.0.7</version>
	  </parent>
	  
	 <properties>
      <fbName>« context.name »</fbName>
      <package>« wrappedfbm.getJavaPackageName() »</package>
      <enableWrapperStyle>false</enableWrapperStyle>
      <folder.generated>${basedir}/src-gen</folder.generated>
     </properties>
     
	<build>
	  <finalName>${package}-model-${version}</finalName>
      <plugins>
         <plugin>
            <artifactId>maven-dependency-plugin</artifactId>
         </plugin>
         <plugin>
            <groupId>org.codehaus.groovy.maven</groupId>
            <artifactId>gmaven-plugin</artifactId>
         </plugin>
         <plugin>
            <artifactId>maven-resources-plugin</artifactId>
               <executions>
	                <execution>
	                   <id>copy-xsd</id>
	                   <phase>none</phase>
	                </execution>
					<execution>
	                   <id>copy-xml</id>
	                   <phase>none</phase>
	                </execution>
					<execution>
	                   <id>copy-jaxb-binding</id>
	                   <phase>none</phase>
	                </execution>
                  <execution>
                     <id>my-copy-xsd</id>
                     <phase>generate-sources</phase>
                     <goals>
                        <goal>copy-resources</goal>
                     </goals>
                     <configuration>
                        <resources>
                           <resource>
                              <directory>${folder.generated}</directory>
                              <filtering>true</filtering>
                              <includes>
                                 <include>*.xsd</include>
                              </includes>
                           </resource>
                        </resources>
                        <outputDirectory>${dp.tmpDirectory}</outputDirectory>
                     </configuration>
                  </execution>
                  <execution>
                     <id>my-copy-xml</id>
                     <phase>generate-sources</phase>
                     <goals>
                        <goal>copy-resources</goal>
                     </goals>
                     <configuration>
                        <resources>
                           <resource>
                              <directory>${folder.generated}</directory>
                              <filtering>true</filtering>
                              <includes>
                                 <include>*.xml</include>
                              </includes>
                              <excludes>
                                 <exclude>pom.xml</exclude>
                              </excludes>
                           </resource>
                        </resources>
                        <outputDirectory>${dp.outputDirectory}</outputDirectory>
                     </configuration>
                  </execution>
                  <execution>
                     <id>my-copy-jaxb-binding</id>
                     <phase>generate-sources</phase>
                     <goals>
                        <goal>copy-resources</goal>
                     </goals>
                     <configuration>
                        <resources>
                           <resource>
                              <directory>${folder.generated}</directory>
                              <filtering>true</filtering>
                              <includes>
                                 <include>*.xjb</include>
                              </includes>
                           </resource>
                        </resources>
                        <outputDirectory>${dp.bindingDirectory}</outputDirectory>
                     </configuration>
                  </execution>
             </executions>
         </plugin>
         <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>xml-maven-plugin</artifactId>
         </plugin>
         <plugin>
            <groupId>org.apache.cxf</groupId>
            <artifactId>cxf-codegen-plugin</artifactId>
         </plugin>
         <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>build-helper-maven-plugin</artifactId>
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
      								<groupId>
      									org.codehaus.groovy.maven
      								</groupId>
      								<artifactId>
      									gmaven-plugin
      								</artifactId>
      								<versionRange>
      									[1.0,)
      								</versionRange>
      								<goals>
      									<goal>execute</goal>
      								</goals>
      							</pluginExecutionFilter>
      							<action>
      								<execute/>
      							</action>
      						</pluginExecution>
      						<pluginExecution>
      							<pluginExecutionFilter>
      								<groupId>
      									org.codehaus.mojo
      								</groupId>
      								<artifactId>
      									build-helper-maven-plugin
      								</artifactId>
      								<versionRange>
      									[1.7,)
      								</versionRange>
      								<goals>
      									<goal>add-resource</goal>
      									<goal>parse-version</goal>
      									<goal>add-source</goal>
      								</goals>
      							</pluginExecutionFilter>
      							<action>
      								<execute/>
      							</action>
      						</pluginExecution>
      						<pluginExecution>
      							<pluginExecutionFilter>
      								<groupId>
      									org.apache.maven.plugins
      								</groupId>
      								<artifactId>
      									maven-dependency-plugin
      								</artifactId>
      								<versionRange>
      									[2.4,)
      								</versionRange>
      								<goals>
      									<goal>unpack</goal>
      								</goals>
      							</pluginExecutionFilter>
      							<action>
      								<execute/>
      							</action>
      						</pluginExecution>
      						<pluginExecution>
      							<pluginExecutionFilter>
      								<groupId>
      									org.codehaus.mojo
      								</groupId>
      								<artifactId>
      									xml-maven-plugin
      								</artifactId>
      								<versionRange>
      									[1.0,)
      								</versionRange>
      								<goals>
      									<goal>transform</goal>
      									<goal>validate</goal>
      								</goals>
      							</pluginExecutionFilter>
      							<action>
      								<execute/>
      							</action>
      						</pluginExecution>
      						<pluginExecution>
      							<pluginExecutionFilter>
      								<groupId>
      									org.apache.felix
      								</groupId>
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
	
	override getFileName(FunctionblockModel context) {
		return "pom.xml"
	}
	
	override getPath(FunctionblockModel context) {
		return '''com.bosch.« new FbModelWrapper(context).functionBlockName.toLowerCase »-model'''
	}
	
}
