[![Build Status](https://travis-ci.org/eclipse/vorto.svg?branch=development)](https://travis-ci.org/eclipse/vorto)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/569649bfe2594bedae2cd172e5ee0741)](https://www.codacy.com/app/alexander-edelmann/vorto?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=eclipse/vorto&amp;utm_campaign=Badge_Grade)
[![Quality Gate](https://sonarqube.com/api/badges/gate?key=org.eclipse.vorto%3Aorg.eclipse.vorto.parent%3Adevelopment)](https://sonarqube.com/dashboard/index/org.eclipse.vorto:org.eclipse.vorto.parent:development) 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.eclipse.vorto/org.eclipse.vorto.parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eclipse.vorto/org.eclipse.vorto.parent)

# Getting started with Vorto

[Vorto](http://www.eclipse.org/vorto) is a toolset that lets you describe devices using a simple language and share these descriptions, so-called Information Models, in a centralized [Vorto Repository](http://vorto.eclipse.org). Information Models can be converted into executable source code with the help of Vorto Code Generators. 

Wanna learn more? [Read Tutorials](tutorials/Readme.md)

# Vorto Toolset


### Install & Run

1. Eclipse for DSL Developers
2. Java 8
3. Install Vorto from Eclipse Marketplace 

### Describe once, Integrate Anywhere

1. Open the Model Repository Browser in the Eclipse Vorto Perspective
2. Search for an Information Model, e.g. 'XDK'
3. Right-Click on the model, Choose 'Generate' and Select the Generator(s) of your choice, e.g. CoAP and Web UI. You should see the generated bundles in the Java Perspective.
4. **That's it**! You have just generated CoAP specific bundles and a web-based UI from a single Vorto model.  

# Vorto Repository Server

Setup your own Vorto Server backend in a couple of minutes:

 - Set up [Vorto Repository](server/repo/repository-server/Readme.md)
 - Set up [Vorto Code Generators](server/generators/Readme.md)

# Documentation

Read our [Vorto Documentation](http://www.eclipse.org/vorto/documentation/overview/introduction.html)

# Contact us
 - You want to chat with us ? [![Join the chat at https://gitter.im/eclipse/vorto](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/eclipse/vorto?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
 - You have problems with Vorto ? Open a [GitHub issue](https://github.com/eclipse/vorto/issues)
 - Find out more about the project on our [Vorto Homepage](http://www.eclipse.org/vorto)
 - Reach out to our developers on our [Discussion Forum](http://eclipse.org/forums/eclipse.vorto) 

# Contribute to the Project

When you create a Pull Request, make sure:

1. You have a valid CLA signed with Eclipse
2. All your commits are signed off (git commit -s)
3. Your commit message contains "Fixes #`<Github issue no>`
4. Target to merge your fix is development branch
