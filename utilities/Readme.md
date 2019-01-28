# Vorto DSL Reader

The Vorto DSL Reader library is a very useful utility library in order to parse local Vorto DSL files. 

## Prerequisite

Add the following maven dependency to your project:

		<dependency>
		   <groupId>org.eclipse.vorto.utilities</groupId>
		   <artifactId>dsl-reader</artifactId>
		   <version>{vorto.version}</version>
		</dependency>


## Parsing individual Vorto files

1. Create a new Workspace with the models

		IModelWorkspace workspace =
	        IModelWorkspace.newReader()
	            .addFile(getClass().getClassLoader().getResourceAsStream(
	                "dsls/model1.infomodel"), ModelType.InformationModel)
	            .addFile(
	                getClass().getClassLoader()
	                    .getResourceAsStream("dsls/model2.fbmodel"),
	                ModelType.Functionblock)
			.read();

2. Read the model, that you are interested in:


		InformationModel model = (InformationModel) workspace.get().stream()
		.filter(p -> p instanceof InformationModel).findAny().get();


## Parsing a Vorto ZIP archive containing Vorto DSL files

1. Create a new Workspace referencing the ZIP archive containing Vorto DSL files:

		IModelWorkspace workspace = IModelWorkspace.newReader()
	        .addZip(new ZipInputStream(getClass().getClassLoader().getResourceAsStream("models.zip")))
	        .read();


2. Read the model, that you are interested in:

	    Model model = workspace.get().stream().filter(p -> p.getName().equals("TI_SensorTag_CC2650"))
		.findAny().get();
