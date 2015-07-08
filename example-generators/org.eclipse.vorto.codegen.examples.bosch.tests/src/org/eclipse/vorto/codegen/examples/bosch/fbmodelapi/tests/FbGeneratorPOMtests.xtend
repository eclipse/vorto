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
package org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.tests;

import com.google.inject.Inject
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl
import org.eclipse.vorto.editor.functionblock.FunctionblockInjectorProvider
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.InMemoryFileSystemAccess
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(FunctionblockInjectorProvider))
class FbGeneratorPOMTest {

	IGenerator underTest = new GeneratorAdapter();
	@Inject ParseHelper<FunctionblockModel> parseHelper

	@BeforeClass
	def static void initializeModel() {
		FunctionblockPackageImpl.init();
	}

	@Test
	def void testfuncBlock_NameChange() {
		val fsa = new InMemoryFileSystemAccess()

		fsa.generateFile("pom.xml", IFileSystemAccess::DEFAULT_OUTPUT,
			'''
				<project xmlns="http://maven.apache.org/POM/4.0.0" 
					xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
					xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
					  
					  <modelVersion>4.0.0</modelVersion>
					  <groupId>com.bosch.functionblock.demo</groupId>
					  <artifactId>com.bosch.fridge</artifactId>
					  <version>1.0.0</version>
					  <name>Fridge</name>
					  <description>comment</description>
					  <packaging>bundle</packaging>
					  
					  <parent>
					      <groupId>com.bosch.msp</groupId>
					      <artifactId>msp-functionblock-parent</artifactId>
					      <version>3.0.6</version>
					  </parent>
					  
					 <properties>
					     <fbName>Fridge</fbName>
					     <package>com.bosch.functionblock.demo.fridge</package>
					     <enableWrapperStyle>false</enableWrapperStyle>
					     <folder.generated>${basedir}/src-gen</folder.generated>
					    </properties>
					    
					<build>
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
					  </build>     
				</project>
			      ''')

		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				functionblock Fridge{
					displayname 'Refrigerator'
					description 'comment'
					category demo					
					configuration{
						optional temperature as int
					}
				}
			''')
		underTest.doGenerate(model.eResource, fsa)

		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml"))
		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd"))
		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "jaxb-bindings.xjb"))

		val model2 = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0			
				functionblock NewFridge{
					displayname 'Refrigerator'
					description 'comment'
					category demo
					configuration{
						optional temperature as int
					}
				}
			''')

		underTest.doGenerate(model2.eResource, fsa)

		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "NewFridge.xml"))
		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "NewFridge.xsd"))
		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "jaxb-bindings.xjb"))

		val expected = '''
<project xmlns="http://maven.apache.org/POM/4.0.0" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	   
	  <modelVersion>4.0.0</modelVersion>
	  <groupId>com.bosch.functionblock.demo</groupId>
	  <artifactId>com.bosch.newfridge-model</artifactId>
	  <version>1.0.0</version>
	  <name>NewFridge</name>
	  <description>comment</description>
	  <packaging>bundle</packaging>
	  
	  <parent>
	      <groupId>com.bosch.msp</groupId>
	      <artifactId>msp-functionblock-parent</artifactId>
	      <version>3.0.7</version>
	  </parent>
	  
	 <properties>
      <fbName>NewFridge</fbName>
      <package>com.bosch.functionblock.demo.newfridge</package>
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
		assertEquals(expected, fsa.readTextFile("pom.xml", IFileSystemAccess::DEFAULT_OUTPUT).toString)

	}

	@Test
	def void testfuncBlock_GroupId() {
		val fsa = new InMemoryFileSystemAccess()

		fsa.generateFile("pom.xml", IFileSystemAccess::DEFAULT_OUTPUT,
			'''
				<project xmlns="http://maven.apache.org/POM/4.0.0" 
					xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
					xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
					  
					  <modelVersion>4.0.0</modelVersion>
					  <groupId>com.bosch.functionblock.demo</groupId>
					  <artifactId>com.bosch.fridge</artifactId>
					  <version>1.0.0</version>
					  <name>Fridge</name>
					  <description>comment</description>
					  <packaging>bundle</packaging>
					  
					  <parent>
					      <groupId>com.bosch.msp</groupId>
					      <artifactId>msp-functionblock-parent</artifactId>
					      <version>3.0.6</version>
					  </parent>
					  
					 <properties>
					     <fbName>Fridge</fbName>
					     <package>com.bosch.functionblock.demo.fridge</package>
					     <enableWrapperStyle>false</enableWrapperStyle>
					     <folder.generated>${basedir}/src-gen</folder.generated>
					    </properties>
					    
					<build>
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
					  </build>     
				</project>
			      ''')

		val model = parseHelper.parse(
			'''
				namespace com.redelephant
				version 1.0.0			
				functionblock Fridge{
					displayname 'Refrigerator'
					description 'comment'
					category demo/indigo
					configuration{
						optional temperature as int
					}
				}
			''')
		underTest.doGenerate(model.eResource, fsa)

		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml"))
		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd"))
		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "jaxb-bindings.xjb"))

		val expected = '''
