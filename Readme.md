# Getting started with Vorto

[![Join the chat at https://gitter.im/eclipse/vorto](https://badges.gitter.im/eclipse/vorto.svg)](https://gitter.im/eclipse/vorto?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Great, that you are interested in the Eclipse Vorto project!

## Automated Setup

Start developing Vorto in a couple of minutes by downloading the [Vorto Developer IDE](https://marketplace.yatta.de/profiles/qTKP)


## Manual Setup

Users with an existing Eclipse IDE, follow these steps:

### Prerequisites

 - [Java] 8
 - [Eclipse] Luna SR2 (4.4.2) 
 - [Xtext] 2.7.x or above
 - [Xtext Antlr] 2.1.x or above 
 - [Apache Maven] 3 (only needed if you would like to build from the command line)

 
### Steps:

  1. git clone [https://github.com/eclipse/vorto.git](https://github.com/eclipse/vorto.git)
  2. Import the project into eclipse 
  3. Generate Xtext Artifacts for all editors files ( Right click -> Run As - Generate Xtext Artifacts), such as
	  1. org.eclipse.vorto.editor.datatype/src/org/eclipse/vorto/editor/datatype/Datatype.xtext
	  2. org.eclipse.vorto.editor.functionblock/src/org/eclipse/vorto/editor/functionblock/Functionblock.xtext
	  3. org.eclipse.vorto.editor.infomodel/src/org/eclipse/vorto/editor/infomodel/InformationModel.xtext
	  4. org.eclipse.vorto.editor.mapping/src/org/eclipse/vorto/editor/mapping/Mapping.xtext 	   
  4. With no compile errors, right click on any plugin Run As - > Eclipse Application would launch new Eclipse application. Add the following VM arguments to your eclipse run configuration
  "-Xms40m -Xmx512m -XX:MaxPermSize=256m"


### Troubleshooting:

  1. If compile errors exists, check vorto.target is checked as active the target platform (Windows -> Preferences - > Plugin Development -> Target Platform - > target definitions - vorto.target)
  2. If network error, check for proxy settings. (Preferences - > General -> Network Connections)  

# Support
-------
For more details and documentation, 
visit 

[Vorto Homepage](http://www.eclipse.org/vorto)

[Vorto Discussions/Community](http://www.eclipse.org/vorto/community.html) 


[Java]:  http://www.oracle.com/technetwork/java/javase/downloads/index.html
[Eclipse]: http://www.eclipse.org/downloads/
[Xtext]: http://www.eclipse.org/Xtext/download.html
[Xtext Antlr]: http://download.itemis.com/updates/
[Apache Maven]: https://maven.apache.org/download.cgi  
  
Contributing to Vorto
---------------------

When you create a Pull Request to Vorto, make sure 

1. all your commits are signed off (git commit -s)
2. you have a valid CLA signed with Eclipse
3. you reference an existing vorto bugzilla issue that your Pull Request fixes
4. targets to be merged with our development branch

