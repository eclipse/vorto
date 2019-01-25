# Extending the Vorto DSL

This tutorial gives a step by step instruction on how to create a DSL that references the Vorto DSL. We are going to use an example DSL called "System Model" that is going to aggregate several information models in one place.

### Clone special branch of Vorto repository

Some artifacts (eclipse plugins) are not on the main tree of Vorto. You need to checkout a special branch.

    git clone https://github.com/eclipse/vorto.git
    cd vorto
    git checkout vortodsl_integration_eclipse

### Install Eclipse Neon for Java and DSL Developers (last neon release)

[Eclipse Neon IDE for Java and DSL Developers - Windows](https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/neon/3/eclipse-dsl-neon-3-win32-x86_64.zip)

[Eclipse Neon IDE for Java and DSL Developers - Linux](https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/neon/3/eclipse-dsl-neon-3-linux-gtk-x86_64.tar.gz)

[Eclipse Neon IDE for Java and DSL Developers - Mac](https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/neon/3/eclipse-dsl-neon-3-macosx-cocoa-x86_64.tar.gz)

### Import projects to Eclipse

Before we import the Vorto projects to Eclipse, let us do a full maven build.

    cd <root of vorto>
    mvn clean install

Remember to set your network connection (if you have proxies) in Eclipse
>Window > Preferences > General > Network Connection

Remember to set your Maven settings file if you need special Maven configuration to reach external Maven repositories.
>Window > Preferences > Maven > User Settings

**Import all Vorto projects in Eclipse**

>File > Import > Maven > Existing Maven Project

**Set the target environment**

>Window > Preferences > Plug-in Development > Target Platform

and uncheck all except for

>target definitions- core- bundles/targetplatform/vorto.target

**Refresh the projects**

Select all projects, right click and click *Refresh*

**Update project maven dependencies**

Select all projects, right click and click *Maven > Update Project ...*

**Build All**

Select all projects and 

>Project > Build All

#### Checkpoint! No error? Continue.

**Modify Vorto MWE2 files to generate files for Eclipse UI plugins**

Modify *GenerateDatatype.mwe2* in *org.eclipse.vorto.editor.datatype* to enable code generation for eclipsePlugin and the IDE

    eclipsePlugin = {
        enabled = true
	}	
	genericIde = {
        enabled = true
    }
	
Do the same thing for *GenerateFunctionblock.mwe2* in *org.eclipse.vorto.editor.functionblock*, *GenerateInformationModel.mwe2* in *org.eclipse.vorto.editor.infomodel*, and *GenerateMapping.mwe2* in *org.eclipse.vorto.editor.mapping*

Now, go to <root>/core-bundles/language/pom.xml and uncomment the commented-out modules.

**Import the UI plugins**

Now, import the UI plugins

>File > Maven > Existing Maven Projects

Point the *Root Directory* to <root>/core-bundles/language, and choose the remaining unimported projects.

**Generate the Xtext UI artifacts**

Now, go to *GenerateDatatype.mwe2* in *org.eclipse.vorto.editor.datatype* and right click

>Run As > MWE2 Workflow

Do the same thing for *GenerateFunctionblock.mwe2* in *org.eclipse.vorto.editor.functionblock*, *GenerateInformationModel.mwe2* in *org.eclipse.vorto.editor.infomodel*, and *GenerateMapping.mwe2* in *org.eclipse.vorto.editor.mapping*

**Test current installation**

Right now, it would be a good idea to test the current vorto plugins if they are functioning correctly, before we add our own plugins.

Select the *org.eclipse.vorto.editor.infomodel.ui* project, right click and 

>Run As > Eclipse Application

Disregard the errors in the console.

In the new Eclipse application, create a new file with a *.infomodel* file extension.

Cut and paste this model to the editor

    namespace org.eclipse.test
    version 1.0.0
    displayname "Test information model"

    infomodel Test {
	
    }

And see if syntax highlighting, and error highlighting are working (try deleting random characters in the keywords). If they are, it means
your installation is working correctly and you can proceed to making your own DSL.

### Use the Xtext wizard to create a new DSL project

**Create our System Model DSL**
>File > New > Other > Xtext > Xtext Project

and go through the wizard with the following values:

>Project name: *org.system.model*

>Name: *org.system.model.Aggregate*

>Extensions: .agg

Click *Next*. In the next window, Check *Eclipse Plugin* and *Generic IDE Support*. Uncheck everything else. Select *Maven* for the preferred build system. Leave the rest to default values.

Click on *Finish*.

After the wizard is finished, it will create five projects for you. The most important to us is the core project (org.system.model) and the projects that end with .ui and .ide.

#### Running the generated project

You can try running the generated project first and see if they are working.

