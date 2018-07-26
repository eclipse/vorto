
# Vorto Repository

The Vorto Repository lets you

 * **Describe** device capabilities and functionality as Information Models using built-in Web Editors
 * **Search** Information Models via different criteria
 * **Generate** Code for Information Models to integrate a device with IoT platforms
 * **Import** other (standardized) device descriptions, e.g. LwM2M/IPSO, in order to manage them in the Vorto Repository and use Vorto Code Generators
 * **Review** and **Release** Information Models in order to use these in IoT Business Solutions, e.g. for Device Data Validation.


## Developer Guide

The Vorto Repository provides a set of extension points for customization.

### Importer API

The Vorto Repository manages Vorto Information Models and provides elegant ways to search these models as well as transform these models into runnable source code via Code Generators. 
But how can existing (standardized) device descriptions be integreated into the Vorto eco-system, so that they can benefit from the Repository features? It is just as easy as provide an Importer that manages the validation and conversion into Vorto Information Models. 
Let me show you how this is done by walking you through the basic steps:

#### Step 1. Prepare your pom.xml

Add the following dependencies to your importer project:

	<dependency>
		<groupId>org.eclipse.vorto</groupId>
		<artifactId>repository-server</artifactId>
		<version>${vorto.version}</version>
	</dependency>

	<dependency>
		<groupId>org.eclipse.vorto</groupId>
		<artifactId>repository-api</artifactId>
		<version>${vorto.version}</version>
	</dependency>

If you want to use the Xtend Template Language for converting your model to Vorto Model DSL representations, add the following build plugins to your pom.xml:

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
		<version>2.9.0</version>
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

#### Step 2. Implement the Importer logic

Here is a just a code snippet of the Importer. [Please go here]() to look at the full implementation.

	import org.eclipse.vorto.repository.api.ModelId;
	import org.eclipse.vorto.repository.api.ModelInfo;
	import org.eclipse.vorto.repository.api.ModelType;
	import org.eclipse.vorto.repository.core.FileContent;
	import org.eclipse.vorto.repository.core.IUserContext;
	import org.eclipse.vorto.repository.core.ModelResource;
	import org.eclipse.vorto.repository.importer.AbstractModelImporter;
	import org.eclipse.vorto.repository.importer.FileUpload;
	import org.eclipse.vorto.repository.importer.ModelImporterException;
	import org.eclipse.vorto.repository.importer.ValidationReport;

	public class IPSOImporter extends AbstractModelImporter {

		public IPSOImporter() {
			super(".xml"); // Add the possible file extensions of your imported model files.
		}

		@Override
		public String getKey() {
			return "IPSO";
		}

		@Override
		public String getShortDescription() {
			return "Imports LwM2M / IPSO descriptions";
		}

		@Override
		protected List<ValidationReport> validate(FileUpload fileUpload, IUserContext user) {
			/* Validates the uploaded SDT XMLs e.g. through parsing. An uploaded file can 
			 * potentially create multiple Vorto Models, e.g. Function Blocks and Datatypes. Each of 
			 * these would have its own Validation Report
			 */
		}

		@Override
		protected List<ModelResource> convert(FileUpload fileUpload, IUserContext user) {	
			/* Here you can define what kind of Vorto Model Resources must be created from the   
			 * uploaded file.
			 * It is recommended to use the Xtend Template Language, as it a super convenient way to 
			 * create Vorto DSL files from your specific Object Model. Also, the inherited Super 
			 * Class provides convenient methods to create Model Resource Objects from these DSL 
			 * representations.
			 */
		}

		@Override
		protected void postProcessImportedModel(ModelInfo importedModel, FileContent originalFileContent) {
			/* 
			 * The method is called after the model was saved in the model repository. 
			 * For example, you can add the code here that adds the original file to the repository 
			 * as well, e.g. getModelRepository().addFileContent(importedModel.getId(), originalFileContent); 
			 */
		}
	}

#### Step 3. Register the Importer

You have two options of registering your importer in the Repository

1. Create Pull Request for your Importer. 
2. Get in touch with us for special handling.