<project xmlns="http://maven.apache.org/POM/4.0.0" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	   
	  <modelVersion>4.0.0</modelVersion>
	  <groupId>com.redelephant.functionblock.demo.indigo</groupId>
	  <artifactId>com.bosch.fridge-model</artifactId>
	  <version>1.0.0</version>
	  <name>Fridge</name>
	  <description>comment</description>
	  <packaging>bundle</packaging>
	  
	  <parent>
	      <groupId>com.bosch.msp</groupId>
	      <artifactId>msp-functionblock-parent</artifactId>
	      <version>3.0.7</version>
	  </parent>
	  
	 <properties>
      <fbName>Fridge</fbName>
      <package>com.redelephant.functionblock.demo.indigo.fridge</package>
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
		assertEquals(expected, fsa.readTextFile("pom.xml", IFileSystemAccess::DEFAULT_OUTPUT).toString)

	}

	@Test
	def void testfuncBlock_Version() {
		val fsa = new InMemoryFileSystemAccess()

		fsa.generateFile("pom.xml", IFileSystemAccess::DEFAULT_OUTPUT,
			'''
				<project xmlns="http://maven.apache.org/POM/4.0.0" 
					xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
					xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
					  
					  <modelVersion>4.0.0</modelVersion>
					  <groupId>com.bosch.functionblock.demo</groupId>
					  <artifactId>Fridge</artifactId>
					  <version>1.0.0</version>
					  <name>Fridge</name>
					  <description>comment</description>
					  <packaging>bundle</packaging>
					  
					  <parent>
					      <groupId>com.bosch.msp</groupId>
					      <artifactId>msp-functionblock-parent</artifactId>
					      <version>3.0.6</version>
					  </parent>
					  
					 <properties>
					     <fbName>Fridge</fbName>
					     <package>com.bosch.functionblock.demo.fridge</package>
					     <enableWrapperStyle>false</enableWrapperStyle>
					     <folder.generated>${basedir}/src-gen</folder.generated>
					    </properties>
					    
					<build>
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
					  </build>     
				</project>
			      ''')

		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.2.0			
				functionblock Fridge{
					displayname 'Refrigerator'
					description 'comment'
					category demo
					configuration{
						optional temperature as int
					}
				}
			''')
		underTest.doGenerate(model.eResource, fsa)

		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml"))
		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd"))
		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "jaxb-bindings.xjb"))

		val expected = '''
<project xmlns="http://maven.apache.org/POM/4.0.0" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	   
	  <modelVersion>4.0.0</modelVersion>
	  <groupId>com.bosch.functionblock.demo</groupId>
	  <artifactId>com.bosch.fridge-model</artifactId>
	  <version>1.2.0</version>
	  <name>Fridge</name>
	  <description>comment</description>
	  <packaging>bundle</packaging>
	  
	  <parent>
	      <groupId>com.bosch.msp</groupId>
	      <artifactId>msp-functionblock-parent</artifactId>
	      <version>3.0.7</version>
	  </parent>
	  
	 <properties>
      <fbName>Fridge</fbName>
      <package>com.bosch.functionblock.demo.fridge</package>
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
		assertEquals(expected, fsa.readTextFile("pom.xml", IFileSystemAccess::DEFAULT_OUTPUT).toString)

	}

	@Test
	def void testfuncBlock_VersionWithQualifier() {
		val fsa = new InMemoryFileSystemAccess()

		fsa.generateFile("pom.xml", IFileSystemAccess::DEFAULT_OUTPUT,
			'''
				<project xmlns="http://maven.apache.org/POM/4.0.0" 
					xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
					xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
					  
					  <modelVersion>4.0.0</modelVersion>
					  <groupId>com.bosch.functionblock.demo</groupId>
					  <artifactId>Fridge</artifactId>
					  <version>1.0.0</version>
					  <name>Fridge</name>
					  <description>comment</description>
					  <packaging>bundle</packaging>
					  
					  <parent>
					      <groupId>com.bosch.msp</groupId>
					      <artifactId>msp-functionblock-parent</artifactId>
					      <version>3.0.6</version>
					  </parent>
					  
					 <properties>
					     <fbName>Fridge</fbName>
					     <package>com.bosch.functionblock.demo.fridge</package>
					     <enableWrapperStyle>false</enableWrapperStyle>
					    </properties>
					    
					<build>
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
					  </build>     
				</project>
			      ''')

		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 11.2.11-RELEASE			
				functionblock Fridge{
					displayname 'Refrigerator'
					description 'comment'
					category demo
					configuration{
						optional temperature as int
					}
				}
			''')
		underTest.doGenerate(model.eResource, fsa)

		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml"))
		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd"))
		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "jaxb-bindings.xjb"))

		val expected = '''
<project xmlns="http://maven.apache.org/POM/4.0.0" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	   
	  <modelVersion>4.0.0</modelVersion>
	  <groupId>com.bosch.functionblock.demo</groupId>
	  <artifactId>com.bosch.fridge-model</artifactId>
	  <version>11.2.11-RELEASE</version>
	  <name>Fridge</name>
	  <description>comment</description>
	  <packaging>bundle</packaging>
	  
	  <parent>
	      <groupId>com.bosch.msp</groupId>
	      <artifactId>msp-functionblock-parent</artifactId>
	      <version>3.0.7</version>
	  </parent>
	  
	 <properties>
      <fbName>Fridge</fbName>
      <package>com.bosch.functionblock.demo.fridge</package>
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
		assertEquals(expected, fsa.readTextFile("pom.xml", IFileSystemAccess::DEFAULT_OUTPUT).toString)

	}

}
