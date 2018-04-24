# Vorto Generator Maven-Plugin

It is now very easy to invoke vorto code generators directly from your maven build script. To do so, just add
the generator-maven-plugin to your build configuration. 
For example, the following snippet invokes the Latex Generator for all information models in your project and stores the generated latex files in the src-gen folder:

	<build>
		<plugins>
			<plugin>
				<groupId>org.eclipse.vorto</groupId>
				<artifactId>vorto-maven-plugin</artifactId>
				<version>0.10.0.M2</version>
				<configuration>
					<generatorClass>org.eclipse.vorto.codegen.examples.latex.LatexGenerator</generatorClass>
					<outputPath>${project.basedir}/src-gen</outputPath>
				</configuration>
				<dependencies>
					<dependency>
						<groupId>org.eclipse.vorto</groupId>
						<artifactId>org.eclipse.vorto.codegen.examples.latex</artifactId>
						<version>${project.version}</version>
					</dependency>
				</dependencies>
			</plugin>
		</plugins>
	</build>
	
## Run
Execute the following command to invoke the generator from your command line:

	mvn clean vorto:generate
	
## Generate PDF from Vorto Models

Now that you know how to easily generate sources from your information models in your project, we can even go further and process the vorto generated files with other maven plugins in the build pipeline.
In the following example, we are going to firstly convert the vorto information models into latex files and then use a 3rd party maven plugin to lastly transform the latex files into PDF's. 

Here is what you have to do:

1. Install pdflatex for your OS
2. Add the following pom.xml to your Vorto project:

		<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
		<modelVersion>4.0.0</modelVersion>
		<groupId>org.eclipse.vorto</groupId>
		<artifactId>example</artifactId>
		<version>0.0.1-SNAPSHOT</version>
		
		<!-- Dependency for the latex-pdf maven converter plugin -->
		<pluginRepositories>
			<pluginRepository>
				<id>maven-latex-plugin-repo</id>
				<url>http://akquinet.github.com/maven-latex-plugin/maven2/</url>
				<releases>
					<enabled>true</enabled>
				</releases>
			</pluginRepository>
		</pluginRepositories>

		<build>
			<plugins>
				<!-- Declare the vorto latex generator plugin -->
				<plugin>
					<groupId>org.eclipse.vorto</groupId>
					<artifactId>vorto-maven-plugin</artifactId>
					<version>0.10.0.M2</version>
					<configuration>
						<generatorClass>org.eclipse.vorto.codegen.examples.latex.LatexGenerator</generatorClass>
						<outputPath>${project.basedir}/src/site</outputPath>
					</configuration>
					<dependencies>
						<dependency>
							<groupId>org.eclipse.vorto</groupId>
							<artifactId>org.eclipse.vorto.codegen.examples.latex</artifactId>
							<version>${project.version}</version>
						</dependency>
					</dependencies>
				</plugin>
				
				<!-- Declare the latex to pdf converter plugin -->
				<plugin>
					<groupId>de.akquinet.jbosscc.latex</groupId>
					<artifactId>maven-latex-plugin</artifactId>
					<version>1.2</version>
					<configuration>
						<settings>
							<texDirectory>${basedir}/src/site/latex</texDirectory>
							<texCommand>/Library/TeX/texbin/pdflatex</texCommand>
						</settings>
					</configuration>
				</plugin>
			</plugins>
		</build>
	</project>

3. Run the command to generate information models into PDF's

	mvn clean vorto:generate latex:latex