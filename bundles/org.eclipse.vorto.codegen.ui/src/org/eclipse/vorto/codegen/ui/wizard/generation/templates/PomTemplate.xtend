package org.eclipse.vorto.codegen.ui.wizard.generation.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext

class PomTemplate implements IFileTemplate<IGeneratorProjectContext> {
	
	override getFileName(IGeneratorProjectContext context) {
		"pom.xml"
	}
	
	override getPath(IGeneratorProjectContext context) {
		return null;
	}
	
	override getContent(IGeneratorProjectContext context,InvocationContext invocationContext) {
		'''
		<project>
			<modelVersion>4.0.0</modelVersion>
			<groupId>«context.packageName»</groupId>
			<artifactId>«context.packageName».«context.generatorName.toLowerCase»</artifactId>
		    <version>1.0.0-SNAPSHOT</version>
			<packaging>eclipse-plugin</packaging>
			
			<properties>
				<tycho-version>0.20.0</tycho-version>
				<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
				<xtext.version>2.7.2</xtext.version>
				<jdk.version>1.7</jdk.version>
			</properties>
		
			<pluginRepositories>
				<pluginRepository>
					<id>tycho-snapshots</id>
					<url>https://oss.sonatype.org/content/groups/public/</url>
				</pluginRepository>
				<pluginRepository>
					<id>cbi-releases</id>
					<url>https://repo.eclipse.org/content/repositories/cbi-releases/</url>
				</pluginRepository>
			</pluginRepositories>
		
			<repositories>
				<repository>
					<id>orbit</id>
					<url>http://download.eclipse.org/tools/orbit/downloads/drops/R20140525021250/repository</url>
					<layout>p2</layout>
				</repository>
				<repository>
					<id>mars</id>
					<layout>p2</layout>
					<url>http://download.eclipse.org/releases/mars</url>
				</repository>
				<repository>
					<id>Xtext Update Site</id>
					<layout>p2</layout>
					<url>http://download.eclipse.org/modeling/tmf/xtext/updates/composite/releases/</url>
				</repository>
				<repository>
					<id>Vorto Update Site</id>
					<layout>p2</layout>
					<url>http://download.eclipse.org/vorto/update/nightly-snapshots/</url>
				</repository>
			</repositories>
		
			<build>
				<sourceDirectory>src</sourceDirectory>
					<plugins>
						<plugin>
							<groupId>org.sonatype.tycho</groupId>
							<artifactId>maven-osgi-packaging-plugin</artifactId>
							<configuration>
								<strictVersions>false</strictVersions>
							</configuration>
						</plugin>
						<plugin>
							<groupId>org.apache.maven.plugins</groupId>
							<artifactId>maven-clean-plugin</artifactId>
							<version>2.5</version>
							<configuration>
								<filesets>
									<fileset>
										<directory>${basedir}/xtend-gen</directory>
										<includes>
											<include>**</include>
										</includes>
										<excludes>
											<exclude>.gitignore</exclude>
										</excludes>
									</fileset>
									<fileset>
										<directory>${basedir}/src/main/generated-sources/xtend</directory>
										<includes>
											<include>**</include>
										</includes>
										<excludes>
											<exclude>.gitignore</exclude>
										</excludes>
									</fileset>
								</filesets>
							</configuration>
						</plugin>
						<plugin>
							<groupId>org.eclipse.xtend</groupId>
							<artifactId>xtend-maven-plugin</artifactId>
							<version>${xtext.version}</version>
							<executions>
								<execution>
									<goals>
										<goal>compile</goal>
										<goal>testCompile</goal>
									</goals>
								</execution>
							</executions>
							<configuration>
								<outputDirectory>xtend-gen</outputDirectory>
							</configuration>
							<dependencies>
								<!-- these dependencies are contributed in Eclipse by the "Xtend Library" 
									classpath container -->
								<dependency>
									<groupId>org.eclipse.xtend</groupId>
									<artifactId>org.eclipse.xtend.lib</artifactId>
									<version>${xtext.version}</version>
									<type>pom</type>
								</dependency>
							</dependencies>
						</plugin>
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
											<source>xtend-gen</source>
											<source>src</source>
										</sources>
									</configuration>
								</execution>
							</executions>
						</plugin>
						<plugin>
							<groupId>org.apache.maven.plugins</groupId>
							<artifactId>maven-release-plugin</artifactId>
							<version>2.5</version>
						</plugin>
						<plugin>
							<groupId>org.eclipse.tycho</groupId>
							<artifactId>tycho-packaging-plugin</artifactId>
							<version>${tycho-version}</version>
						</plugin>
						<plugin>
							<groupId>org.eclipse.tycho</groupId>
							<artifactId>tycho-versions-plugin</artifactId>
							<version>${tycho-version}</version>
						</plugin>
						<plugin>
							<groupId>org.eclipse.tycho</groupId>
							<artifactId>tycho-source-plugin</artifactId>
							<version>${tycho-version}</version>
							<executions>
								<execution>
									<id>attach-sources</id>
									<goals>
										<goal>plugin-source</goal>
									</goals>
								</execution>
							</executions>
						</plugin>
						<plugin>
							<groupId>org.eclipse.tycho</groupId>
							<artifactId>tycho-maven-plugin</artifactId>
							<version>${tycho-version}</version>
							<extensions>true</extensions>
						</plugin>
						<plugin>
							<groupId>org.eclipse.tycho</groupId>
							<artifactId>tycho-compiler-plugin</artifactId>
							<version>${tycho-version}</version>
							<configuration>
								<source>${jdk.version}</source>
								<target>${jdk.version}</target>
							</configuration>
						</plugin>
						<plugin>
							<groupId>org.eclipse.tycho</groupId>
							<artifactId>tycho-surefire-plugin</artifactId>
							<version>${tycho-version}</version>
							<configuration>
								<useUIHarness>false</useUIHarness>
								<useUIThread>false</useUIThread>
							</configuration>
							<executions>
								<execution>
									<id>test</id>
									<phase>test</phase>
									<configuration>
										<includes>
											<include>**/*Test.java</include>
										</includes>
										<excludes>
											<exclude>**/All*Tests.java</exclude>
										</excludes>
									</configuration>
									<goals>
										<goal>test</goal>
									</goals>
								</execution>
							</executions>
						</plugin>
						<plugin>
							<groupId>org.codehaus.mojo</groupId>
							<artifactId>exec-maven-plugin</artifactId>
							<version>1.2.1</version>
						</plugin>
						<plugin>
							<groupId>org.eclipse.xtend</groupId>
							<artifactId>xtend-maven-plugin</artifactId>
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