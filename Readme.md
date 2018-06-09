[![Build Status](https://travis-ci.org/eclipse/vorto.svg?branch=development)](https://travis-ci.org/eclipse/vorto)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/569649bfe2594bedae2cd172e5ee0741)](https://www.codacy.com/app/alexander-edelmann/vorto?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=eclipse/vorto&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.eclipse.vorto/org.eclipse.vorto.parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eclipse.vorto/org.eclipse.vorto.parent)

# Overview

[Vorto](http://www.eclipse.org/vorto) provides an **Eclipse Toolset** that lets you describe the device functionality and characteristics as **Information Models**. These models are managed in a [Vorto Repository](http://vorto.eclipse.org). [Code generators](http://vorto.eclipse.org/#/generators) convert these models into device - specific "stubs" that run on the device and send Information Model compliant messages to an IoT Backend. In order to process these messages in the IoT backend, Vorto offers a set of **technical components**, for example parsers and validators. For devices sending arbitrary, non-Vorto, messages to an IoT backend, the **Vorto Mapping Engine** helps you to execute device message transformations to IoT Platform specific meta-models, e.g. Eclipse Ditto or AWS IoT Shadow.  
 
 <img src="./website/static/images/vorto_technicalview.png" width="90%"/>


# Getting started with Vorto 

1. Download the [Vorto Eclipse Toolset Plugins](https://marketplace.eclipse.org/content/vorto-toolset) and install them into your [Eclipse for DSL Developers](https://www.eclipse.org/downloads/packages/eclipse-ide-java-and-dsl-developers/oxygen3a)

2. [Read Getting Started Guide](https://www.eclipse.org/vorto/gettingstarted/)

# Developer Guide


## Generator SDK

With the **Generator SDK** you can easily create and plug-in a new Vorto Generator. [Read the Tutorial](tutorials/tutorial_create_generator.md)

Here is a list of currently supported [Vorto Generators](http://vorto.eclipse.org/#/generators)

## Repository Java Client

Search models and generate code via the [Repository Java Client](repository-java-client/Readme.md)

## Payload Mapping Engine

Map arbitrary device payload, such as JSON or BLE GATT, to standardized data, that is described by Vorto Information Models. [Payload Mapping Documentation](https://www.eclipse.org/vorto/documentation/mappingengine)  

# Documentation

- Read our [tutorials](https://www.eclipse.org/vorto/tutorials/)
- Read our [Vorto Documentation](https://www.eclipse.org/vorto/documentation)

# Contact us
 - You want to chat with us ? [![Join the chat at https://gitter.im/eclipse/vorto](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/eclipse/vorto?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
 - You have problems with Vorto ? Open a [GitHub issue](https://github.com/eclipse/vorto/issues)
 - Find out more about the project on our [Vorto Homepage](http://www.eclipse.org/vorto)
 - Reach out to our developers on our [Discussion Forum](http://eclipse.org/forums/eclipse.vorto) 

# Contribute to the Project

Make sure, that you have installed [Vorto for contributors](tutorials/tutorial_vortosetup_contributors.md)

When you create a Pull Request, make sure:

1. You have a valid CLA signed with Eclipse
2. All your commits are signed off (git commit -s)
3. Your commit message contains "Fixes #`<Github issue no>`
4. Target to merge your fix is development branch


