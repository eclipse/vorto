[![Build Status](https://travis-ci.org/eclipse/vorto.svg?branch=development)](https://travis-ci.org/eclipse/vorto)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/56964)
[![Stories in Progress](https://badge.waffle.io/eclipse/vorto.svg?label=in%20progress&title=Issues%20In%20Progress)](http://waffle.io/eclipse/vorto)
[![Throughput Graph](https://graphs.waffle.io/eclipse/vorto/throughput.svg)](https://waffle.io/eclipse/vorto/metrics)

# Getting started with Vorto

[Vorto](http://www.eclipse.org/vorto) is a toolset that lets you describe devices using a simple language and share these descriptions, so-called Information Models, in a centralized [Vorto Repository](http://vorto.eclipse.org). Convert the Information Models into executable code with the help of Vorto Code Generators. 

# Vorto Toolset

### Install & Run

1. Eclipse for DSL Developers
2. Java 8
3. Install Vorto from Eclipse Marketplace 

Alternatively, download the [Vorto IDE](https://marketplace.yatta.de/profiles/ziXJ) and we set everything up for you, automatically.

### Describe once, Integrate Anywhere

1. Open the Model Repository Browser in the Eclipse Vorto Perspective
2. Search for an Information Model, e.g. 'XDK'
3. Right-Click on the model, Choose 'Generate' and Select the Generator(s) of your choice, e.g. CoAP and Web UI. You should see the generated bundles in the Java Perspective.
4. **That's it**! You have just generated CoAP specific bundles and a web-based UI from a single Vorto model.  

# Vorto Repository Server

Setup your own Vorto Server backend in a couple of minutes:

 - Set up [Vorto Repository](server/repo/repo-ui/Readme.md)
 - Set up [Vorto Code Generators](server/generators/Readme.md)

# Documentation

Read our [Vorto Documentation](http://www.eclipse.org/vorto/documentation/overview/introduction.html)

# Contact us
 - You want to chat with us ? [![Join the chat at https://gitter.im/eclipse/vorto](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/eclipse/vorto?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
 - You have problems with Vorto ? Open a [GitHub issue](https://github.com/eclipse/vorto/issues)
 - Find out more about the project on our [Vorto Homepage](http://www.eclipse.org/vorto)
 - Reach out to our developers on our [Discussion Forum](http://eclipse.org/forums/eclipse.vorto) 

# Contribute to the Project

Download the [Vorto for Contributors IDE](https://marketplace.yatta.de/profiles/qTKP). 

When you create a Pull Request, make sure:

1. You have a valid CLA signed with Eclipse
2. All your commits are signed off (git commit -s)
3. Your commit message contains "Fixes #`<Github issue no>`
4. Target to merge your fix is development branch