First, due to a weird bug in Xtext, download the file at [antlr-generator-3.2.0-patch.jar](http://download.itemis.com/antlr-generator-3.2.0-patch.jar), rename it to *.antlr-generator-3.2.0-patch.jar* (don't miss the dot (.) at the beginning of the filename), and move it to the root of *org.system.model* project.

Second, generate the rest of the artifact. Right click on the generated .mwe2 file in org.system.model and

>Run as > MWE2 Workflow

Refresh the generated projects just for good measure. Click on the generated project and

>Run as > Eclipse Application

Now, on the new Eclipse window, try creating a file with the *.agg* extension and see if syntax highlighting is working and if error highlighting works if you deviate from your specified grammar.

Refer to *Aggregate.xtext* in *org.system.model* project for the grammar of your newly generated DSL.

If everything is working, continue.

### Linking the new DSL to the Vorto DSL

#### The System Model DSL

The DSL that we would like to create would allow us to aggregate several Vorto information models into one *aggregate* entity. It would allow for source code like this

    namespace org.test
    version 1.0.0
    aggregate MyRoomba {
        BatterySensor as my.testmodel.Battery
        LeftMotor as my.testmodel.Motor
        RightMotor as my.testmodel.Motor
        Vacuum as my.testmodel.Vacuum
    }

Where *my.testmodel.Battery*, *my.testmodel.Motor*, and *my.testmodel.Vacuum* are Vorto information models.

The first thing we have to do is to add a dependency on *org.system.model* to the following plugins

>org.eclipse.vorto.core

>org.eclipse.vorto.editor

>org.eclipse.vorto.editor.datatype

>org.eclipse.vorto.editor.datatype.ide

>org.eclipse.vorto.editor.datatype.ui

>org.eclipse.vorto.editor.functionblock

>org.eclipse.vorto.editor.functionblock.ide

>org.eclipse.vorto.editor.functionblock.ui

>org.eclipse.vorto.editor.infomodel

>org.eclipse.vorto.editor.infomodel.ide

>org.eclipse.vorto.editor.infomodel.ui

Click on the *Manifest.mf* of *org.system.model*, go to the *Dependencies* section and add the plugins above to the *Required Plug-ins*.

Secondly, we now replace the xtext grammar in *Aggregate.xtext* with the *org.system.model* grammar that we want.

    grammar org.system.model.Aggregate with org.eclipse.vorto.editor.infomodel.InformationModel

    import "http://www.eclipse.org/vorto/metamodel/InformationModel" as im

    generate aggregate "http://www.system.org/model/Aggregate"

    Aggregate:
        'namespace' namespace = QualifiedName
        'version' version = VERSION
        'aggregate' aggregateName = ID '{'
            model += Model*
        '}';
	
    Model:
        name = ID 'as' informationModel = [im::InformationModel|QualifiedName];

Notice that we replaced the parent of our DSL to the *Vorto Information Model* DSL. Then we created an import to the information model. The rest that followed is standard Xtext.

Next, we change the MWE workflow file. Let's add the *Vorto Information Model* as a *referencedResource* to the *StandardLanguage* section

    language = StandardLanguage {
        name = "com.bosch.ProdLine"
        fileExtensions = "prodline"
        referencedResource = "platform:/resource/org.eclipse.vorto.core/model/InformationModel.genmodel"
        referencedResource = "platform:/resource/org.eclipse.vorto.core/model/Model.genmodel"
        serializer = {
            generateStub = false
        }
        validator = {
            // composedCheck = "org.eclipse.xtext.validation.NamesAreUniqueValidator"
        }
    }

Next, we run the workflow process once again to regenerate the artifacts (you should do this after every change on the *.xtext* file). 

Right click on the *.mwe2* file and

>Run As > MWE2 Workflow

Next, we need to import several packages to the *org.system.model.ide* plugin.

Go to the *MANIFEST.MF* file of *org.system.model.ide*. In the *Dependencies* section, add the packages below to the *Imported Packages*.

>org.eclipse.vorto.editor.datatype.services

>org.eclipse.vorto.editor.functionblock.services

>org.eclipse.vorto.editor.infomodel.services

Next, we need to add several plugins as dependencies of *org.system.model.ui* plugin.

Go to the *MANIFEST.MF* file of *org.system.model.ui*. In the *Dependencies* section, add the plugins below to the *Required Plug-ins*

>org.eclipse.vorto.core

>org.eclipse.vorto.editor

>org.eclipse.vorto.editor.datatype

>org.eclipse.vorto.editor.datatype.ui

>org.eclipse.vorto.editor.functionblock

>org.eclipse.vorto.editor.functionblock.ui

>org.eclipse.vorto.editor.infomodel

>org.eclipse.vorto.editor.infomodel.ui

Next, we need to instantiate a needed object in *org.system.model*.

Go to the *AggregateRuntimeModule.xtend* class in the *src* folder of *org.system.model* plugin, and add the following

    @Provides def TypeHelper getTypeHelper() {
        return new TypeFileAccessingHelper()
    }

## Finally, let's run our new DSL

Right click on the *org.system.model* plugin and

>Run As > Eclipse Application

On the new Eclipse application, let's first create the information models we are dependent on.

Create three files with the *.infomodel* extension with the following content

*Battery.infomodel*

    namespace my.testmodel
    version 1.0.0
    displayname "Information model for battery"
    infomodel Battery {
    }

*Motor.infomodel*

    namespace my.testmodel
    version 1.0.0
    displayname "Information model for motor"
    infomodel Motor {
    }

*Vacuum.infomodel*

    namespace my.testmodel
    version 1.0.0
    displayname "Information model for vacuum"
    infomodel Vacuum {
    }

Now, create a file called *DifferentialRobot.agg*, and type your new DSL there

*DifferentialRobot.agg*

    namespace org.test
    version 1.0.0
    aggregate DifferentialRobot {
        BatterySensor as my.testmodel.Battery
        LeftMotor as my.testmodel.Motor
        RightMotor as my.testmodel.Motor
        Vacuum as my.testmodel.Vacuum
    }

There should be no syntax highlighting error and Ctrl-clicking on *my.testmodel.Battery* should open the *Battery.infomodel*.

Congratulations, you have just created a DSL that linked to the Vorto Information Model DSL.

A full copy of the org.system.model project is in [link](https://github.com/bsinno/vorto/tree/feature/extend-vorto-dsl/core-bundles/language/sample-extension/org.system.model.parent)