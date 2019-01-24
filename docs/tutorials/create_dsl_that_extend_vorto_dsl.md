# Extending the Vorto DSL

This tutorial gives a step by step instruction on how to create a DSL that uses the Vorto DSL.

## General Steps
* Clone special branch of Vorto repository
* Install Eclipse Neon for Java and DSL Developers (last neon release)
* Import the projects in the cloned repository to Eclipse
* Use Xtext wizard to create a new DSL project
* Link the new DSL to the Vorto to be able to reference it.

### Clone special branch of Vorto repository

Some artifacts (eclipse plugins) are not on the main tree of Vorto. You need to checkout a special branch.

    git clone https://github.com/eclipse/vorto.git
    cd vorto
    git checkout vortodsl_integration_eclipse

### Install Eclipse Neon for Java and DSL Developers (last neon release)

[Eclipse Neon IDE for Java and DSL Developers - Windows](https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/neon/3/eclipse-dsl-neon-3-win32-x86_64.zip)

[Eclipse Neon IDE for Java and DSL Developers - Linux]
(https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/neon/3/eclipse-dsl-neon-3-linux-gtk-x86_64.tar.gz)

[Eclipse Neon IDE for Java and DSL Developers - Mac]
(https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/neon/3/eclipse-dsl-neon-3-macosx-cocoa-x86_64.tar.gz)

### Import projects to Eclipse

Before we import the Vorto projects to Eclipse, let us do a full maven build.

    cd <root of vorto>
    mvn clean install

Remember to set your network connection (if you have proxies) in Eclipse
>Window > Preferences > General > Network Connection

** Import all Vorto projects in Eclipse **

>File > Import > Maven > Existing Maven Project

** Set the target environment **

>Window > Preferences > Plug-in Development > Target Platform

and uncheck all except for

>target definitions- core- bundles/targetplatform/vorto.target

** Refresh the projects **

Select all projects, right click and click *Refresh*

** Update project maven dependencies **

Select all projects, right click and click *Maven > Update Project ...*

** Build All **

Select all projects and 

>Project > Build All

#### Checkpoint! No error? Continue.

** Test current installation **

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


